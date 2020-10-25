CREATE TABLE `ride_hailing`.`RIDER_DETAILS` 
(
     `USER_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `USER_NAME` VARCHAR(32) NOT NULL,
     `EMAIL_ADDRESS` VARCHAR(255) NOT NULL,
     `PASSWORD` VARCHAR(255) NOT NULL,
     `LATITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `LONGITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
     `FIND_STATUS` INT(1) DEFAULT 0,
     `DRIVER_ID` INT(10) DEFAULT -1,
     PRIMARY KEY (`USER_ID`), 
     INDEX `USER_AUTH`(`EMAIL_ADDRESS`, `PASSWORD`)
) ENGINE = InnoDB;

CREATE TABLE `ride_hailing`.`DRIVER_DETAILS` 
(
     `USER_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
     `USER_NAME` VARCHAR(32) NOT NULL,
     `EMAIL_ADDRESS` VARCHAR(255) NOT NULL,
     `PASSWORD` VARCHAR(255) NOT NULL,
     `LATITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `LONGITUDE` DECIMAL(9,6) NULL DEFAULT 0,
     `TIMESTAMP` TIMESTAMP NULL DEFAULT NULL,
     `IS_ACTIVE` BOOLEAN DEFAULT FALSE,
     `RIDE_STATUS` INT(1) DEFAULT 0,
     `RIDER_ID` INT(10) DEFAULT -1,
     `DEST_LAT` DECIMAL(9,6) NULL DEFAULT 0,
     `DEST_LONG` DECIMAL(9,6) NULL DEFAULT 0,
     PRIMARY KEY (`USER_ID`), 
     INDEX `USER_AUTH`(`EMAIL_ADDRESS`, `PASSWORD`)
) ENGINE = InnoDB;

/* Query for findDriver() */
SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST (LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS
WHERE (LATITUDE BETWEEN %s AND %s) AND (LONGITUDE BETWEEN %s and %s)
ORDER BY LATITUDE %s + LONGITUDE - %s 


/* Query for checkRideStatus(), return rider details if request has been received */
SELECT D.RIDE_STATUS, D.RIDER_ID, R.USER_NAME, R.LATITUDE AS SOURCE_LAT, R.LONGITUDE AS SOURCE_LONG, D.DEST_LAT, D.DEST_LONG
FROM driver_details D
LEFT JOIN rider_details R
ON D.RIDER_ID = R.USER_ID
WHERE D.RIDE_STATUS = 1;

