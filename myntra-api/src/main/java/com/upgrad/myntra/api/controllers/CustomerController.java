package com.upgrad.myntra.api.controllers;

import com.upgrad.myntra.api.model.*;
import com.upgrad.myntra.service.business.CustomerService;
import com.upgrad.myntra.service.entity.CustomerAuthEntity;
import com.upgrad.myntra.service.entity.CustomerEntity;
import com.upgrad.myntra.service.exception.AuthenticationFailedException;
import com.upgrad.myntra.service.exception.AuthorizationFailedException;
import com.upgrad.myntra.service.exception.SignUpRestrictedException;
import com.upgrad.myntra.service.exception.UpdateCustomerException;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;



        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    public CustomerService customerService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signUp(@RequestBody (required = false) final SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException {



        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setEmailAddress(signupCustomerRequest.getEmailAddress());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setSalt(signupCustomerRequest.getPassword());
        customerEntity.setUuid(UUID.randomUUID().toString());

        final CustomerEntity responseCustomer = customerService.saveCustomer(customerEntity);
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse();
        signupCustomerResponse.setId(responseCustomer.getUuid());
        signupCustomerResponse.setStatus("Customer Registered");
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String auth) throws AuthenticationFailedException {

        //call to service to login
        byte[] decode = Base64.getDecoder().decode(auth.split("Basic")[1]);
        String decodedText= new String(decode);
        String[] decodedArray= decodedText.split(":");
        //Use https://www.base64encode.org/ to create the encoded string


        CustomerAuthEntity customerauthentity= customerService.login(decodedArray[0],decodedArray[1]);

        CustomerEntity user= customerauthentity.getCustomer();


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(customerauthentity.getUuid());
        loginResponse.setMessage("Logged In Successfully");
        loginResponse.setFirstName(user.getFirstName());
        loginResponse.setLastName(user.getLastName());
        loginResponse.setEmailAddress(user.getEmailAddress());
        loginResponse.setContactNumber(user.getContactNumber());
        HttpHeaders headers=new HttpHeaders();
        headers.add("acess-token",customerauthentity.getAccess_token());
        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST,path="/logout",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String token ) throws AuthorizationFailedException {
        String tok= token.split("Bearer")[1];


        CustomerAuthEntity customer= customerService.logout(tok);
        LogoutResponse logoutResponse= new LogoutResponse();
        logoutResponse.setId(customer.getUuid());
        logoutResponse.setMessage("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.PUT,path="/changepassword",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changepassword(@RequestHeader("authorization") final String acess, @RequestBody(required = false) final UpdatePasswordRequest  updatePasswordRequest) throws AuthorizationFailedException, UpdateCustomerException {
        String token= acess.split("Bearer")[1];
        CustomerEntity user= customerService.getCustomer(token);


        CustomerEntity customerEntity= customerService.changepassword(updatePasswordRequest.getOldPassword(),updatePasswordRequest.getNewPassword(),user);

        UpdatePasswordResponse response=new UpdatePasswordResponse();
        response.setId(customerEntity.getUuid());
        response.setStatus("CUSTORMER PASSWORD UPDATED SUCCESSFULLY.");
        return new ResponseEntity<UpdatePasswordResponse>(response,HttpStatus.OK);
    }

}
