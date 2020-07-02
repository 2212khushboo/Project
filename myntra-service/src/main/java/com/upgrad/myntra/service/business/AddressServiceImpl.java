package com.upgrad.myntra.service.business;



import com.upgrad.myntra.service.dao.AddressDao;
import com.upgrad.myntra.service.entity.AddressEntity;
import com.upgrad.myntra.service.entity.CustomerAddressEntity;
import com.upgrad.myntra.service.entity.CustomerEntity;
import com.upgrad.myntra.service.entity.StateEntity;
import com.upgrad.myntra.service.exception.AddressNotFoundException;
import com.upgrad.myntra.service.exception.AuthorizationFailedException;
import com.upgrad.myntra.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class AddressServiceImpl implements AddressService{
	@Autowired
	private AddressDao addressdao ;
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AddressEntity saveAddress(AddressEntity addressEntity, CustomerAddressEntity customerAddressEntity) throws
			SaveAddressException {
		//validations
		String regex= "^(?=.*[0-9]).{6}$";
		Pattern pat= Pattern.compile(regex);
		if(addressEntity.getCity().isEmpty()||addressEntity.getFlatBuilNo().isEmpty()||addressEntity.getLocality().isEmpty()||addressEntity.getPincode().isEmpty()||addressEntity.getUuid().isEmpty())
		{
			throw new SaveAddressException("SAR-001","No field can be empty");
		}

		if(!pat.matcher(addressEntity.getPincode()).matches())
		{
			throw new SaveAddressException("SAR-002","Invalid pincode");
		}

		addressdao.saveAddress(addressEntity);
		saveCustomerAddress(customerAddressEntity);
		return  addressEntity;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity) {
		return addressdao.saveCustomerAddress(customerAddressEntity);
	}

	@Override
	public StateEntity getStateByUUID(String uuid)  throws
			AddressNotFoundException
	{



		if(addressdao.getStateByUUID(uuid)==null)
		{
			throw new AddressNotFoundException("ANF-002","No state by this id");
		}
		return addressdao.getStateByUUID(uuid);
	}
	@Override
	public  List<AddressEntity> getAllAddress(CustomerEntity customerEntity)
	{
		return addressdao.getAllAddress(customerEntity);
	}

	@Override
	public AddressEntity getAddressByUUID(String uuid,CustomerEntity customerEntity)  throws
			AddressNotFoundException {

		if(uuid.isEmpty())
		{
			throw new AddressNotFoundException("ANF-005","Address field cannot be empty.");
		}
		if (addressdao.getAddressByUUID(uuid) == null)
		{
			throw new AddressNotFoundException("ANF-003", "No address by this id");
		}
		if(addressdao.getCustomerByAddress(uuid).getCustomer()!=customerEntity)
		{
			throw new AddressNotFoundException("ANF-004","You are not authorized to view/update/delete any one else's address");
		}
		return addressdao.getAddressByUUID(uuid);
	}

	@Override
	public AddressEntity deleteAddress(AddressEntity addressEntity) {
		return addressdao.deleteAddress(addressEntity);

	}

}
