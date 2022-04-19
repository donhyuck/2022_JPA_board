package com.ldh.exam2.jpaBoard.user.controller;

import com.ldh.exam2.jpaBoard.article.domain.Article;
import com.ldh.exam2.jpaBoard.user.dao.UserRepository;
import com.ldh.exam2.jpaBoard.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // 회원 가입 페이지 보기
    @RequestMapping("join")
    private String showJoin(HttpSession session, Model model) {

        // 로그인 확인
        boolean isLogined = false;
        long loginedUserId = 0;

        if (session.getAttribute("loginedUserId") != null) {
            isLogined = true;
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }

        // 로그인 중 가입 방지
        if (isLogined == true) {
            // common/js.html 도입
            model.addAttribute("msg", "로그아웃 후 이용해주세요.");
            model.addAttribute("historyBack", true);
            return "common/js";
        }

        return "menu/user/join";
    }

    // 회원가입하기
    @RequestMapping("doJoin")
    @ResponseBody
    public String doJoin(String email, String password, String name) {

        if (email == null || email.trim().length() == 0) {
            return """
                    <script>
                    alert('이메일을 입력해주세요.');
                    history.back();
                    </script>
                    """;
        }
        email = email.trim();

        // 회원가입시 이메일 중복체크
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            return """
                    <script>
                    alert('입력하신 이메일(%s)은 이미 사용중입니다.');
                    history.back();
                    </script>
                    """.formatted(email);
        }

        if (password == null || password.trim().length() == 0) {
            return """
                    <script>
                    alert('비밀번호를 입력해주세요.');
                    history.back();
                    </script>
                    """;
        }
        password = password.trim();

        if (name == null || name.trim().length() == 0) {
            return """
                    <script>
                    alert('이름을 입력해주세요.');
                    history.back();
                    </script>
                    """;
        }
        name = name.trim();

        User user = new User();
        user.setRegDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        userRepository.save(user);
        return """
                <script>
                alert('%s님 %d번 회원으로 가입되었습니다.');
                location.replace("/menu/article/showList");
                </script>
                """.formatted(user.getName(), user.getId());

    }

    // 회원 로그인 페이지 보기
    @RequestMapping("login")
    private String showLogin(HttpSession session, Model model) {

        // 로그인 확인
        boolean isLogined = false;
        long loginedUserId = 0;

        if (session.getAttribute("loginedUserId") != null) {
            isLogined = true;
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }

        // 중복 로그인 방지
        if (isLogined == true) {
            // common/js.html 도입
            model.addAttribute("msg", "이미 로그인되었습니다.");
            model.addAttribute("historyBack", true);
            return "common/js";
        }

        return "menu/user/login";
    }

    // 회원 로그인 하기
    @RequestMapping("doLogin")
    @ResponseBody
    public String doLogin(String email, String password, HttpServletRequest req, HttpServletResponse resp) {

        if (email == null || email.trim().length() == 0) {
            return """
                    <script>
                    alert('이메일을 입력해주세요.');
                    history.back();
                    </script>
                    """;
        }
        email = email.trim();

        if (password == null || password.trim().length() == 0) {
            return """
                    <script>
                    alert('비밀번호를 입력해주세요.');
                    history.back();
                    </script>
                    """;
        }
        password = password.trim();

        // 회원등록여부 확인
        // User user = userRepository.findByEmail(email).get(); null값 오류

        // 해결
        // User user = userRepository.findByEmail(email).orElse(null); // 방법1
        Optional<User> user = userRepository.findByEmail(email); // 방법2

        if (user.isEmpty()) {
            return """
                    <script>
                    alert('입력하신 이메일(%s)을 잘못 입력하시거나 등록되지 않은 회원입니다.');
                    history.back();
                    </script>
                    """.formatted(email);
        }

        if (user.get().getPassword().equals(password) == false) {
            return """
                    <script>
                    alert('비밀번호가 틀렸습니다.');
                    location.replace("/menu/article/showList");
                    </script>
                    """;
        }

        // 로그인시 쿠키정보설정
        // Cookie cookie = new Cookie("loginedUserId", user.get().getId() + "");
        // resp.addCookie(cookie);

        // 로그인시 세션설정
        HttpSession session = req.getSession();
        session.setAttribute("loginedUserId", user.get().getId());

        return """
                <script>
                alert('%s님 환영합니다.');
                location.replace("/");
                </script>
                """.formatted(user.get().getName());
    }

    // 로그인 후 내 정보 보기
    @RequestMapping("me")
    public String showMe(HttpSession session, Model model) {

        boolean isLogined = false;
        long loginedUserId = 0;

        // 세션정보 가져오기
        if (session.getAttribute("loginedUserId") != null) {
            isLogined = true;
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }

        // 로그인 유저가 없는 경우
        if (isLogined == false) {
            model.addAttribute("msg", "로그인 후 이용해주세요.");
            model.addAttribute("replaceUri", "login");
            return "common/js";
        }

        Optional<User> OptionalUser = userRepository.findById(loginedUserId);

        if (OptionalUser.isEmpty()) {
            model.addAttribute("msg", "유저 정보를 찾을 수 없습니다.");
            model.addAttribute("historyBack", true);
            return "common/js";
        }

        User user = OptionalUser.get();

        model.addAttribute("user", user);

        return "menu/user/me";
    }

    // 회원정보 수정 페이지 보기
    @RequestMapping("infoModify")
    private String showModify(HttpSession session, Model model) {

        long loginedUserId = 0;

        // 세션정보 가져오기
        if (session.getAttribute("loginedUserId") != null) {
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }

        Optional<User> OptionalUser = userRepository.findById(loginedUserId);
        User user = OptionalUser.get();

        // 수정할 게시글 보내기
        model.addAttribute("user", user);

        return "menu/user/infoModify";
    }

    // 로그아웃 하기
    @RequestMapping("doLogout")
    @ResponseBody
    public String doLogout(HttpSession session) {

        if (session.getAttribute("loginedUserId") != null) {
            session.removeAttribute("loginedUserId");
            return """
                    <script>
                    alert('로그아웃 되었습니다.');
                    location.replace("/");
                    </script>
                    """;
        }

        return """
                <script>
                alert('로그인 중인 회원이 없습니다.');
                history.back();
                </script>
                """;
    }
}
