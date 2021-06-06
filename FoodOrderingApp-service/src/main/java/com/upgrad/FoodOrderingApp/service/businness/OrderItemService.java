package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemService {


    @Autowired
    OrderItemDao orderItemDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderItemEntity> getOrderItemEntityByOrder(OrdersEntity orderEntity) {

        return orderItemDao.getOrderItemsByOrder(orderEntity);

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItemEntity(OrderItemEntity orderItemEntity) {

        return orderItemDao.saveOrderItem(orderItemEntity);

    }


}
