CREATE TABLE if not exists Person (
                                      personID VARCHAR(255) NOT NULL PRIMARY KEY,
                                      username VARCHAR(255),
                                      hashedPassword int,
                                      firstName VARCHAR(255),
                                      lastName VARCHAR(255),
                                      gender VARCHAR(50),
                                      personType ENUM ('CLIENT', 'ADMIN', 'MANAGER', 'DIRECTOR', 'DELIVERY_DRIVER', 'COOK')
);

insert into Person values ('mock_admin_id', 'mock_admin_un', 1557416358, 'Mock', 'Admin', 'X', 'ADMIN');
insert into Person values ('mock_client_id', 'mock_client_un', 1557416358, 'Mock', 'Client', 'X', 'CLIENT');
insert into Person values ('mock_manager_id', 'mock_manager_un', 1557416358, 'Mock', 'Manager', 'X', 'MANAGER');
insert into Person values ('mock_director_id', 'mock_director_un', 1557416358, 'Mock', 'Director', 'X', 'DIRECTOR');
insert into Person values ('mock_delivery_driver_id', 'mock_delivery_driver_un', 1557416358, 'Mock', 'Delivery Driver', 'X', 'DELIVERY_DRIVER');
-- passwords for all of these is: mock_pass

create table if not exists `Restaurant` (
                                            restaurantID varchar(255),
                                            name varchar(255),
                                            directorID varchar(255),
                                            primary key (restaurantID),
                                            foreign key (directorID) references Person(personID)
);

insert into `Restaurant` values ('mock_restaurant_id', 'Mock Restaurant', 'mock_director_id');

create table if not exists `Order` (
                                       orderID varchar(255),
                                       orderType ENUM('DINE_IN', 'TAKEAWAY', 'DELIVERY'),
                                       orderStatus ENUM('RECEIVED', 'PREPARING', 'WAITING_FOR_PICKUP', 'IN_DELIVERY', 'DONE'),
                                       deliveryDriverID varchar(255),
                                        restaurantID varchar(255),
                                       clientID varchar(255),
                                       orderDate date,
                                       primary key (orderID),
                                       foreign key (deliveryDriverID) references Person(personID),
                                       foreign key (clientID) references Person(personID),
    foreign key (restaurantID) references Restaurant(restaurantID)
);

insert into `Order` values (58934758, 'TAKEAWAY', 'RECEIVED', 'mock_delivery_driver_id', 'mock_restaurant_id', 'mock_client_id', sysdate());


create table if not exists `Meal`(
                                     mealID varchar(255),
                                     mealType ENUM('BEVERAGE', 'DISH', 'DESERT'),
                                     mealName varchar(255),
                                     price double,
                                     timeToPrepare double,
                                     primary key (mealID)
);

insert into `Meal` values ('mock_desert', 'DESERT', 'Mock Desert', 19.99, 10);
insert into `Meal` values ('mock_beverage', 'BEVERAGE', 'Mock Beverage', 2.99, 5);
insert into `Meal` values ('mock_dish', 'DISH', 'Mock Dish', 29.99, 15);

create table if not exists `Ingredient` (
                                            ingredientID varchar(255),
                                            ingredientName varchar(255),
                                            pricePerHundred double,
                                            caloriesPerHundred double,
                                            primary key (ingredientID)
);

insert into `Ingredient` values ('mock_ingredient1', 'Ingredient 1', 0.99, 120.15);
insert into `Ingredient` values ('mock_ingredient2', 'Ingredient 2', 2.99, 120.15);
insert into `Ingredient` values ('mock_ingredient3', 'Ingredient 3', 4.99, 120.15);
insert into `Ingredient` values ('mock_ingredient4', 'Ingredient 4', 12.99, 120.15);

create table if not exists `Item` (
                                      itemID varchar(255),
                                      itemName varchar(255),
                                      price double,
                                      primary key (itemID)
);

insert into `Item` values ('mock_item', 'Mock Item', 0.49);

create table if not exists `Meal_has_Ingredient` (
                                                     ingredientID varchar(255),
                                                     mealID varchar(255),
                                                     quantity double,
                                                     primary key (ingredientID, mealID),
                                                     foreign key (ingredientID) references Ingredient(ingredientID),
                                                     foreign key (mealID) references Meal(mealID)
);

insert into `Meal_has_Ingredient` values ('mock_ingredient1', 'mock_desert', 120.00);
insert into `Meal_has_Ingredient` values ('mock_ingredient2', 'mock_beverage', 120.00);
insert into `Meal_has_Ingredient` values ('mock_ingredient3', 'mock_dish', 120.00);
insert into `Meal_has_Ingredient` values ('mock_ingredient4', 'mock_dish', 120.00);

create table if not exists `Restaurant` (
    restaurantID varchar(255),
    restaurantName varchar(255),
    directorID varchar(255),
    primary key (restaurantID),
    foreign key (directorID) references Person (personID)
);

insert into `Restaurant` values ('mock_restaurant', 'Al Freddo', 'mock_director_id');

create table if not exists `Franchise` (
    franchiseID varchar(255),
    restaurantID varchar(255),
    managerID varchar(255),
    location varchar(255),
    primary key (franchiseID),
    foreign key (restaurantID) references Restaurant(restaurantID),
    foreign key (managerID) references Person(personID)
);

insert into `Franchise` values ('mock_franchise_id', 'mock_restaurant', 'mock_manager_id', 'Primaverii 25');

create table if not exists `Kitchen` (
                                         kitchenID varchar(255),
                                         franchiseID varchar(255),
                                         primary key (kitchenID),
                                         foreign key (franchiseID) references Franchise(franchiseID)
);

insert into Kitchen values ('mock_kitchen_id', 'mock_franchise_id');

create table if not exists `Meal_in_Restaurant` (
        restaurantID varchar(255),
        mealID varchar(255),
    primary key (restaurantID, mealID),
    foreign key (restaurantID) references Restaurant(restaurantID),
    foreign key (mealID) references  Meal(mealID)
);

insert into `Meal_in_Restaurant` values ('mock_restaurant_id', 'mock_desert');
insert into `Meal_in_Restaurant` values ('mock_restaurant_id', 'mock_beverage');
insert into `Meal_in_Restaurant` values ('mock_restaurant', 'mock_dish');
insert into `Meal_in_Restaurant` values ('mock_restaurant', 'mock_beverage');
-- todo add inserts here for mock data

create table if not exists `Ingredient_in_Kitchen` (
    ingredientID varchar(255),
    kitchenID varchar(255),
    quantity Double,
    primary key (ingredientID, kitchenID),
    foreign key (ingredientID) references Ingredient(ingredientID),
    foreign key (kitchenID) references Kitchen(kitchenID)
);

-- todo mock data

create table if not exists `Cook_in_Kitchen` (
    cookID varchar(255),
    kitchenID varchar(255),
    isBusy bool,
    primary key (cookID, kitchenID),
    foreign key (cookID) references Person(personID),
    foreign key (kitchenID) references Kitchen(kitchenID)
);

-- todo mock data