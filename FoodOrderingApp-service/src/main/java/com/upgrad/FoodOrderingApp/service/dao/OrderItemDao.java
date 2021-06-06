package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * This class provide the necessary methods to access the PaymentEntity
 */
@Repository
public class OrderItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save Order Item
     *
     * @param orderItemEntity OrderItemEntity to be Saved
     * @return Saved OrderItemEntity
     */
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }

    /**
     * Get Items by Orders
     *
     * @param ordersEntity Orders Entity
     * @return OrderItemEntities or Null
     */
    public List<OrderItemEntity> getItemsByOrders(OrdersEntity ordersEntity) {
        try {
            List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery("getItemsByOrders", OrderItemEntity.class).setParameter("ordersEntity", ordersEntity).getResultList();
            return orderItemEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get Order Items by Orders
     *
     * @param ordersEntity Orders Entity
     * @return OrderItemEntities or NULL
     */
    public List<OrderItemEntity> getOrderItemsByOrder(OrdersEntity ordersEntity) {
        try {
            List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery("getOrderItemsByOrder", OrderItemEntity.class).setParameter("orders", ordersEntity).getResultList();
            return orderItemEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
