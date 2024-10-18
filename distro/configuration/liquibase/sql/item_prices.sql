insert into cashier_item_price (`service_id`, `price`, `payment_mode`, `name`, `creator`, `date_created`, `uuid`) 
values 
    ('1', '50.00', '1', 'Cash', '1', NOW(), uuid()),
    ('1', '55.00', '2', 'Bank transfer', '1', NOW(), uuid()),
    ('1', '55.00', '3', 'Paypal', '1', NOW(), uuid()),
    
    ('2', '80.00', '1', 'Cash', '1', NOW(), uuid()),
    ('2', '90.00', '2', 'Bank transfer', '1', NOW(), uuid()),
    ('2', '90.00', '3', 'Paypal', '1', NOW(), uuid()),
    
    ('3', '27.00', '1', 'Cash', '1', NOW(), uuid()),
    ('3', '32.00', '2', 'Bank transfer', '1', NOW(), uuid()),
    ('3', '32.00', '3', 'Paypal', '1', NOW(), uuid()),
    
    ('4', '15.00', '1', 'Cash', '1', NOW(), uuid());