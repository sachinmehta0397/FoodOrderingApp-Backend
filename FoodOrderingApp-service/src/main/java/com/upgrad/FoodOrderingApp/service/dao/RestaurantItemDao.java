package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This class provide the necessary methods to access the RestaurantItemEntity
 */
@Repository
public class RestaurantItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get the Items by Restaurant
     *
     * @param restaurantEntity Restaurant Entity
     * @return RestaurantItemEntity or NULL
     */
    public List<RestaurantItemEntity> getItemsByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            List<RestaurantItemEntity> restaurantItemEntities = entityManager.createNamedQuery("getItemsByRestaurant", RestaurantItemEntity.class).setParameter("restaurant", restaurantEntity).getResultList();
            return restaurantItemEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
