CREATE TABLE product
(
    id_product INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    amount INT NOT NULL
);


CREATE TABLE shoppingCart
(
    id_shoppingCart INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    amount INT NOT NULL,
    total_value DOUBLE NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart (id_cart),
    FOREIGN KEY (product_id) REFERENCES product (id_product)
);

CREATE TABLE cart
(
    id_cart INT PRIMARY KEY AUTO_INCREMENT
);