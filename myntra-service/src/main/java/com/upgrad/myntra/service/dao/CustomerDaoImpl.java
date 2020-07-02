package com.upgrad.myntra.service.dao;



import com.upgrad.myntra.service.entity.CustomerAuthEntity;
import com.upgrad.myntra.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


/**
 * CustomerDao class provides the database access for all the endpoints in customer controller.
 */
@Repository
@Transactional
public class CustomerDaoImpl implements CustomerDao {

    //When a container of the application(be it a Java EE container or any other custom container like Spring) manages the lifecycle of the Entity Manager, the Entity Manager is said to be Container Managed. The most common way of acquiring a Container Managed EntityManager is to use @PersistenceContext annotation on an EntityManager attribute.
    @PersistenceContext
    private EntityManager entityManager;


    public CustomerEntity saveCustomer(CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    public CustomerEntity getCustomerByContact(String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContact", CustomerEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerEntity getCustomer(Integer id) {
        try {
            return entityManager.createNamedQuery("customerById", CustomerEntity.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAuthEntity createCustomerAuth(CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }

    public CustomerAuthEntity logout(CustomerAuthEntity customerAuthEntity) {
        return entityManager.merge(customerAuthEntity);
    }

    public CustomerEntity changepassword(CustomerEntity customerEntity) {
        return entityManager.merge(customerEntity);
    }

    @Override
    public CustomerAuthEntity getCustomerauthByAccesstoken(String access_token) {
        try {
            return entityManager.createNamedQuery("getcustomerauthByAccesstoken", CustomerAuthEntity.class).setParameter("access_token", access_token).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
