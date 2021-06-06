package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUUID, String categoryUUID) {
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUUID);
        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryUUID);
        List<ItemEntity> restaurantItemEntityList = new ArrayList<>();

        for (ItemEntity restaurantItemEntity : restaurantEntity.getItems()) {
            for (ItemEntity categoryItemEntity : categoryEntity.getItems()) {
                if (restaurantItemEntity.getUuid().equals(categoryItemEntity.getUuid())) {
                    restaurantItemEntityList.add(restaurantItemEntity);
                }
            }
        }
        restaurantItemEntityList.sort(Comparator.comparing(ItemEntity::getitemName));

        return restaurantItemEntityList;
    }

    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        for (OrdersEntity orderEntity : orderDao.getOrdersByRestaurant(restaurantEntity)) {
            for (OrderItemEntity orderItemEntity : orderItemDao.getItemsByOrders(orderEntity)) {
                itemEntityList.add(orderItemEntity.getItem());
            }
        }


        //Get all items ordered from the particular restaurant
        Map<String, Integer> map = new HashMap<>();
        for (ItemEntity itemEntity : itemEntityList) {
            Integer count = map.get(itemEntity.getUuid());
            map.put(itemEntity.getUuid(), (count == null) ? 1 : count + 1);
        }

        //Store the entire map data in treemap
        Map<String, Integer> treeMap = new TreeMap<>(map);
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(treeMap.entrySet());

        //Sort the map entries based on number of orders - ascending
        //Store in a list
        list.sort(Map.Entry.comparingByValue());

        //Transfer list data to  List<ItemEntity>
        List<ItemEntity> sortedItemEntityList = new ArrayList<>();
        for (Map.Entry<String, Integer> stringIntegerEntry : list) {
            sortedItemEntityList.add(itemDao.getItemByUUID(stringIntegerEntry.getKey()));
        }

        // Reverse the List<ItemEntity> to sort by number of orders - descending order
        Collections.reverse(sortedItemEntityList);
        return sortedItemEntityList;
    }


    public ItemEntity getItemByUUID(String uuid) throws ItemNotFoundException {
        final ItemEntity itemEntity = itemDao.getItemByUUID(uuid);
        if (itemEntity == null)
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        return itemEntity;

    }
}
