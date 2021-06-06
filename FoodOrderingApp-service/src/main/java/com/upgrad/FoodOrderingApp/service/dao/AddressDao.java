package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * This class provide the necessary methods to access the AddressEntity
 */
@Repository
public class AddressDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save a Address Details in the Database
     *
     * @param addressEntity Address Details to be Saved
     * @return Saved Address Details
     */
    public AddressEntity saveAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     * Update the Active Status in the Database
     *
     * @param addressEntity Address Details for which Active Status to be Updated
     * @return Address Details for which Active Status has been Updated
     */
    //To update Active Status.
    public AddressEntity updateAddressActiveStatus(AddressEntity addressEntity) {
        entityManager.merge(addressEntity);
        return addressEntity;
    }

    /**
     * Delete a Address Details in the Database
     *
     * @param addressEntity Address Entity to be Saved
     * @return Deleted Address Details
     */
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    /**
     * Get the Address Details based on the UUID
     *
     * @param uuid UUID
     * @return Address details matching the UUID or NULL
     */
    public AddressEntity getAddressByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("getAddressByUuid", AddressEntity.class).setParameter("uuid", uuid).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }
}
