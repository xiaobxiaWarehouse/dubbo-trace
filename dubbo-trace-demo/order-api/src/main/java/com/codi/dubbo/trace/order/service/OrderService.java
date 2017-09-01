package com.codi.dubbo.trace.order.service;

import com.codi.dubbo.trace.order.dto.OrderDto;


public interface OrderService {

    OrderDto create(Long userId, Integer amount);
}
