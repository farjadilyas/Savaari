/*
     CHANGES:
          ADDED NUM_RATINGS & NUM_RIDES TO RIDER AND DRIVER
*/

/*
FIND_STATUS: DEFAULT : 0, NOT_FOUND : 1, FOUND : 2
*/
CREATE TABLE `ride_hailing`.`RIDER_DETAILS` 
(
     `USER_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `USER_NAME` VARCHAR(32) NOT NULL,
     `EMAIL_ADDRESS` VARCHAR(255) UNIQUE NOT NULL,
     `PASSWORD` VARCHAR(255) NOT NULL,
     `RATING` DECIMAL(5,4) NOT NULL DEFAULT 0,
     `NUM_RATINGS` INT(8) NOT NULL DEFAULT 0,
     `NUM_RIDES` INT(8) NOT NULL DEFAULT 0,
     `LATITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `LONGITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
     `FIND_STATUS` INT(1) DEFAULT -1,
     `DRIVER_ID` INT(10) DEFAULT -1,
     PRIMARY KEY (`USER_ID`), 
     INDEX `USER_AUTH`(`EMAIL_ADDRESS`, `PASSWORD`)
) ENGINE = InnoDB;

/* 
Rating formula: avgRating*(numRatings/numRatings+1) + newRating/(numRatings+1)
++numRatings
*/


/*
     IS_ACTIVE: INACTIVE : 0, ACTIVE : 1
     RIDE_STATUS: NOT_ASSIGNED : 0, REQ_RECEIVED: 1, ACCEPTED: 2, CANCELLED: 3, COMPLETED: 4
     TODO: ADD FOREIGN KEY CONSTRAINT (USER_ID, ACTIVE_VEHICLE_ID) REFERENCES DRIVERS_VEHICLES(DRIVER_ID, VEHICLE_ID)
*/
CREATE TABLE `ride_hailing`.`DRIVER_DETAILS` 
(
     `USER_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `USER_NAME` VARCHAR(32) NOT NULL,
     `FIRST_NAME` VARCHAR(32),
     `LAST_NAME` VARCHAR(32),
     `PHONE_NO` VARCHAR(12),
     `CNIC` CHAR(13),
     `LICENSE_NO` CHAR(6),
     `EMAIL_ADDRESS` VARCHAR(255) UNIQUE NOT NULL,
     `PASSWORD` VARCHAR(255) NOT NULL,
     `RATING` DECIMAL(5,4) NOT NULL DEFAULT 0,
     `NUM_RATINGS` INT(8) NOT NULL DEFAULT 0,
     `STATUS` INT(1) NOT NULL DEFAULT 0,
     `NUM_RIDES` INT(8) NOT NULL DEFAULT 0,
     `LATITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `LONGITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
     `IS_ACTIVE` BOOLEAN DEFAULT FALSE,
     `RIDE_STATUS` INT(1) DEFAULT 0,
     `RIDER_ID` INT(10) DEFAULT -1,
     `SOURCE_LAT` DECIMAL(9,6) NULL DEFAULT 0,
     `SOURCE_LONG` DECIMAL(9,6) NULL DEFAULT 0,
     `DEST_LAT` DECIMAL(9,6) NULL DEFAULT 0,
     `DEST_LONG` DECIMAL(9,6) NULL DEFAULT 0,
     `PAYMENT_MODE` INT(1),
     `RIDE_TYPE` INT(10) UNSIGNED NOT NULL DEFAULT 1,
     `SPLIT_FARE` BOOLEAN DEFAULT FALSE,
     `ACTIVE_VEHICLE_ID` INT(3) UNSIGNED DEFAULT 0,
     PRIMARY KEY (`USER_ID`),
     INDEX `USER_AUTH`(`EMAIL_ADDRESS`, `PASSWORD`)
) ENGINE = InnoDB;


CREATE TABLE `ride_hailing`.`RIDE_TYPES`
(
     `TYPE_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `NAME` VARCHAR(32) UNIQUE NOT NULL,
     `MAX_PASSENGERS` INT(2) UNSIGNED NOT NULL,
     `BASE_FARE` DECIMAL(8,2) UNSIGNED NOT NULL,
     `PER_KM_CHARGE` DECIMAL(8,2) UNSIGNED NOT NULL,
     `PER_MIN_CHARGE` DECIMAL(8,2) UNSIGNED NOT NULL,
     `MIN_FARE` DECIMAL(8,2) UNSIGNED NOT NULL,
     STATUS BOOLEAN DEFAULT FALSE,
     PRIMARY KEY (`TYPE_ID`)
) ENGINE = InnoDB;


INSERT INTO RIDE_TYPES
VALUES(0, "Bike", 1, 17.71, 12.13, 3.61, 71, true),
(0, "Small", 3, 64, 8.8, 3.9, 96, true),
(0, "Med", 4, 80, 11, 4.91, 120, true), 
(0, "NotSoSmol", 6, 94, 13.4, 5.7, 160, true);

CREATE TABLE `ride_hailing`.`VEHICLE_TYPES`
(
     `VEHICLE_TYPE_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `MAKE` VARCHAR(16),
     `MODEL` VARCHAR(32),
     `YEAR` CHAR(4),
     `RIDE_TYPE_ID` INT(10) UNSIGNED NOT NULL,
     PRIMARY KEY (`VEHICLE_TYPE_ID`),
     FOREIGN KEY (`RIDE_TYPE_ID`) REFERENCES `RIDE_TYPES`(`TYPE_ID`),
     UNIQUE(`MAKE`, `MODEL`, `YEAR`)
) ENGINE = InnoDB;

INSERT INTO VEHICLE_TYPES VALUES(0, 'HONDA', 'CITY', '2013', 3),
(0, 'HONDA', 'INSIGHT', '2015', 2),
(0, 'HONDA', 'CITY', '2016', 3),
(0, 'HONDA', '120', '2018', 1),
(0, 'HONDA', 'CD70', '2012', 1),
(0, 'TOYOTA', 'COROLLA', '2017', 3),
(0, 'HONDA', 'ACCORD', '2013', 4),
(0, 'TOYOTA', 'CROWN', '2018', 4);

/*
* STATUS:
* 0 -> DEFAULT
* 1 -> SENT
* 2 -> REJECTED
* 3 -> ACCEPTED
*/
/* DRIVERS_VEHICLES DDL and inserts */
CREATE TABLE `ride_hailing`.`DRIVERS_VEHICLES`
(
     `DRIVER_ID` INT(10) UNSIGNED NOT NULL,
     `VEHICLE_ID` INT(3) UNSIGNED NOT NULL,
     `VEHICLE_TYPE_ID` INT(10) UNSIGNED NOT NULL,
     `NUMBER_PLATE` CHAR(8) NOT NULL,
     `STATUS` INT(1) DEFAULT 0,
     `COLOR` VARCHAR(32),
     PRIMARY KEY (`DRIVER_ID`, `VEHICLE_ID`),
     FOREIGN KEY (`VEHICLE_TYPE_ID`) REFERENCES `VEHICLE_TYPES`(`VEHICLE_TYPE_ID`)
) ENGINE = InnoDB;

DELIMITER $$
CREATE OR REPLACE TRIGGER DV_ON_INSERT
     BEFORE INSERT ON DRIVERS_VEHICLES
FOR EACH ROW 
BEGIN
    SET NEW.VEHICLE_ID = (
       SELECT IFNULL(MAX(VEHICLE_ID), 0) + 1
       FROM DRIVERS_VEHICLES
       WHERE DRIVER_ID = NEW.DRIVER_ID);
END$$

/* --- --- END OF SECTION --- ---*/


/*
* STATUS:
* 0 -> DEFAULT
* 1 -> SENT
* 2 -> REJECTED
* 3 -> ACCEPTED
*/
CREATE TABLE `ride_hailing`.`VEHICLE_REGISTRATION_REQ`
(
     `DRIVER_ID` INT(10) UNSIGNED NOT NULL,
     `REGISTRATION_REQ_ID` INT(4) UNSIGNED NOT NULL,
     `MAKE` VARCHAR(16),
     `MODEL` VARCHAR(32),
     `YEAR` CHAR(4),
     `NUMBER_PLATE` CHAR(8) NOT NULL,
     `COLOR` VARCHAR(32),
     `STATUS` INT(1) DEFAULT 0,
     PRIMARY KEY (`DRIVER_ID`, `REGISTRATION_REQ_ID`),
     UNIQUE(`DRIVER_ID`, `NUMBER_PLATE`),
    FOREIGN KEY (`DRIVER_ID`) REFERENCES `DRIVER_DETAILS`(`USER_ID`)
) ENGINE = InnoDB; 

DELIMITER $$
CREATE OR REPLACE TRIGGER VRR_ON_INSERT
     BEFORE INSERT ON VEHICLE_REGISTRATION_REQ
FOR EACH ROW 
BEGIN
    SET NEW.REGISTRATION_REQ_ID = (
       SELECT IFNULL(MAX(REGISTRATION_REQ_ID), 0) + 1
       FROM VEHICLE_REGISTRATION_REQ
       WHERE DRIVER_ID = NEW.DRIVER_ID);
END$$

CREATE TABLE `ride_hailing`.`PAYMENTS`
(
     `PAYMENT_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `AMOUNT_PAID` DECIMAL(8,2),
     `CHANGE` DECIMAL(8,2),
     `TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
     `PAYMENT_MODE` INT(1),
     PRIMARY KEY (`PAYMENT_ID`)
) ENGINE = InnoDB;


/*
 * TODO: REFERENCE DRIVERS_VEHICLES INSTEAD OF DRIVERS
*/
CREATE TABLE `ride_hailing`.`RIDES`
(
     `RIDE_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `RIDER_ID` INT(10) UNSIGNED NOT NULL,
     `DRIVER_ID` INT(10) UNSIGNED NOT NULL,
     `VEHICLE_ID` INT(10) UNSIGNED NOT NULL,
     `PAYMENT_ID` INT(10) UNSIGNED UNIQUE NULL,
     `SOURCE_LAT` DECIMAL(9,6) NULL DEFAULT 0,
     `SOURCE_LONG` DECIMAL(9,6) NULL DEFAULT 0,
     `DEST_LAT` DECIMAL(9,6) NULL DEFAULT 0,
     `DEST_LONG` DECIMAL(9,6) NULL DEFAULT 0,
     `START_TIME` TIMESTAMP NULL DEFAULT NULL,
     `FINISH_TIME` TIMESTAMP NULL DEFAULT NULL,
     `DIST_TRAVELLED` DECIMAL(7, 3) DEFAULT 0,
     `RIDE_TYPE` INT(10) UNSIGNED NOT NULL,
     `POLICY_ID` INT(2) UNSIGNED NOT NULL DEFAULT 1,
     `ESTIMATED_FARE` INT(4) UNSIGNED,
     `FARE` DECIMAL(8,2) UNSIGNED,
     `STATUS` INT(1) UNSIGNED DEFAULT 0,
     PRIMARY KEY (`RIDE_ID`),
     FOREIGN KEY (`RIDER_ID`) REFERENCES `RIDER_DETAILS`(`USER_ID`),
     FOREIGN KEY (`DRIVER_ID`, `VEHICLE_ID`) REFERENCES `DRIVERS_VEHICLES`(`DRIVER_ID`, `VEHICLE_ID`),
     FOREIGN KEY (`PAYMENT_ID`) REFERENCES `PAYMENTS`(`PAYMENT_ID`),
     FOREIGN KEY (`RIDE_TYPE`) REFERENCES `RIDE_TYPES`(`TYPE_ID`),
     INDEX `RIDER_RIDES`(`RIDER_ID`),
     INDEX `DRIVER_RIDES`(`DRIVER_ID`)
) ENGINE = InnoDB;

/*FOREIGN KET (`DRIVER_ID`) REFERENCES `DRIVER_DETAILS`(`USER_ID`),*/

DROP TABLE `RIDES`;
DROP TABLE `PAYMENTS`;
DROP TABLE `VEHICLE_REGISTRATION_REQ`;
DROP TABLE `DRIVERS_VEHICLES`;
DROP TABLE `VEHICLE_TYPES`;
DROP TABLE `RIDE_TYPES`;
DROP TABLE `DRIVER_DETAILS`;
DROP TABLE `RIDER_DETAILS`;



/* Initialize tables with necessary data */




/*RESET ALL DRIVERS*/
UPDATE DRIVER_DETAILS
SET IS_ACTIVE = 0, RIDE_STATUS = 0, RIDER_ID = -1, SOURCE_LAT = 0, SOURCE_LONG = 0, DEST_LAT = 0, DEST_LONG = 0;

UPDATE RIDER_DETAILS
SET FIND_STATUS = 0, DRIVER_ID = -1;

DELETE FROM RIDES;

DELETE FROM PAYMENTS;

DELETE FROM SPRING_SESSION;

DELETE FROM SPRING_SESSION_ATTRIBUTES;