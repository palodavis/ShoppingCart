CREATE TABLE product
(
    id_product INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    amount INT NOT NULL
);

CREATE TABLE shoppingCart (
    id_cart INT NOT NULL AUTO_INCREMENT,
    id_shopping INT NOT NULL,
    product_id INT NOT NULL,
    amount INT NOT NULL,
    total_value DOUBLE NOT NULL,
    PRIMARY KEY (id_cart),
    FOREIGN KEY (product_id) REFERENCES product (id_product)
);
