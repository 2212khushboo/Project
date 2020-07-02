package com.upgrad.myntra.service.business;


import com.upgrad.myntra.service.dao.CustomerDao;

import com.upgrad.myntra.service.entity.CustomerAuthEntity;
import com.upgrad.myntra.service.entity.CustomerEntity;
import com.upgrad.myntra.service.exception.AuthenticationFailedException;
import com.upgrad.myntra.service.exception.AuthorizationFailedException;
import com.upgrad.myntra.service.exception.SignUpRestrictedException;
import com.upgrad.myntra.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service

public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)

    public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {
        //handle the validations

        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        String cont= "^(?=.*[0-9]).{10}$ " ;
        String pass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$";

        Pattern pat = Pattern.compile(regex);
        Pattern pat1 = Pattern.compile(pass);
        Pattern pat2= Pattern.compile(cont);

        if (customerEntity.getFirstName().isEmpty()) {
            throw new SignUpRestrictedException("SGR -005", "No fields should be empty except Last Name");
        }
        if (customerEntity.getPassword().isEmpty()) {
            throw new SignUpRestrictedException("SGR -005", "No fields should be empty except Last Name");
        }


        if (customerEntity.getEmailAddress().isEmpty()) {
            throw new SignUpRestrictedException("SGR -005", "No fields should be empty except Last Name");
        }

        if (customerEntity.getContactNumber().isEmpty()) {
            throw new SignUpRestrictedException("SGR -005", "No fields should be empty except Last Name");
        }
        if(!pat2.matcher(customerEntity.getContactNumber()).matches())
        {
            throw new SignUpRestrictedException("SGR-003","Invalid Contact number");
        }
        if (!pat.matcher(customerEntity.getEmailAddress()).matches()) {
            throw new SignUpRestrictedException("SGR-002", "invalid email format");
        }
        if (!pat1.matcher(customerEntity.getPassword()).matches()) {
            throw new SignUpRestrictedException("SGR-004", "Weak Password!");
        }
        if(!pat2.matcher(customerEntity.getContactNumber()).matches())
        {
            throw new SignUpRestrictedException("SGR-003","Invalid Contact Number");
        }

        CustomerEntity contact = customerDao.getCustomerByContact(customerEntity.getContactNumber());
        if (contact != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }


        if(customerDao.getCustomerByContact(customerEntity.getContactNumber()) != null) {
            throw new SignUpRestrictedException("SGR-001 " , "This contact number is already registered! Try other contact number.");
        }







        String[] encryptPassoword = passwordCryptographyProvider.encrypt(customerEntity.getPassword());

        customerEntity.setSalt(encryptPassoword[0]);
        customerEntity.setPassword(encryptPassoword[1]);

        return customerDao.saveCustomer(customerEntity);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity login(final String contactNumber, final String password) throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDao.getCustomerByContact(contactNumber);

        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            customerAuthEntity.setCustomer(customerEntity);
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expiresAt = now.plusHours(8);

            customerAuthEntity.setLoginAt(ZonedDateTime.now());
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setAccess_token(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));

            return customerDao.createCustomerAuth(customerAuthEntity);
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }


    }

    public void authorization(String access_token) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerauthByAccesstoken(access_token);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        if (ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
    }

    @Override
    public CustomerEntity getCustomer(String access_token) throws AuthorizationFailedException {

        authorization(access_token);
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerauthByAccesstoken(access_token);
        return customerAuthEntity.getCustomer();
    }
    @Override
    public CustomerAuthEntity logout(String access_token) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerauthByAccesstoken(access_token);
        authorization(access_token);
        customerAuthEntity.setLogoutAt(ZonedDateTime.now());
        return customerDao.logout(customerAuthEntity);
    }
    @Override
    public CustomerEntity changepassword(String oldPassword, String newPassword, CustomerEntity user) throws UpdateCustomerException
    {
        String pass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$";
        Pattern pat1 = Pattern.compile(pass);
        if(oldPassword.isEmpty()||newPassword.isEmpty())
        {
            throw new UpdateCustomerException("UCR-003","No field should be empty.");
        }
        if(!oldPassword.matches(user.getPassword()))
        {
            throw new UpdateCustomerException("UCR-004","incorrect Old Password");
        }
        if(!pat1.matcher(newPassword).matches())
        {
            throw new UpdateCustomerException("UCR-001","Weak Password!");
        }



        return customerDao.changepassword(user);
    }

}
