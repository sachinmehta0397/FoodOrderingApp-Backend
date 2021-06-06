package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This class provide the necessary methods to access the RestaurantEntity
 */
@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get Restaurant Details by UUID
     *
     * @param uuid UUID
     * @return Restaurant Details Matching the UUID or NULL
     */
    public RestaurantEntity getRestaurantByUuid(String uuid) {
        try {
            RestaurantEntity restaurantEntity = entityManager.createNamedQuery("getRestaurantByUuid", RestaurantEntity.class).setParameter("uuid", uuid).getSingleResult();
            return restaurantEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get the list Restaurant by Name
     *
     * @param restaurantName Restaurant Name
     * @return Restaurant Details Matching the Name or NULL
     */
    public List<RestaurantEntity> restaurantsByName(String restaurantName) {
        try {
            String restaurantNameLow = "%" + restaurantName.toLowerCase() + "%"; // to make a check with lower
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("restaurantsByName", RestaurantEntity.class).setParameter("restaurant_name_low", restaurantNameLow).getResultList();
            return restaurantEntities;
        } catch (NoResultException nre) {
            return null;
        }

    }

    /**
     * Get the list of Restaurant by Ratings
     *
     * @return List of Restaurant or NULL
     */
    public List<RestaurantEntity> restaurantsByRating() {
        try {
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("restaurantsByRating", RestaurantEntity.class).getResultList();
            return restaurantEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Update Restaurant Rating
     *
     * @param restaurantEntity Restaurant Entity
     * @return Updated Restaurant Entity
     */
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity) {
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }
}
