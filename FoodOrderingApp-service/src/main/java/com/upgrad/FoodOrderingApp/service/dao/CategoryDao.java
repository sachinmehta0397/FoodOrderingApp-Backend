package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This class provide the necessary methods to access the CategoryEntity
 */
@Repository
public class CategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get Category by the UUID
     *
     * @param uuid UUID
     * @return CategoryEntity or NULL
     */
    public CategoryEntity getCategoryByUuid(String uuid) {
        try {
            CategoryEntity categoryEntity = entityManager.createNamedQuery("getCategoryByUuid", CategoryEntity.class).setParameter("uuid", uuid).getSingleResult();
            return categoryEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get list Categories
     *
     * @return CategoryEntities or NULL
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        try {
            List<CategoryEntity> categoryEntities = entityManager.createNamedQuery("getAllCategoriesOrderedByName", CategoryEntity.class).getResultList();
            return categoryEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
