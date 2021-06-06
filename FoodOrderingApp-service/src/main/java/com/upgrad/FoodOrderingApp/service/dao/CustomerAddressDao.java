package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provide the necessary methods to access the CustomerAddressEntity
 */
@Repository
public class CustomerAddressDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save a Customer Address Details in the Database
     *
     * @param customerAddressEntity Customer Address Details to be Saved
     * @return Saved Customer Address Details
     */
    public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }

    /**
     * Get the Customer Address Details based on the Customer Details
     *
     * @param customerEntity Customer Details
     * @return Customer Address details matching the Customer Details or NULL
     */
    public List<CustomerAddressEntity> getAllCustomerAddressByCustomer(CustomerEntity customerEntity) {
        try {
            Integer active = 1;
            return entityManager.createNamedQuery("getAllCustomerAddressByCustomer", CustomerAddressEntity.class).setParameter("customer_entity", customerEntity).setParameter("active", active).getResultList();

        } catch (NoResultException nre) {
            return new ArrayList<>();
        }
    }

    /**
     * Get the Customer Address Details based on the Address
     *
     * @param addressEntity Address
     * @return Customer Address details matching the Address or NULL
     */
    public CustomerAddressEntity getCustomerAddressByAddress(AddressEntity addressEntity) {
        try {
            return entityManager.createNamedQuery("getCustomerAddressByAddress", CustomerAddressEntity.class).setParameter("address_entity", addressEntity).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }
}
