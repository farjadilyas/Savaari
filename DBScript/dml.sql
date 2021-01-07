/*
*    RIDE-HAILING SERVICE
*    SOFTWARE DESIGN AND ANALYSIS SEMESTER PROJECT
*    
*    Muhammad Farjad Ilyas - 18I-0436 - CS_E
*    Nabeel Danish         - 18i-0579 - CS_E
*
*    DML Script
*/

/* Initialize table with necessary data */

INSERT INTO ADMIN_DETAILS
VALUES(0, 'saad@gmail.com', 'Password2109', 'Saad', 'Saqlain', '3137734043', '4213329578622', 1);

INSERT INTO RIDE_TYPES
VALUES(0, "Bike", 1, 17.71, 12.13, 3.61, 71, true),
(0, "Small", 3, 64, 8.8, 3.9, 96, true),
(0, "Med", 4, 80, 11, 4.91, 120, true), 
(0, "NotSoSmol", 6, 94, 13.4, 5.7, 160, true);

INSERT INTO VEHICLE_TYPES VALUES(0, 'HONDA', 'CITY', '2013', 3),
(0, 'HONDA', 'INSIGHT', '2015', 2),
(0, 'HONDA', 'CITY', '2016', 3),
(0, 'HONDA', '120', '2018', 1),
(0, 'HONDA', 'CD70', '2012', 1),
(0, 'TOYOTA', 'COROLLA', '2017', 3),
(0, 'HONDA', 'ACCORD', '2013', 4),
(0, 'TOYOTA', 'CROWN', '2018', 4);

/*RESET RIDE-RELATED TABLES*/
UPDATE DRIVER_DETAILS
SET IS_ACTIVE = 0, RIDE_STATUS = 0, RIDER_ID = -1, SOURCE_LAT = 0, SOURCE_LONG = 0, DEST_LAT = 0, DEST_LONG = 0;

UPDATE RIDER_DETAILS
SET FIND_STATUS = 0, DRIVER_ID = -1;

DELETE FROM RIDES;

DELETE FROM PAYMENTS;

DELETE FROM SPRING_SESSION;

DELETE FROM SPRING_SESSION_ATTRIBUTES;