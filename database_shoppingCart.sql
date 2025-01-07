CREATE TABLE product
(
    id_product INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    category   VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    amount     INT          NOT NULL
);

CREATE TABLE shoppingCart
(
    id_shopping INT PRIMARY KEY AUTO_INCREMENT,
    amount      INT NOT NULL,
    product_id  INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id_product)
);