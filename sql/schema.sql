--------------------------------------------------------------------
-- Ya Mohaideen Briyani - Table Reservation System
-- Oracle Database Schema
-- Run this script in SQL*Plus / SQL Developer connected to your
-- Oracle user/schema before starting the application.
--------------------------------------------------------------------

-- Drop existing objects (safe re-run)
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE reservations CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE offers CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE dishes CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE restaurant_tables CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE users CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

--------------------------------------------------------------------
-- USERS  (customer login / registration)
--------------------------------------------------------------------
CREATE TABLE users (
    user_id      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name    VARCHAR2(100)  NOT NULL,
    email        VARCHAR2(100)  NOT NULL UNIQUE,
    phone        VARCHAR2(15)   NOT NULL,
    password     VARCHAR2(200)  NOT NULL,
    created_at   TIMESTAMP DEFAULT SYSTIMESTAMP
);

--------------------------------------------------------------------
-- RESTAURANT_TABLES  (physical tables in the restaurant)
--------------------------------------------------------------------
CREATE TABLE restaurant_tables (
    table_id     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_no     VARCHAR2(10)   NOT NULL,
    capacity     NUMBER(2)      NOT NULL,
    location_desc VARCHAR2(50)
);

--------------------------------------------------------------------
-- DISHES  (menu items / recently introduced dishes)
--------------------------------------------------------------------
CREATE TABLE dishes (
    dish_id      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dish_name    VARCHAR2(100)  NOT NULL,
    description  VARCHAR2(300),
    price        NUMBER(8,2)    NOT NULL,
    category     VARCHAR2(50),
    is_new       CHAR(1) DEFAULT 'N' CHECK (is_new IN ('Y','N')),
    created_at   TIMESTAMP DEFAULT SYSTIMESTAMP
);

--------------------------------------------------------------------
-- OFFERS  (ongoing discounts on dishes)
--------------------------------------------------------------------
CREATE TABLE offers (
    offer_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dish_id         NUMBER NOT NULL,
    discount_percent NUMBER(4,1) NOT NULL,
    offer_title     VARCHAR2(150),
    valid_until     DATE,
    CONSTRAINT fk_offer_dish FOREIGN KEY (dish_id) REFERENCES dishes(dish_id)
);

--------------------------------------------------------------------
-- RESERVATIONS  (customer table bookings)
--------------------------------------------------------------------
CREATE TABLE reservations (
    reservation_id   NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id          NUMBER NOT NULL,
    table_id         NUMBER NOT NULL,
    customer_name    VARCHAR2(100) NOT NULL,
    customer_phone   VARCHAR2(15)  NOT NULL,
    guests           NUMBER(2)     NOT NULL,
    reservation_date DATE          NOT NULL,
    reservation_time VARCHAR2(10)  NOT NULL,
    special_request  VARCHAR2(300),
    status           VARCHAR2(20) DEFAULT 'CONFIRMED',
    created_at       TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_res_user  FOREIGN KEY (user_id)  REFERENCES users(user_id),
    CONSTRAINT fk_res_table FOREIGN KEY (table_id) REFERENCES restaurant_tables(table_id)
);

-- Prevent double-booking the same table on the same date & time slot
CREATE UNIQUE INDEX uq_table_slot
    ON reservations (table_id, reservation_date, reservation_time);

COMMIT;
