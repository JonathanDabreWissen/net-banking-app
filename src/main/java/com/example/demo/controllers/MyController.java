package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    private final String URL = "jdbc:postgresql://localhost:5432/demodb";
    private final String USER = "postgres";
    private final String PASSWORD = "tiger";

    // Home Page
    @RequestMapping("/")
    public String getFirstPage() {
        return "home.jsp";
    }

    // Login Page
    @RequestMapping("/login")
    public String getLogin() {
        return "login.jsp";
    }

    // Register Page
    @RequestMapping("/register")
    public String getRegister() {
        return "register.jsp";
    }

    // Register User with JDBC
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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Check if user already exists
            String checkQuery = "SELECT * FROM users WHERE customer_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, customer_id);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    model.addAttribute("error", "User already exists!");
                    return "register.jsp";
                }
            }

            // Insert new user
            String insertQuery = "INSERT INTO users (customer_id, name, password) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, customer_id);
                insertStmt.setString(2, name);
                insertStmt.setString(3, pwd);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "register.jsp";
        }

        return "redirect:/login";
    }

    // Login User with JDBC
    @PostMapping("/doLogin")
    public String loginUser(@RequestParam String user,
                            @RequestParam String pwd,
                            Model model) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT name FROM users WHERE customer_id = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user);
                stmt.setString(2, pwd);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    model.addAttribute("uname", rs.getString("name"));
                    return "welcome.jsp";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Database error: " + e.getMessage());
        }

        model.addAttribute("error", "Invalid credentials!");
        return "login.jsp";
    }

    // Welcome Page
    @RequestMapping("/welcome")
    public String getWelcomePage() {
        return "welcome.jsp";
    }
}


/*
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
*/
