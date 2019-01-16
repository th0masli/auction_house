-- SUPER CLASS FOR CUSTOMER AND EMPLOYEE; AN EMPLOYEE COULD BE A CUSTOMER AND VICE VERSA
CREATE TABLE Person (
    ID                CHAR(20), -- only employee needs an SSN
    LastName          CHAR(20) NOT NULL,
    FirstName         CHAR(20) NOT NULL,
    Address           TEXT,
    City              CHAR(20),
    State             CHAR(20),
    ZipCode           INTEGER,
    Telephone         VARCHAR(10),
    Email             CHAR(30),
    PRIMARY KEY (ID)
);

-- INSERT DATA
INSERT INTO Person
    (ID, LastName, FirstName, Address, City, State, ZipCode, Telephone, Email)
VALUES
    ('shiyong', 'Lu', 'Shiyong', '123 Success Street', 'Stony Brook',
      'NY', 11790, '5166328959', 'shiyong@cs.sunysb.edu'),
    ('haixia', 'Du', 'Haixia', '456 Fortune Road', 'Stony Brook',
      'NY', 11790, '5166324360', 'dhaixia@cs.sunysb.edu'),
    ('john', 'Smith', 'John', '789 Peace Blvd', 'Los Angeles',
     'CA', 12345, '4124434321', 'shlu@ic.sunysb.edu'),
    ('phil', 'Phil', 'Lewis', '135 Knowledge Lane', 'Stony Brook',
     'NY', 11794, '5166668888', 'pml@cs.sunysb.edu'),
    ('123456789', 'Smith', 'David', '123 Colledge Road', 'Stony Brook',
     'NY', 11790, '5162152345', 'smith@ytc.com'),
    ('789123456', 'Warren', 'David', '456 Sunken Street', 'Stony Brook',
     'NY', 11794, '5166329987', 'warren@ytc.com');


CREATE TABLE Customer (
    CustomerID        CHAR(20), -- customer id can be arbitrary
    CreditCardNum     VARCHAR(16), -- INTEGER(16),
    ItemsSold         INTEGER CHECK(ItemsSold >= 0),
    ItemsPurchased    INTEGER CHECK(ItemsPurchased >= 0),
    Rating            INTEGER,
    PRIMARY KEY (CustomerID),
    FOREIGN KEY (CustomerID) REFERENCES Person (ID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- INSERT DATA
INSERT INTO Customer
    (CustomerID, CreditCardNum, ItemsSold, ItemsPurchased, Rating)
VALUES
    ('shiyong', '1234567812345678', 0, 1, 1),
    ('haixia', '5678123456781234', 0, 0, 1),
    ('john', '2345678923456789', 0, 0, 1),
    ('phil', '6789234567892345', 1, 0, 1);


CREATE TABLE Employee (
    SSN               CHAR(20), -- employee id is an ssn
    StartDate         DATETIME,
    HourlyRate        DECIMAL(13,2),
    Level             INTEGER, -- 1: manager 0: employee
    PRIMARY KEY (SSN),
    FOREIGN KEY (SSN) REFERENCES Person (ID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- INSERT DATA
INSERT INTO Employee
    (SSN, StartDate, HourlyRate, Level)
VALUES
    ('123456789', '1998-11-01', 60, 0),
    ('789123456', '1999-02-02', 50, 1);


-- ITEM TABLE
CREATE TABLE Item (
    ItemID            INTEGER CHECK (ItemID >= 0 AND ItemID < 10000000),
    Description       TEXT,
    Name              CHAR(255) NOT NULL,
    Type              ENUM('DVD', 'BOOK', 'CAR', 'LAPTOP', 'OTHER'),
    ManuYear          INTEGER NOT NULL,
    Sold              INTEGER CHECK (Sold >= 0),
    NumCopies         INTEGER CHECK (NumCopies >= 0),
    PRIMARY KEY (ItemID)
);

-- INSERT DATA
INSERT INTO Item
    -- ItemID is AUTO_INCREMENT so we do not need to specify
    (ItemID, Description, Name, Type, ManuYear, Sold, NumCopies)
VALUES
    (0, NULL, 'Titanic', 'DVD', 2005, 1, 3),
    (1, NULL, 'Nissan Sentra', 'Car', 2007, 0, 1);


-- RECORD THE AUCTION DATA
CREATE TABLE Auction (
    AuctionID         INTEGER CHECK (AuctionID >= 0 AND AuctionID < 10000000),
    BidIncrement      DECIMAL(13,2),
    MinimuBid         DECIMAL(13,2),
    Copies_Sold       INTEGER CHECK (Copies_Sold >= 0),
    Monitor           CHAR(20) NOT NULL,
    ItemID            INTEGER NOT NULL CHECK (ItemID >= 0 AND ItemID < 10000000),
    ReservePrice      DECIMAL(13,2),
    -- add a status column; and a winnner column
    PRIMARY KEY (AuctionID),
    FOREIGN KEY (Monitor) REFERENCES Employee (SSN)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (ItemID) REFERENCES Item (ItemID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- INSERT DATA
INSERT INTO Auction
    (AuctionID, BidIncrement, MinimuBid, Copies_Sold, Monitor, ItemID)
VALUES
    (0, 1, 5, 1, '123456789', 0),
    (1, 10, 1000, 0, '789123456', 1);


-- CUSTOMER CAN POST AN ITEM TO AUCTION
CREATE TABLE Post (
    AuctionID         INTEGER CHECK (AuctionID >= 0 AND AuctionID < 10000000),
    ExpireDate        DATETIME,
    PostDate          DATETIME,
    CustomerID        CHAR(20),
    PRIMARY KEY (CustomerID, AuctionID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (AuctionID) REFERENCES Auction(AuctionID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- INSERT DATA
INSERT INTO Post
    (AuctionID, ExpireDate, PostDate, CustomerID)
VALUES
    (0, '2008-12-16 13:00:00', '2008-12-9 13:00:00', 'phil'),
    (1, '2028-12-16 13:00:00', '2008-12-9 13:00:00', 'john');


-- CUSTOMER CAN ALSO BID ITEM
CREATE TABLE Bid (
    CustomerID        CHAR(20),
    AuctionID         INTEGER CHECK (AuctionID >= 0 AND AuctionID < 10000000),
    BidTime           DATETIME,
    MaxBid            DECIMAL(13,2),
    BidPrice          DECIMAL(13,2),
    CurrentWinner     CHAR(20),
    PRIMARY KEY (CustomerID, AuctionID, BidTime),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (AuctionID) REFERENCES Auction(AuctionID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    -- a currentWinner column added
    FOREIGN KEY (CurrentWinner) REFERENCES Customer(CustomerID)
        -- ON DELETE NO ACTION
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- INSERT DATA
INSERT INTO Bid
    (CustomerID, AuctionID, BidTime, MaxBid, BidPrice, CurrentWinner)
VALUES
    ('haixia', 0, '2008-12-12 10:00:00', 10, 5, 'haixia'),
    ('shiyong', 0, '2008-12-12 11:00:00', 10, 9, 'haixia'),
    ('shiyong', 0, '2008-12-12 11:30:00', 10, 10, 'haixia'),
    ('shiyong', 0, '2008-12-12 13:00:00', 15, 11, 'shiyong');
    -- ('Closing', 0, '2008-12-12 13:00:00', 15, 11, 'shiyong')

-- add views

-- Produce a summary listing of revenue generated by a particular item, item type, or customer
CREATE VIEW WinningPrice (AuctionID, BidPrice) AS
SELECT DISTINCT Bid.AuctionID, MAX(Bid.BidPrice)
FROM Bid, Post WHERE Bid.AuctionID = Post.AuctionID
AND Post.ExpireDate <= NOW() GROUP BY Bid.auctionID;

-- Determine which customer representative generated most total revenue
CREATE VIEW EmployeeRevenue (EmployeeID, TotalRevenue) AS
SELECT Auction.Monitor, SUM(WinningPrice.BidPrice)
FROM Auction, WinningPrice
WHERE Auction.AuctionID = WinningPrice.AuctionID
GROUP BY Auction.Monitor;

-- Determine which customer generated most total revenue
CREATE VIEW Winner (CustomerID, TotalPurchase) AS
SELECT Bid.CustomerID, SUM(WinningPrice.BidPrice) FROM
WinningPrice NATURAL JOIN Bid
GROUP BY Bid.CustomerID;

-- Produce a list of item suggestions for a given customer
CREATE VIEW AuctionWinner(AuctionID, Winner, CurrentBid, CurrentHighBid) AS
SELECT b1.AuctionID, b1.CurrentWinner, b1.BidPrice, b1.MaxBid
FROM bid b1 LEFT OUTER JOIN bid b2
ON b1.AuctionID = b2.AuctionID AND b1.BidTime < b2.BidTime
WHERE b2.AuctionID IS NULL;

CREATE VIEW ItemTypeWinner(ItemID, Type, Winner) AS
SELECT I.ItemID, I.Type, W.Winner
FROM Item AS I, Auction AS A, AuctionWinner AS W
WHERE I.ItemID = A.ItemID AND A.AuctionID = W.AuctionID;
