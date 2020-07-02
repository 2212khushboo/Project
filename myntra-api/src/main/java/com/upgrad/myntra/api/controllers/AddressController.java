package com.upgrad.myntra.api.controllers;

import com.upgrad.myntra.api.model.*;
import com.upgrad.myntra.service.business.AddressService;
import com.upgrad.myntra.service.business.CustomerService;
import com.upgrad.myntra.service.entity.AddressEntity;
import com.upgrad.myntra.service.entity.CustomerAddressEntity;
import com.upgrad.myntra.service.entity.CustomerEntity;
import com.upgrad.myntra.service.exception.AddressNotFoundException;
import com.upgrad.myntra.service.exception.AuthorizationFailedException;
import com.upgrad.myntra.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping()
public class AddressController {
    @Autowired
    CustomerService customerService;
    @Autowired
    AddressService addressService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST,path = "/address",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveaddress(@RequestBody(required = false) SaveAddressRequest saveAddressRequest, @RequestHeader("authorization") final String auth) throws SaveAddressException, AuthorizationFailedException, AddressNotFoundException {
        String token= auth.split("Bearer")[1];

        CustomerEntity cust=customerService.getCustomer(token);

        final AddressEntity addressEntity = new AddressEntity();

        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setState(addressService.getStateByUUID(saveAddressRequest.getStateUuid()));
        addressEntity.setUuid(UUID.randomUUID().toString());
        CustomerAddressEntity customeraddressentity= new CustomerAddressEntity();
        customeraddressentity.setAddress(addressEntity);
        customeraddressentity.setCustomer(cust);
        AddressEntity addressEntity1=addressService.saveAddress(addressEntity,customeraddressentity);
        SaveAddressResponse saveAddressResponse= new SaveAddressResponse();
        saveAddressResponse.setId(addressEntity1.getUuid());
        saveAddressResponse.setStatus("LOGGED IN SUCCESSFULLY.");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse , HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/address/customer",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseEntity<AddressListResponse>  getallsavedaddress(@RequestHeader("authorization") String acess_token) throws AuthorizationFailedException
    {
        String token = acess_token.split("Bearer")[1];
        CustomerEntity customerEntity= customerService.getCustomer(token);
        List<AddressEntity> addressEntityList= addressService.getAllAddress(customerEntity);
        AddressListResponse addressListResponse = new AddressListResponse();
        for(AddressEntity addressEntity : addressEntityList)
        {


            AddressList addressList = new AddressList();
            addressList.setId(UUID.fromString(addressEntity.getUuid()));
            addressList.setLocality(addressEntity.getLocality());
            addressList.setCity(addressEntity.getCity());
            addressList.setFlatBuildingName(addressEntity.getFlatBuilNo());
            addressList.setPincode(addressEntity.getPincode());
            AddressListState addressListState = new AddressListState();
            addressListState.setId(UUID.fromString(addressEntity.getState().getUuid()));
            addressListState.setStateName(addressEntity.getState().getStateName());
            addressList.setState(addressListState);
            addressListResponse.addAddressesItem(addressList);

        }
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.DELETE,path = "/address/{address_id}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteaddress(@RequestHeader("authorization") String acess_token,@PathVariable("UUID") String uuid) throws AuthorizationFailedException, AddressNotFoundException {
        String token = acess_token.split("Bearer")[1];
        CustomerEntity customerEntity= customerService.getCustomer(token);
        AddressEntity  addressEntity= addressService.getAddressByUUID(uuid,customerEntity);
        AddressEntity delete= addressService.deleteAddress(addressEntity);
        DeleteAddressResponse deleteAddressResponse= new DeleteAddressResponse();
        deleteAddressResponse.setId(UUID.fromString(delete.getUuid()));
        deleteAddressResponse.setStatus("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse,HttpStatus.OK);
    }


}

