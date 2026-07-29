package com.ymb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ymb.db.DBConnection;
import com.ymb.model.RestaurantTable;

public class TableDAO {

    /**
     * Returns every table in the restaurant, each flagged as
     * available or not for the given date + time slot.
     */
    public List<RestaurantTable> getTablesWithAvailability(String date, String time) {
        List<RestaurantTable> tables = new ArrayList<>();
        Set<Integer> bookedTableIds = getBookedTableIds(date, time);

        String sql = "SELECT table_id, table_no, capacity, location_desc FROM restaurant_tables ORDER BY table_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RestaurantTable t = new RestaurantTable();
                t.setTableId(rs.getInt("table_id"));
                t.setTableNo(rs.getString("table_no"));
                t.setCapacity(rs.getInt("capacity"));
                t.setLocationDesc(rs.getString("location_desc"));
                t.setAvailable(!bookedTableIds.contains(t.getTableId()));
                tables.add(t);
            }

        } catch (SQLException e) {
            System.err.println("getTablesWithAvailability failed: " + e.getMessage());
        }

        return tables;
    }

    private Set<Integer> getBookedTableIds(String date, String time) {
        Set<Integer> booked = new HashSet<>();

        String sql = "SELECT table_id FROM reservations " +
                     "WHERE reservation_date = TO_DATE(?, 'YYYY-MM-DD') " +
                     "AND reservation_time = ? AND status != 'CANCELLED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, date);
            ps.setString(2, time);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    booked.add(rs.getInt("table_id"));
                }
            }

        } catch (SQLException e) {
            System.err.println("getBookedTableIds failed: " + e.getMessage());
        }

        return booked;
    }

    /** Quick server-side re-check right before saving a reservation. */
    public boolean isTableAvailable(int tableId, String date, String time) {
        String sql = "SELECT 1 FROM reservations " +
                     "WHERE table_id = ? AND reservation_date = TO_DATE(?, 'YYYY-MM-DD') " +
                     "AND reservation_time = ? AND status != 'CANCELLED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tableId);
            ps.setString(2, date);
            ps.setString(3, time);

            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }

        } catch (SQLException e) {
            System.err.println("isTableAvailable failed: " + e.getMessage());
            return false;
        }
    }
}
