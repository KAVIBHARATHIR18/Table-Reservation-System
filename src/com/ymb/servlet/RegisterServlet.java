package com.ymb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ymb.dao.UserDAO;
import com.ymb.model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            out.print("{\"success\": false, \"message\": \"All fields are required.\"}");
            return;
        }

        if (userDAO.emailExists(email)) {
            out.print("{\"success\": false, \"message\": \"An account with this email already exists.\"}");
            return;
        }

        User user = new User(fullName, email, phone, password);
        boolean created = userDAO.registerUser(user);

        if (created) {
            out.print("{\"success\": true, \"message\": \"Account created successfully. Please log in.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"Registration failed. Please try again.\"}");
        }
    }
}
