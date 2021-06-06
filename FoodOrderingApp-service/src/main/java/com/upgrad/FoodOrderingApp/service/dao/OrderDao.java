package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provide the necessary methods to access the OrderEntity
 */
@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save the Order
     *
     * @param ordersEntity Orders Entity to be Saved
     * @return Saved Order Entity
     */
    public OrdersEntity saveOrder(OrdersEntity ordersEntity) {
        entityManager.persist(ordersEntity);
        return ordersEntity;
    }

    /**
     * Get list of Orders placed by Customer
     *
     * @param customerEntity Customer Entity
     * @return OrdersEntities or NULL
     */
    public List<OrdersEntity> getOrdersByCustomers(CustomerEntity customerEntity) {
        try {
            return entityManager.createNamedQuery
                    ("getOrdersByCustomers", OrdersEntity.class)
                    .setParameter("customer", customerEntity).
                            getResultList();

        } catch (NoResultException nre) {
            return new ArrayList<>();
        }
    }

    /**
     * Get list of Orders by Restaurant
     *
     * @param restaurantEntity RestaurantEntity
     * @return OrdersEntities or NULL
     */
    public List<OrdersEntity> getOrdersByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            return entityManager.createNamedQuery("getOrdersByRestaurant", OrdersEntity.class).setParameter("restaurant", restaurantEntity).
                    getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<>();
        }
    }

    /**
     * get list of Orders corresponding to an Address
     *
     * @param addressEntity AddressEntity
     * @return OrdersEntities or NULL
     */
    public List<OrdersEntity> getOrdersByAddress(AddressEntity addressEntity) {
        try {
            return entityManager.createNamedQuery("getOrdersByAddress", OrdersEntity.class).setParameter("address", addressEntity)
                    .getResultList();

        } catch (NoResultException nre) {
            return new ArrayList<>();
        }
    }
}
