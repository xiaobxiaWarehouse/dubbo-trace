package com.codi.dubbo.trace.order.dto;

import com.codi.dubbo.trace.order.model.Order;
import com.codi.dubbo.trace.user.model.User;

import java.io.Serializable;


public class OrderDto implements Serializable {

    private static final long serialVersionUID = 8142107812921077485L;

    private User user;

    private Order order;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "user=" + user +
                ", order=" + order +
                '}';
    }
}
