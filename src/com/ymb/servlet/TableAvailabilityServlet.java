package com.ymb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ymb.dao.TableDAO;
import com.ymb.model.RestaurantTable;

/**
 * GET /tables?date=YYYY-MM-DD&time=HH:mm
 * Returns a JSON array describing every table and whether it is free
 * for the requested date + time slot. Called via AJAX from
 * reservation.html whenever the customer changes date or time.
 */
@WebServlet("/tables")
public class TableAvailabilityServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final TableDAO tableDAO = new TableDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String date = request.getParameter("date");
        String time = request.getParameter("time");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (date == null || date.trim().isEmpty() || time == null || time.trim().isEmpty()) {
            out.print("[]");
            return;
        }

        List<RestaurantTable> tables = tableDAO.getTablesWithAvailability(date, time);

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tables.size(); i++) {
            RestaurantTable t = tables.get(i);
            if (i > 0) {
                json.append(",");
            }
            json.append("{")
                .append("\"tableId\":").append(t.getTableId()).append(",")
                .append("\"tableNo\":\"").append(JsonUtil.escape(t.getTableNo())).append("\",")
                .append("\"capacity\":").append(t.getCapacity()).append(",")
                .append("\"location\":\"").append(JsonUtil.escape(t.getLocationDesc())).append("\",")
                .append("\"available\":").append(t.isAvailable())
                .append("}");
        }
        json.append("]");

        out.print(json.toString());
    }
}
