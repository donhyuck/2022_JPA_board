package com.ldh.exam2.jpaBoard.user.controller;

import com.ldh.exam2.jpaBoard.user.dao.UserRepository;
import com.ldh.exam2.jpaBoard.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/menu/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("doJoin")
    @ResponseBody
    public String doJoin(String email, String password, String name) {

        if (email == null || email.trim().length() == 0 ) {
            return "이메일을 입력해주세요.";
        }
        email = email.trim();

        // 회원가입시 이메일 중복체크
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            return "입력하신 이메일(%s)은 이미 사용중입니다.".formatted(email);
        }

        if (password == null || password.trim().length() == 0 ) {
            return "비밀번호를 입력해주세요.";
        }
        password = password.trim();

        if (name == null || name.trim().length() == 0 ) {
            return "이름을 입력해주세요.";
        }
        name = name.trim();

        User user = new User();
        user.setRegDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        userRepository.save(user);
        return "%s님 %d번 회원으로 가입되었습니다.".formatted(user.getName(), user.getId());
    }
}
