package com.ldh.exam2.jpaBoard.user.controller;

import com.ldh.exam2.jpaBoard.user.dao.UserRepository;
import com.ldh.exam2.jpaBoard.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/menu/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // 회원가입하기
    @RequestMapping("doJoin")
    @ResponseBody
    public String doJoin(String email, String password, String name) {

        if (email == null || email.trim().length() == 0) {
            return "이메일을 입력해주세요.";
        }
        email = email.trim();

        // 회원가입시 이메일 중복체크
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            return "입력하신 이메일(%s)은 이미 사용중입니다.".formatted(email);
        }

        if (password == null || password.trim().length() == 0) {
            return "비밀번호를 입력해주세요.";
        }
        password = password.trim();

        if (name == null || name.trim().length() == 0) {
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

    // 회원 로그인 하기
    @RequestMapping("doLogin")
    @ResponseBody
    public String doLogin(String email, String password, HttpServletRequest req, HttpServletResponse resp) {

        if (email == null || email.trim().length() == 0) {
            return "이메일을 입력해주세요.";
        }
        email = email.trim();

        if (password == null || password.trim().length() == 0) {
            return "비밀번호를 입력해주세요.";
        }
        password = password.trim();

        // 회원등록여부 확인
        // User user = userRepository.findByEmail(email).get(); null값 오류

        // 해결
        // User user = userRepository.findByEmail(email).orElse(null); // 방법1
        Optional<User> user = userRepository.findByEmail(email); // 방법2

        if (user.isEmpty()) {
            return "입력하신 이메일(%s)을 잘못 입력하시거나 등록되지 않은 회원입니다.".formatted(email);
        }

        System.out.println("user.getPassword() : " + user.get().getPassword());
        System.out.println("password : " + password);

        if (user.get().getPassword().equals(password) == false) {
            return "비밀번호가 틀렸습니다.";
        }

        // 로그인시 쿠키정보설정
        // Cookie cookie = new Cookie("loginedUserId", user.get().getId() + "");
        // resp.addCookie(cookie);

        // 로그인시 세션설정
        HttpSession session = req.getSession();
        session.setAttribute("loginedUserId", user.get().getId());

        return "%s님 환영합니다.".formatted(user.get().getName());
    }

    // 로그인 후 내 정보 보기
    @RequestMapping("me")
    @ResponseBody
    public User showMe(HttpSession session) {

        boolean isLogined = false;
        long loginedUserId = 0;

        // 세션정보 가져오기
        if (session.getAttribute("loginedUserId") != null) {
            isLogined = true;
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }

        // 로그인 유저가 없는 경우
        if (isLogined == false) {
            return null;
        }

        Optional<User> user = userRepository.findById(loginedUserId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    // 로그아웃 하기
    @RequestMapping("doLogout")
    @ResponseBody
    public String doLogout(HttpSession session) {

        if (session.getAttribute("loginedUserId") != null) {
            session.removeAttribute("loginedUserId");
            return "로그아웃 되었습니다.";
        }

        return "로그인 중인 회원이 없습니다.";
    }
}
