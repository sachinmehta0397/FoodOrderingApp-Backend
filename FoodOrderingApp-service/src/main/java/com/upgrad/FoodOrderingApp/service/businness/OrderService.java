package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    CouponService couponService;

    @Autowired
    OrderItemService orderItemService;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrdersEntity> getOrdersByCustomer(CustomerEntity customerEntity) {
        return orderDao.getOrdersByCustomers(customerEntity);
    }

    public CouponEntity getCouponByCouponId(String couponId) throws CouponNotFoundException {

        return couponService.getCouponByCouponId(couponId);

    }

    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {

        return couponService.getCouponByCouponName(couponName);

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public OrdersEntity saveOrder(OrdersEntity orderEntity) {
        return orderDao.saveOrder(orderEntity);
    }


    public OrderItemEntity saveOrderItem(OrderItemEntity orderItem) {
        return orderItemService.saveOrderItemEntity(orderItem);
    }


}
