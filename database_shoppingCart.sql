CREATE TABLE product
(
    id_product INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    amount INT NOT NULL
);

CREATE TABLE cart
(
    id_cart INT PRIMARY KEY AUTO_INCREMENT
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

INSERT INTO product (name, category, price, amount)
VALUES ("Notebook", "Eletronics", 5000, 20),
       ("Motheboard", "Eletronics", 1500, 10),
       ("Smartphone", "Eletronics", 3000, 25),
       ("Headphones", "Eletronics", 300, 50),
       ("Tablet", "Eletronics", 2000, 15);