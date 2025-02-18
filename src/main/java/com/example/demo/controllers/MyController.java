package com.example.demo.controllers;

import com.example.demo.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MyController {

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/")
    public String getFirstPage() {
        return "home.jsp";
    }

    @RequestMapping("/login")
    public String getLogin() {
        return "login.jsp";
    }

    @RequestMapping("/register")
    public String getRegister() {
        return "register.jsp";
    }

    @Transactional
    @PostMapping("/doRegister")
    public String registerUser(@RequestParam String customer_id,
                               @RequestParam String name,
                               @RequestParam String pwd,
                               @RequestParam String con_pwd,
                               Model model) {
        if (!pwd.equals(con_pwd)) {
            model.addAttribute("error", "Passwords do not match!");
            return "register.jsp";
        }

        List<User> existingUser = entityManager
                .createQuery("SELECT u FROM User u WHERE u.customerId = :customerId", User.class)
                .setParameter("customerId", customer_id)
                .getResultList();

        if (!existingUser.isEmpty()) {
            model.addAttribute("error", "User already exists!");
            return "register.jsp";
        }

        User user = new User(customer_id, name, pwd);
        entityManager.persist(user);
        return "redirect:/login";
    }

    @Transactional
    @PostMapping("/doLogin")
    public String loginUser(@RequestParam String user,
                            @RequestParam String pwd,
                            Model model) {
        List<User> userList = entityManager
                .createQuery("SELECT u FROM User u WHERE u.customerId = :customerId AND u.password = :password", User.class)
                .setParameter("customerId", user)
                .setParameter("password", pwd)
                .getResultList();

        if (!userList.isEmpty()) {
            model.addAttribute("uname", userList.get(0).getName());
            return "welcome.jsp";
        }

        model.addAttribute("error", "Invalid credentials!");
        return "login.jsp";
    }

    @RequestMapping("/welcome")
    public String getWelcomePage() {
        return "welcome.jsp";
    }
}
