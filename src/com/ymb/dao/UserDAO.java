package com.ymb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ymb.db.DBConnection;
import com.ymb.model.User;

public class UserDAO {

    /**
     * Inserts a new user. Returns true on success, false if the email
     * already exists (or another SQL error occurs).
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (full_name, email, phone, password) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("registerUser failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates login credentials. Returns the matching User (with
     * userId populated) if found, otherwise null.
     */
    public User validateLogin(String email, String password) {
        String sql = "SELECT user_id, full_name, email, phone FROM users WHERE email = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    return user;
                }
            }

        } catch (SQLException e) {
            System.err.println("validateLogin failed: " + e.getMessage());
        }

        return null;
    }

    /** Returns true if an account already exists with this email. */
    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("emailExists failed: " + e.getMessage());
            return false;
        }
    }
}
