package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This class provide the necessary methods to access the RestaurantCategoryEntity
 */
@Repository
public class RestaurantCategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get the list of RestaurantCategoryEntity by Category
     *
     * @param categoryEntity Category of the Restaurant
     * @return RestaurantCategoryEntity or NULL
     */
    public List<RestaurantCategoryEntity> getRestaurantByCategory(CategoryEntity categoryEntity) {
        try {
            List<RestaurantCategoryEntity> restaurantCategoryEntities = entityManager.createNamedQuery("getRestaurantByCategory", RestaurantCategoryEntity.class).setParameter("category", categoryEntity).getResultList();
            return restaurantCategoryEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get the list of Restaurant Category
     *
     * @param restaurantEntity Restaurant Entity
     * @return List of RestaurantCategoryEntity
     */
    public List<RestaurantCategoryEntity> getCategoriesByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            List<RestaurantCategoryEntity> restaurantCategoryEntity = entityManager.createNamedQuery("getCategoriesByRestaurant", RestaurantCategoryEntity.class).setParameter("restaurant", restaurantEntity).getResultList();
            return restaurantCategoryEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
