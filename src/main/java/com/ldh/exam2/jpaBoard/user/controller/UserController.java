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

        email = email.trim();
        password = password.trim();
        name = name.trim();

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

        email = email.trim();
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
    @RequestMapping("modify")
    private String showModify(HttpSession session, Model model) {

        // 로그인 확인
        boolean isLogined = false;
        long loginedUserId = 0;

        if (session.getAttribute("loginedUserId") != null) {
            isLogined = true;
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }

        if (isLogined == false) {
            // common/js.html 도입
            model.addAttribute("msg", "로그인 후 이용해주세요.");
            model.addAttribute("historyBack", true);
            return "common/js";
        }

        Optional<User> OptionalUser = userRepository.findById(loginedUserId);
        User user = OptionalUser.get();

        // 수정할 회원정보 보내기
        model.addAttribute("user", user);

        return "menu/user/modify";
    }

    // 회원정보 수정하기
    @RequestMapping("doModify")
    @ResponseBody
    private String doModify(HttpSession session, long id, String email, String name) {

        // 수정할 회원정보 가져오기
        Optional<User> OptionalUser = userRepository.findById(id);
        User user = OptionalUser.get();

        // 로그인 이메일 중복 방지
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            return """
                    <script>
                    alert('입력하신 이메일(%s)은 이미 사용중입니다.');
                    history.back();
                    </script>
                    """.formatted(email);
        }

        // 수정하기
        if (email != null) {
            user.setEmail(email);
        }

        if (name != null) {
            user.setName(name);
        }

        userRepository.save(user);
        return """
                <script>
                alert('%s님의 회원정보가 수정되었습니다.');
                location.replace('/');
                </script>
                """.formatted(user.getName());
    }

    // 비밀번호 변경 페이지 보기
    @RequestMapping("pwModify")
    private String showPwModify(HttpSession session, Model model) {

        // 로그인 확인
        boolean isLogined = false;
        long loginedUserId = 0;

        if (session.getAttribute("loginedUserId") != null) {
            isLogined = true;
            loginedUserId = (long) session.getAttribute("loginedUserId");
        }

        if (isLogined == false) {
            // common/js.html 도입
            model.addAttribute("msg", "로그인 후 이용해주세요.");
            model.addAttribute("historyBack", true);
            return "common/js";
        }

        Optional<User> OptionalUser = userRepository.findById(loginedUserId);
        User user = OptionalUser.get();

        // 수정할 회원정보 보내기
        model.addAttribute("user", user);

        return "menu/user/pwModify";
    }

    // 비밀번호 변경하기
    @RequestMapping("doPwModify")
    @ResponseBody
    private String doPwModify(HttpSession session, long id, String password, String newPassword) {

        // 수정할 회원정보 가져오기
        Optional<User> OptionalUser = userRepository.findById(id);
        User user = OptionalUser.get();

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            return """
                    <script>
                    alert('현재 비밀번호가 잘못되었습니다. 확인해주세요.');
                    history.back();
                    </script>
                    """;
        }

        // 동일 비밀번호 제외
        if (password.equals(newPassword)) {
            return """
                    <script>
                    alert('이전 비밀번호와 동일한 비밀번호입니다.');
                    history.back();
                    </script>
                    """;
        }

        // 비밀번호 변경하기
        user.setPassword(newPassword);
        userRepository.save(user);

        // 비밀번호 변경후 로그아웃
        session.removeAttribute("loginedUserId");

        return """
                <script>
                alert('비밀번호가 변경되었습니다. 다시 로그인해주세요.');
                location.replace('/');
                </script>
                """;
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
