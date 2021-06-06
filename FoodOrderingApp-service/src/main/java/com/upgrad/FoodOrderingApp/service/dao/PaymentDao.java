package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provide the necessary methods to access the PaymentEntity
 */
@Repository
public class PaymentDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get Payment by UUID
     *
     * @param paymentId Payment ID
     * @return PaymentEntity or NULL
     */
    public PaymentEntity getPaymentByUUID(String paymentId) {
        try {
            return entityManager.createNamedQuery("getPaymentByUUID", PaymentEntity.class).
                    setParameter("uuid", paymentId).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get all Payment Methods
     *
     * @return Payment Entities
     */
    public List<PaymentEntity> getAllPaymentMethods() {
        try {
            return entityManager.createNamedQuery("getAllPaymentMethods", PaymentEntity.class).
                    getResultList();

        } catch (NoResultException nre) {
            return new ArrayList<>();
        }
    }
}
