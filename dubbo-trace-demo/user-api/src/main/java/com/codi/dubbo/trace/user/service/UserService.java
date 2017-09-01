package com.codi.dubbo.trace.user.service;

import com.codi.dubbo.trace.user.model.Addr;
import com.codi.dubbo.trace.user.model.User;

import java.util.List;


public interface UserService {

    User findById(Long id);

    List<Addr> myAddrs(Long userId);

    User findBigData();
}
