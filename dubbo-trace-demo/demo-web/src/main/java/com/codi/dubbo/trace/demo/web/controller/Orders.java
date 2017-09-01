package com.codi.dubbo.trace.demo.web.controller;

import com.codi.base.domain.BaseResult;
import com.codi.dubbo.trace.demo.web.dto.OrderCreateResp;
import com.codi.dubbo.trace.order.dto.OrderDto;
import com.codi.dubbo.trace.order.service.OrderService;
import com.codi.dubbo.trace.user.model.Addr;
import com.codi.dubbo.trace.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class Orders {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public OrderCreateResp create() {

        OrderDto orderDto = orderService.create(1L, 1000);

        List<Addr> addrs = userService.myAddrs(1L);

        return new OrderCreateResp(orderDto, addrs);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public BaseResult deleteOrder() {
        userService.findBigData();
        return new BaseResult();
    }
}
