# Ya Mohaideen Briyani — Table Reservation System

A full-stack table reservation web app: HTML/CSS/JS frontend, Java Servlets
backend, Oracle Database via JDBC. Built to match a classic Eclipse
**Dynamic Web Project** layout (the same style used for JDBC/Servlet
internship projects).

## Features

- Landing page with hotel info (Ya Mohaideen Briyani, Pammal), location,
  contact number, and food-themed CSS animations (steaming rice, floating
  spice dots, animated flame logo)
- Customer login &amp; registration (stored in Oracle `users` table)
- Table reservation page: pick a date, a time slot, then click a table on
  a live floor-plan grid (color-coded available / booked / selected).
  Availability is fetched live via AJAX from the database.
- Separate **Menu &amp; Offers** page listing recently introduced dishes and
  ongoing discounts, with category filters
- All reservations stored in Oracle with a unique constraint that blocks
  double-booking the same table/date/time

## Project Structure

```
RestaurantReservationSystem/
├── src/com/ymb/
│   ├── db/DBConnection.java        JDBC connection helper
│   ├── model/                      User, RestaurantTable, Reservation
│   ├── dao/                        UserDAO, TableDAO, ReservationDAO
│   └── servlet/                    Login, Register, Logout,
│                                    TableAvailability, Reservation servlets
├── WebContent/
│   ├── index.html                  Home / About / Location / Contact
│   ├── login.html
│   ├── register.html
│   ├── reservation.html            Date/time + table picker + booking form
│   ├── menu.html                   Dishes + offers
│   ├── css/style.css
│   ├── js/{main,auth,reservation}.js
│   └── WEB-INF/web.xml
└── sql/
    ├── schema.sql                  Run first
    └── sample_data.sql             Run second (sample tables/dishes/offers)
```

## Setup

### 1. Database

1. Make sure Oracle Database (XE or full) is running.
2. Open SQL*Plus / SQL Developer connected to your schema.
3. Run `sql/schema.sql`, then `sql/sample_data.sql`.

### 2. Configure the JDBC connection

Edit `src/com/ymb/db/DBConnection.java` and set your own values:

```java
private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
private static final String DB_USER = "system";
private static final String DB_PASSWORD = "password";
```

### 3. Import into Eclipse

1. `File → Import → Dynamic Web Project` (or `File → New → Dynamic Web
   Project`, name it `RestaurantReservationSystem`, then copy these
   `src` / `WebContent` folders in over the generated ones).
2. Download the Oracle JDBC driver (`ojdbc8.jar`, matching your Oracle
   version) and place it in `WebContent/WEB-INF/lib`. Right-click the
   project → **Build Path → Add to Build Path** if it isn't picked up
   automatically.
3. Add a Tomcat (8 or 9) server in Eclipse if you don't have one:
   `Window → Preferences → Server → Runtime Environments → Add`.

> **Using Tomcat 10+?** Its servlet API moved from `javax.servlet.*` to
> `jakarta.servlet.*`. If so, find/replace `javax.servlet` with
> `jakarta.servlet` in the servlet classes.

### 4. Run

Right-click the project → `Run As → Run on Server`. It should open
`index.html`. Register an account, log in, then go to **Reserve a
Table**.
