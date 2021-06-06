package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * This class provide the necessary methods to access the ItemEntity
 */
@Repository
public class CouponDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get Coupon by Coupon ID
     *
     * @param couponUuid Coupon UUID
     * @return CouponEntity or NULL
     */
    public CouponEntity getCouponByCouponId(String couponUuid) {
        try {
            CouponEntity couponEntity = entityManager.createNamedQuery("getCouponByCouponId", CouponEntity.class).setParameter("uuid", couponUuid).getSingleResult();
            return couponEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get Coupon by Coupon Name
     *
     * @param couponName Coupon Name
     * @return CouponEntity or NULL
     */
    public CouponEntity getCouponByCouponName(String couponName) {
        try {
            CouponEntity couponEntity = entityManager.createNamedQuery("getCouponByCouponName", CouponEntity.class).setParameter("coupon_name", couponName).getSingleResult();
            return couponEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
