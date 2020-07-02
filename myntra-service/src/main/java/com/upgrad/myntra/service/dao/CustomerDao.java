package com.upgrad.myntra.service.dao;



import com.upgrad.myntra.service.entity.CustomerAuthEntity;
import com.upgrad.myntra.service.entity.CustomerEntity;

import java.time.ZonedDateTime;

/*
 * This CustomerDao interface gives the list of all the dao methods that exist in the customer dao implementation class.
 * Service class will be calling the dao methods by this interface.
 */
public interface CustomerDao {

    CustomerEntity saveCustomer(CustomerEntity customerEntity);


    CustomerEntity getCustomerByContact(String contactNumber);


    CustomerAuthEntity getCustomerauthByAccesstoken(String access_token);


    CustomerAuthEntity createCustomerAuth(CustomerAuthEntity customerAuthEntity);

    CustomerAuthEntity logout(CustomerAuthEntity customerAuthEntity);

    CustomerEntity changepassword(CustomerEntity user);
}
