CREATE TABLE customers
(   id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    birth DATE,
    email VARCHAR(100)
);
CREATE TABLE cars
(   id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model VARCHAR(100),
    plate VARCHAR(100),
    fee DECIMAL(10,2)
);
CREATE TABLE rents
(   id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INTEGER,
    car_id INTEGER,
    date_from DATE,
    date_to DATE,
    cost DECIMAL(10,2),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);