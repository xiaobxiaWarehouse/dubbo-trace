package com.codi.dubbo.trace.order.service;


import com.codi.dubbo.trace.order.dto.OrderDto;
import com.codi.dubbo.trace.order.model.Order;
import com.codi.dubbo.trace.user.model.User;
import com.codi.dubbo.trace.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    public OrderDto create(Long userId, Integer amount) {

        // invoke other rpc service
        User user = userService.findById(userId);

        Order order = new Order();
        order.setId(2L);
        order.setAmount(amount);
        order.setBuyerId(user.getId());
        order.setOrderName("测试订单");
        order.setOrderNo("123456");

        OrderDto orderDto = new OrderDto();
        orderDto.setUser(user);
        orderDto.setOrder(order);

        return orderDto;
    }
}
