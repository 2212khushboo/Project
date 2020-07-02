package com.upgrad.myntra.service.business;



import com.upgrad.myntra.service.entity.CustomerAuthEntity;
import com.upgrad.myntra.service.entity.CustomerEntity;
import com.upgrad.myntra.service.exception.AuthenticationFailedException;
import com.upgrad.myntra.service.exception.AuthorizationFailedException;
import com.upgrad.myntra.service.exception.SignUpRestrictedException;
import com.upgrad.myntra.service.exception.UpdateCustomerException;

/*
 * This CustomerService interface gives the list of all the service that exist in the customer service implementation class.
 * Controller class will be calling the service methods by this interface.
 */
public interface CustomerService {

    CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException;

    CustomerAuthEntity login(final String contactNumber,final  String password) throws AuthenticationFailedException;

    CustomerEntity getCustomer(String access_token) throws AuthorizationFailedException;

    CustomerAuthEntity logout(String acess_token) throws AuthorizationFailedException;

    CustomerEntity changepassword(String oldPassword, String newPassword, CustomerEntity customerEntity) throws UpdateCustomerException;
}
