package com.ldh.exam2.jpaBoard.user.controller;

import com.ldh.exam2.jpaBoard.user.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
}
