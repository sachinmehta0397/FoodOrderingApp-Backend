package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.upgrad.FoodOrderingApp.api.util.Constants.*;

@CrossOrigin
@RestController
@RequestMapping("/")
public class OrderController {


    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AddressService addressService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    ItemService itemService;
    @Autowired
    RestaurantService restaurantService;

    /**
     * Returns the details of a Coupon based on the coupon name.
     *
     * @param accessToken Auth info of the customer.
     * @param couponName  The name of the Coupon which is requested.
     * @return The details of requested coupon..
     * @throws AuthorizationFailedException if the Access token provided is invalid or expired.
     * @throws CouponNotFoundException      if the coupon name is invalid/empty.
     */


    @CrossOrigin
    @GetMapping(value = "/order/coupon/{couponName}",
            produces = {"application/json"})
    public ResponseEntity<CouponDetailsResponse> getCoupon
    (@RequestHeader(value = "authorization") String accessToken,
     @PathVariable("couponName") String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        final String bearerToken = accessToken.split(BEARER_AUTH)[1];
        customerService.getCustomer(bearerToken);
        final CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        final CouponDetailsResponse response = new CouponDetailsResponse();
        response.couponName(couponEntity.getCouponName()).
                id(UUID.fromString(couponEntity.getUuid())).
                percent(couponEntity.getPercent());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @CrossOrigin
    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder
            (@RequestHeader(value = "authorization") String accessToken,
             @RequestBody SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {

        final String bearerToken = accessToken.split(BEARER_AUTH)[1];
        final CustomerEntity customer = customerService.getCustomer(bearerToken);
        final PaymentEntity payment = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
        final AddressEntity address = addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customer);
        final RestaurantEntity restaurant = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());
        final CouponEntity coupon = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());

        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setUuid(UUID.randomUUID().toString());
        ordersEntity.setAddress(address);
        ordersEntity.setBill(saveOrderRequest.getBill().floatValue());
        ordersEntity.setDate(LocalDateTime.now());
        ordersEntity.setCoupon(coupon);
        ordersEntity.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        ordersEntity.setCustomer(customer);
        ordersEntity.setPayment(payment);
        ordersEntity.setRestaurant(restaurant);

        ordersEntity = orderService.saveOrder(ordersEntity);
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        for (ItemQuantity itemQuantity : saveOrderRequest.getItemQuantities()) {
            final ItemEntity itemEntity = itemService.getItemByUUID(itemQuantity.getItemId().toString());
            final OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setOrder(ordersEntity);
            orderItemEntity.setItem(itemEntity);
            orderItemEntity.setPrice(itemQuantity.getPrice());
            orderItemEntity.setQuantity(itemQuantity.getQuantity());
            orderItemEntities.add(orderItemEntity);
        }
        for (OrderItemEntity orderItemEntity : orderItemEntities)
            orderService.saveOrderItem(orderItemEntity);
        final SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
        saveOrderResponse.id(ordersEntity.getUuid());
        saveOrderResponse.setStatus("ORDER SUCCESSFULLY PLACED");

        return new ResponseEntity<>(saveOrderResponse, HttpStatus.CREATED);
    }


    /**
     * This method return all the orders associated to a particular customer
     *
     * @param accessToken Bearer Access token auth info of the customer
     * @return List of all the orders of the customer.
     * @throws AuthorizationFailedException if the Access token provided is invalid or expired.
     */


    @CrossOrigin
    @GetMapping(value = "/order",
            produces = {"application/json"})
    public ResponseEntity<CustomerOrderResponse> getAllOrders
    (@RequestHeader(value = "authorization") String accessToken) throws AuthorizationFailedException {

        final String bearerToken = accessToken.split(BEARER_AUTH)[1];


        CustomerEntity customerEntity = customerService.getCustomer(bearerToken);

        List<OrdersEntity> ordersList = orderService.getOrdersByCustomer(customerEntity);

        final CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
        customerOrderResponse.orders(new ArrayList<>());

        if (ordersList.isEmpty()) return new ResponseEntity<>(customerOrderResponse, HttpStatus.OK);

        customerOrderResponse.orders(prepareOrderList(ordersList));
        return new ResponseEntity<>(customerOrderResponse, HttpStatus.OK);

    }

    public List<OrderList> prepareOrderList(List<OrdersEntity> orderEntityList) {
        final List<OrderList> orderList = new ArrayList<>();
        for (OrdersEntity o : orderEntityList) {
            final OrderList order = new OrderList();
            order.id(UUID.fromString(o.getUuid())).
                    bill(BigDecimal.valueOf(o.getBill())).
                    discount(BigDecimal.valueOf(o.getDiscount())).
                    date(String.valueOf(o.getLocalDateTime()));

            final OrderListCoupon orderListCoupon = new OrderListCoupon();
            orderListCoupon.id(UUID.fromString(o.getCoupon().getUuid())).
                    couponName(o.getCoupon().getCouponName()).
                    percent(o.getCoupon().getPercent());
            order.setCoupon(orderListCoupon);

            final OrderListPayment payment = new OrderListPayment();
            payment.paymentName(o.getPayment().getPaymentName()).
                    id(UUID.fromString(o.getPayment().getUuid()));
            order.setPayment(payment);

            final OrderListCustomer customer = new OrderListCustomer();
            customer.id(UUID.fromString(o.getCustomer().getUuid())).
                    firstName(o.getCustomer().getFirstName()).
                    lastName(o.getCustomer().getLastName()).
                    contactNumber(o.getCustomer().getContactNumber()).
                    emailAddress(o.getCustomer().getEmail());
            order.setCustomer(customer);

            final OrderListAddress orderListAddress = new OrderListAddress();
            orderListAddress.id(UUID.fromString(o.getAddress().getUuid())).
                    city(o.getAddress().getCity()).
                    flatBuildingName(o.getAddress().getFlatBuilNo()).
                    locality(o.getAddress().getLocality()).
                    pincode(o.getAddress().getPincode());

            final OrderListAddressState state = new OrderListAddressState();
            state.id(UUID.fromString(o.getAddress().getState().getStateUuid())).
                    stateName(o.getAddress().getState().getStateName());

            orderListAddress.setState(state);

            order.setAddress(orderListAddress);

            final List<ItemQuantityResponse> itemQuantityResponseList = new ArrayList<>();

            final List<OrderItemEntity> orderItemEntityByOrder = orderItemService.getOrderItemEntityByOrder(o);

            for (OrderItemEntity oie : orderItemEntityByOrder) {
                final ItemQuantityResponse quantityResponse = new ItemQuantityResponse();
                quantityResponse.quantity(oie.getQuantity()).price(oie.getPrice());

                final ItemQuantityResponseItem item = new ItemQuantityResponseItem();
                item.id(UUID.fromString(oie.getItem().getUuid())).
                        itemName(oie.getItem().getitemName()).
                        itemPrice(oie.getItem().getPrice());
                quantityResponse.item(item);
                itemQuantityResponseList.add(quantityResponse);

            }

            order.setItemQuantities(itemQuantityResponseList);
            orderList.add(order);

        }
        return orderList;

    }


}
