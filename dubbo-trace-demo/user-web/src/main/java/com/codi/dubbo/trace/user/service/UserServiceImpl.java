package com.codi.dubbo.trace.user.service;

import com.codi.dubbo.trace.user.model.Addr;
import com.codi.dubbo.trace.user.model.User;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public User findById(Long id) {

        User user = new User();
        user.setId(id);
        user.setPasswd("");
        user.setUsername("xiaoming");

        return user;
    }

    public List<Addr> myAddrs(Long userId) {
        return Lists.newArrayList(
                new Addr(1L, userId, "Addr" + userId, "Mobile" + userId),
                new Addr(2L, userId, "Addr" + userId, "Mobile" + userId)
        );
    }

    @Override
    public User findBigData() {

        int a = (int) (100 * Math.random());

        logger.info("a={}", a);
        if (a % 2 == 0) {
            logger.error(" error Bingo");
            throw new RuntimeException("mysss");
        }


        return null;
    }
}
