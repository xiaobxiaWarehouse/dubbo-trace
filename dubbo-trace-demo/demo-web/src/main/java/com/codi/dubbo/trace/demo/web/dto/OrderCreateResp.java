package com.codi.dubbo.trace.demo.web.dto;

import com.codi.dubbo.trace.order.dto.OrderDto;
import com.codi.dubbo.trace.user.model.Addr;

import java.io.Serializable;
import java.util.List;


public class OrderCreateResp implements Serializable {

    private static final long serialVersionUID = -78879006483682489L;

    private OrderDto orderDto;

    private List<Addr> addrs;

    public OrderCreateResp(OrderDto orderDto, List<Addr> addrs) {
        this.orderDto = orderDto;
        this.addrs = addrs;
    }

    public OrderDto getOrderDto() {
        return orderDto;
    }

    public void setOrderDto(OrderDto orderDto) {
        this.orderDto = orderDto;
    }

    public List<Addr> getAddrs() {
        return addrs;
    }

    public void setAddrs(List<Addr> addrs) {
        this.addrs = addrs;
    }

    @Override
    public String toString() {
        return "OrderCreateResp{" +
                "orderDto=" + orderDto +
                ", addrs=" + addrs +
                '}';
    }
}
