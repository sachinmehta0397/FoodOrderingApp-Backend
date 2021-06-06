package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {

    @Autowired
    CouponDao couponDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {

        if (StringUtils.isEmpty(couponName))
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");

        final CouponEntity couponEntity = couponDao.getCouponByCouponName(couponName);
        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }

        return couponEntity;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponId(String couponId) throws CouponNotFoundException {

        final CouponEntity couponEntity = couponDao.getCouponByCouponId(couponId);
        if (couponEntity == null) throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        return couponEntity;

    }



}
