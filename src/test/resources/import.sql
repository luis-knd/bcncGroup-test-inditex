INSERT INTO brand (id, name) VALUES (1, 'Zara');
INSERT INTO brand (id, name) VALUES (2, 'Bershka');
INSERT INTO brand (id, name) VALUES (3, 'Stradivarius');
INSERT INTO brand (id, name) VALUES (4, 'Oysho');

INSERT INTO product (id, name) VALUES (35455, 'Product 35455');

INSERT INTO price (brand_id, product_id, start_date, end_date, price_list, priority, price, currency) VALUES (1, 35455, '2020-06-14T00:00:00', '2020-12-31T23:59:59', 1, 0, 35.50, 'EUR');
INSERT INTO price (brand_id, product_id, start_date, end_date, price_list, priority, price, currency) VALUES (1, 35455, '2020-06-14T15:00:00', '2020-06-14T18:30:00', 2, 1, 25.45, 'EUR');
INSERT INTO price (brand_id, product_id, start_date, end_date, price_list, priority, price, currency) VALUES (1, 35455, '2020-06-15T00:00:00', '2020-06-15T11:00:00', 3, 1, 30.50, 'EUR');
INSERT INTO price (brand_id, product_id, start_date, end_date, price_list, priority, price, currency) VALUES (1, 35455, '2020-06-15T16:00:00', '2020-12-31T23:59:59', 4, 1, 38.95, 'EUR');