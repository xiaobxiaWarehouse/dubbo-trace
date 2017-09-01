package com.codi.dubbo.trace.user.model;

import java.io.Serializable;


public class Addr implements Serializable {

    private static final long serialVersionUID = -5917242291472606520L;

    private Long id;

    private Long userId;

    private String name;

    private String mobile;

    public Addr() {
    }

    public Addr(Long id, Long userId, String name, String mobile) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Addr{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
