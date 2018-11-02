package com.example.admin.controller.api.impl;

import com.example.admin.controller.api.AdminApi;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminApiImpl implements AdminApi {
    @Autowired
    UserService userService;
    @Override
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public Long getId() {
        Long id = userService.get();
        return id;
    }
}
