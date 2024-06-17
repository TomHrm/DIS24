CREATE TABLE Shop (
    ShopID SERIAL PRIMARY KEY ,
    Name varchar(255) NOT NULL ,
    City varchar(255) NOT NULL ,
    Region varchar(255) NOT NULL ,
    Country varchar (255) NOT NULL
);

CREATE TABLE Date (
    DateId SERIAL PRIMARY KEY ,
    Day INT NOT NULL,
    Month INT NOT NULL ,
    Quarter INT NOT NULL ,
    Year INT NOT NULL
);

CREATE TABLE Article (
    ArticleId SERIAL PRIMARY KEY ,
    Name varchar(255) NOT NULL ,
    Price double precision NOT NULL,
    ProductGroupName varchar(255) NOT NULL ,
    ProductFamilyName varchar(255) NOT NULL ,
    ProductCategoryName varchar(255) NOT NULL
);

CREATE TABLE Sale (
                      SaleID SERIAL PRIMARY KEY ,
                      ShopID int NOT NULL ,
                      ArticleID int NOT NULL ,
                      DayId int NOT NULL ,
                      Sold bigint NOT NULL
);


ALTER TABLE Sale ADD CONSTRAINT SaleID_fk_1 FOREIGN KEY (ShopID) REFERENCES Shop (ShopID);
ALTER TABLE Sale ADD CONSTRAINT SaleID_fk_2 FOREIGN KEY (ArticleID) REFERENCES Article (ArticleID);
ALTER TABLE Sale ADD CONSTRAINT SaleID_fk_3 FOREIGN KEY (DayId) REFERENCES Date (DateId);

SELECT articleid FROM article WHERE Name = 'AEG �ko Lavatherm 59850 Sensidry';
