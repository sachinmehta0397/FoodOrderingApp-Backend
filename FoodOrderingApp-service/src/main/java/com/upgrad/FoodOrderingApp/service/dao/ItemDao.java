package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * This class provide the necessary methods to access the ItemEntity
 */
@Repository
public class ItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get Item by UUID
     *
     * @param uuid UUID
     * @return ItemEntity or NULL
     */
    public ItemEntity getItemByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("getItemByUUID", ItemEntity.class).setParameter("uuid", uuid).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }
}
