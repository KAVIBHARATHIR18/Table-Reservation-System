package com.ymb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ymb.db.DBConnection;
import com.ymb.model.Reservation;

public class ReservationDAO {

    /**
     * Saves a new reservation. Returns true on success. Relies on the
     * unique index (table_id, reservation_date, reservation_time) in
     * schema.sql to reject a double-booking race condition.
     */
    public boolean createReservation(Reservation r) {
        String sql = "INSERT INTO reservations " +
                "(user_id, table_id, customer_name, customer_phone, guests, " +
                " reservation_date, reservation_time, special_request, status) " +
                "VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, 'CONFIRMED')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getTableId());
            ps.setString(3, r.getCustomerName());
            ps.setString(4, r.getCustomerPhone());
            ps.setInt(5, r.getGuests());
            ps.setString(6, r.getReservationDate());
            ps.setString(7, r.getReservationTime());
            ps.setString(8, r.getSpecialRequest());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Unique constraint violation (ORA-00001) means the slot was
            // just taken by someone else between the availability check
            // and this insert.
            System.err.println("createReservation failed: " + e.getMessage());
            return false;
        }
    }
}
