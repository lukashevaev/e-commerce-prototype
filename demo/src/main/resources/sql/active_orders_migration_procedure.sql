CREATE OR REPLACE PROCEDURE InsertNonCompletedOrdersToNewOrders()
    LANGUAGE SQL
AS $$
INSERT INTO new_orders (id, item_id, city_id, is_completed, creation_date, completion_date)
SELECT id, item_id, city_id, is_completed, creation_date, completion_date FROM orders
    WHERE is_completed = false;
DELETE FROM orders
    WHERE is_completed = false;
$$;


