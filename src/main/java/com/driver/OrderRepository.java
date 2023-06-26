package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class OrderRepository {


    Map<String,Order> orderDb = new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerDb= new HashMap<>();

    Map<String,String> orderPartnerDb = new HashMap<>();
    Map<String, List<String>> partnerOrderDb = new HashMap<>();

    public void addOrder(Order order) {
        orderDb.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        deliveryPartnerDb.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderDb.containsKey(orderId) && deliveryPartnerDb.containsKey(partnerId)){
            orderPartnerDb.put(orderId,partnerId);
        }

        List<String> currOrder = new ArrayList<>();
        if(partnerOrderDb.containsKey(partnerId)){
            currOrder = partnerOrderDb.get(partnerId);
        }

        currOrder.add(orderId);
        partnerOrderDb.put(partnerId, currOrder);

        // increase the number of order
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        deliveryPartner.setNumberOfOrders(currOrder.size());
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDb.get(partnerId);
    }


    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerOrderDb.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderDb.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> ans = new ArrayList<>();
        for(String ord : orderDb.keySet()){
            ans.add(ord);
        }
        return ans;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderDb.size()-orderPartnerDb.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        int count = 0;
        List<String> orders = partnerOrderDb.get(partnerId);
        for(String orderId : orders){
            int deliveryTime = orderDb.get(orderId).getDeliveryTime();
            if(time<deliveryTime) count++;
        }
        return count;

    }


    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int maxTime = 0;
        List<String> orders = partnerOrderDb.get(partnerId);
        for(String order : orders){
            int deliveryTime = orderDb.get(order).getDeliveryTime();
            maxTime = Math.max(maxTime,deliveryTime);
        }

        return maxTime;
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartnerDb.remove(partnerId);

        List<String> listOfOrder = partnerOrderDb.get(partnerId);
        partnerOrderDb.remove((partnerId));

        for(String order : listOfOrder){
            listOfOrder.remove(order);
        }

    }

    public void deleteOrderById(String orderId) {
        orderDb.remove(orderId);

        String partnerId = orderPartnerDb.get(orderId);
        orderPartnerDb.remove(orderId);

        partnerOrderDb.get(partnerId).remove(orderId);
        deliveryPartnerDb.get(partnerId).setNumberOfOrders(partnerOrderDb.get(partnerId).size());
    }
}
