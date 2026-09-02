CREATE TABLE product (
 id INT PRIMARY KEY AUTO_INCREMENT,
 product_name VARCHAR(255) NOT NULL,
 created_by VARCHAR(100) NOT NULL,
 created_on TIMESTAMP NOT NULL,
 modified_by VARCHAR(100),
 modified_on TIMESTAMP
);

CREATE TABLE item (
 id INT PRIMARY KEY AUTO_INCREMENT,
 product_id INT NOT NULL,
 quantity INT NOT NULL,
 FOREIGN KEY (product_id) REFERENCES product(id)
);
