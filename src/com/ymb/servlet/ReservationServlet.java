package com.ymb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ymb.dao.ReservationDAO;
import com.ymb.dao.TableDAO;
import com.ymb.model.Reservation;

/**
 * POST /reserve
 * Requires an active session (user must be logged in). Re-validates
 * table availability server-side before inserting, then saves the
 * booking.
 */
@WebServlet("/reserve")
public class ReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final TableDAO tableDAO = new TableDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\": false, \"message\": \"Please log in before booking a table.\"}");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try {
            int tableId = Integer.parseInt(request.getParameter("tableId"));
            String customerName = request.getParameter("customerName");
            String customerPhone = request.getParameter("customerPhone");
            int guests = Integer.parseInt(request.getParameter("guests"));
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String specialRequest = request.getParameter("specialRequest");

            if (customerName == null || customerName.trim().isEmpty() ||
                customerPhone == null || customerPhone.trim().isEmpty() ||
                date == null || date.trim().isEmpty() ||
                time == null || time.trim().isEmpty()) {

                out.print("{\"success\": false, \"message\": \"Please fill in all required fields.\"}");
                return;
            }

            if (!tableDAO.isTableAvailable(tableId, date, time)) {
                out.print("{\"success\": false, \"message\": \"Sorry, that table was just booked for this slot. Please choose another.\"}");
                return;
            }

            Reservation r = new Reservation();
            r.setUserId(userId);
            r.setTableId(tableId);
            r.setCustomerName(customerName);
            r.setCustomerPhone(customerPhone);
            r.setGuests(guests);
            r.setReservationDate(date);
            r.setReservationTime(time);
            r.setSpecialRequest(specialRequest);

            boolean saved = reservationDAO.createReservation(r);

            if (saved) {
                out.print("{\"success\": true, \"message\": \"Table reserved successfully! We look forward to serving you.\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Could not complete the reservation. Please try again.\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"Invalid input. Please check the form and try again.\"}");
        }
    }
}
