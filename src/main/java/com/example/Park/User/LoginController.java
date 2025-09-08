package com.example.Park.User;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    private user_master_repo userRepo;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    @PostMapping("/login1")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        User_master user = userRepo.findByUserName(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("userId", user.getUser_id());
            session.setAttribute("username", user.getUser_name());
            return "register_visitor";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "logout";
//    }
}
