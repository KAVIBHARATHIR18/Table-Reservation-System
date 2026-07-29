--------------------------------------------------------------------
-- Sample data for Ya Mohaideen Briyani Reservation System
-- Run AFTER schema.sql
--------------------------------------------------------------------

-- Restaurant tables (10 tables of varying capacity)
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T1', 2, 'Window Side');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T2', 2, 'Window Side');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T3', 4, 'Main Hall');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T4', 4, 'Main Hall');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T5', 4, 'Main Hall');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T6', 6, 'Family Section');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T7', 6, 'Family Section');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T8', 8, 'Party Hall');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T9', 2, 'Outdoor Seating');
INSERT INTO restaurant_tables (table_no, capacity, location_desc) VALUES ('T10', 4, 'Outdoor Seating');

-- Dishes (recently introduced + regular favorites)
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Ya Mohaideen Special Mutton Briyani', 'Slow-cooked mutton briyani with our signature blend of 18 spices', 320.00, 'Briyani', 'Y');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Chicken 65 Briyani', 'Classic chicken briyani topped with crispy chicken 65', 260.00, 'Briyani', 'Y');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Prawn Masala Briyani', 'Fresh prawns simmered in a spicy masala, layered with fragrant rice', 340.00, 'Briyani', 'Y');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Egg Briyani', 'Traditional egg briyani cooked in dum style', 150.00, 'Briyani', 'N');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Chicken Briyani', 'Our all-time bestseller, tender chicken with basmati rice', 220.00, 'Briyani', 'N');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Mutton Chukka', 'Dry roasted mutton tossed with onions and curry leaves', 280.00, 'Starters', 'N');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Chicken Lollipop', 'Crispy fried chicken lollipops with spicy sauce', 190.00, 'Starters', 'N');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Butter Naan', 'Soft tandoor-baked naan brushed with butter', 40.00, 'Breads', 'N');
INSERT INTO dishes (dish_name, description, price, category, is_new) VALUES
 ('Rose Falooda', 'Chilled rose-flavored dessert with vermicelli and ice cream', 90.00, 'Desserts', 'Y');

-- Offers (linked to dish_id — assumes identity starts at 1 in insert order above)
INSERT INTO offers (dish_id, discount_percent, offer_title, valid_until) VALUES
 (1, 15, 'Weekend Special - 15% off Mutton Briyani', TO_DATE('2026-12-31','YYYY-MM-DD'));
INSERT INTO offers (dish_id, discount_percent, offer_title, valid_until) VALUES
 (3, 10, 'Seafood Friday - 10% off Prawn Briyani', TO_DATE('2026-12-31','YYYY-MM-DD'));
INSERT INTO offers (dish_id, discount_percent, offer_title, valid_until) VALUES
 (9, 20, 'Sweet Treat - 20% off Rose Falooda with any Briyani', TO_DATE('2026-12-31','YYYY-MM-DD'));

COMMIT;
