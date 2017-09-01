package com.codi.dubbo.trace.demo.web.controller;


import com.codi.dubbo.trace.user.model.User;
import com.codi.dubbo.trace.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api/users")
public class Users {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User query(@PathVariable(value = "id") Long id) {
        return userService.findById(id);
    }
}
