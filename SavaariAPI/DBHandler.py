"""
    Savaari Application Programming Interface
    Nabeel Danish               18I-0579
    Muhammad Farjad Ilyas       18I-0436
"""

import json
import pymysql
from flask import jsonify
from config import mysql

import time

class DBHandler:
    def __init__(self):
        self.conn = mysql.connect()
        self.cursor = self.conn.cursor()

    def executeQuery(self, sqlQuery, data):
        self.cursor.execute(sqlQuery, data)
        self.conn.commit()
        return self.cursor.fetchall()

    def add_rider(self, nickname, username, password):
        try:
            sqlQuery = "INSERT INTO `RIDER_DETAILS` (`USER_ID`, `USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (%s, %s, %s, %s)"
            data = (0, nickname, sha256(password.encode()).hexdigest(), username)
            
            self.executeQuery(sqlQuery, data)

            return json.dumps({"STATUS_CODE" : 200})
        except Exception as e:
            print(e)
            return json.dumps({"STATUS_CODE": 404})
            

    # create Driver		
    def add_driver(self,emailAddress, username, password):
        try:            
            # insert record in database
            sqlQuery = "INSERT INTO `DRIVER_DETAILS` (`USER_ID`, `USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (%s, %s, %s, %s)"
            data = (0, username, sha256(password.encode()).hexdigest(), emailAddress)
            
            self.executeQuery(sqlQuery, data)

            return json.dumps({"STATUS_CODE" : 200})
        except Exception as e:
            print(e)
            return json.sumps({"STATUS_CODE": 404})

            

    # Login User
    def login_rider(self, username, password):
        try:
            sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE EMAIL_ADDRESS = %s AND PASSWORD = %s"
            data = (username, sha256(password.encode()).hexdigest())

            rows = self.executeQuery(sqlQuery, data)

            if (self.cursor.rowcount == 0):
                results = {"USER_ID": -1, "STATUS_CODE": 200}
            else:
                res = jsonify(rows)
                res.status_code = 200
                results = {"USER_ID" : rows[0][0], "STATUS_CODE" : 200}
            
            results = json.dumps(results)
            print(results)

            return results
        except Exception as e:
            print(e)
            return json.dumps({"USER_ID": -1, "STATUS_CODE": 404})       


    # Login User
    def login_driver(self, username, password):
        try:
            sqlQuery = "SELECT USER_ID FROM DRIVER_DETAILS WHERE EMAIL_ADDRESS = %s AND PASSWORD = %s"
            data = (username, sha256(password.encode()).hexdigest())

            rows = self.executeQuery(sqlQuery, data)

            if (self.cursor.rowcount == 0):
                results = {"USER_ID": -1, "STATUS_CODE": 200}
            else:
                res = jsonify(rows)
                res.status_code = 200
                results = {"USER_ID" : rows[0][0], "STATUS_CODE" : 200}
            
            results = json.dumps(results)
            print(results)

            return results
        except Exception as e:
            print(e)
            return json.dumps({"USER_ID" : -1, "STATUS_CODE": 404})
            

    # Get Method for User Details
    def rider_details(self):
        try:
            sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM RIDER_DETAILS"
            
            rows = self.executeQuery(sqlQuery, ())
            return json.dumps(rows)

        except Exception as e:
            print(e)
            return json.dumps({"STATUS": 404})
            

    # Get Method for User Details
    def driver_details(self):
        try:
            sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM DRIVER_DETAILS"
            rows = executeQuery(sqlQuery, ())
            return json.dumps(rows)

        except Exception as e:
            print(e)
        

    # Load Rider Data
    def rider_data(self, userID):
        try:
            sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM RIDER_DETAILS WHERE USER_ID = %s"
            data = (userID)

            rows = executeQuery(sqlQuery, data)
            results = {"STATUS": 200, "USER_NAME": rows[0][0], "EMAIL_ADDRESS" : rows[0][1]}
            
            return json.dumps(results)

        except Exception as e:
            print(e)
            return json.dumps({"STATUS" : 404})


    # Load Driver Data
    def driver_data(self, userID):
        try:
            sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM DRIVER_DETAILS WHERE USER_ID = %s"
            data = (userID)

            rows = executeQuery(sqlQuery, data)

            results = {"USER_NAME": rows[0][0], "EMAIL_ADDRESS" : rows[0][1]}
            return json.dumps(results)

        except Exception as e:
            print(e)
            return json.dumps({"STATUS" : 404})
        

    """	
    @app.route('/student/<int:student_id>')
    def student(student_id):
        try:
            conn = mysql.connect()
            cursor = conn.cursor(pymysql.cursors.DictCursor)
            cursor.execute("SELECT * FROM student WHERE id=%s", student_id)
            row = cursor.fetchone()
            res = jsonify(row)
            res.status_code = 200
            return res
        except Exception as e:
            print(e)
        
    """      
 

    """
    Return codes: 
        500 - Invalid request
        404, 200
    """
    # Update User using PUT
    def update_rider(self, userID, username, emailAddress, password):
        try:
            sql = "UPDATE student SET first_name=%s, last_name=%s, class=%s, age=%s, address=%s WHERE id=%s"
            data = []

            isDirty = False
            if (username is not None):
                sql += "USER_NAME = %s "
                data.append(username)
                isDirty = True
            if (password is not None):
                sql+= "PASSWORD = %s "
                data.append(password)
                isDirty = True
            if (emailAddress is not None):
                sql+= "EMAIL_ADDRESS = %s "
                data.append(emailAddress)
                isDirty = True

            if (isDirty):
                sql += "WHERE USER_ID = %s"
                data.append(userID)
                data = tuple(data)

                conn = mysql.connect()
                cursor = conn.cursor()
                cursor.execute(sql, data)
                conn.commit()
                results = {"UPDATED": 200}
            else:
                results = {"UPDATED": 500}

            return json.dumps(results)
            #else:
            #    return not_found()
        except Exception as e:
            print(e)         
            

    # Delete record from the Database
    def delete_rider(self, userID):
        try:
            sqlQuery = "DELETE FROM RIDER_DETAILS WHERE USER_ID=%s"
            data = (userID)
            
            executeQuery(sqlQuery, data)
            
            return json.dumps({"STATUS": 200})
        except Exception as e:
            print(e)
            return json.dumps({"STATUS": 404})



    # Delete record from the Database
    def delete_driver(student_id):
        try:
            sqlQuery = "DELETE FROM DRIVER_DETAILS WHERE USER_ID=%s"
            data = (userID)
            
            executeQuery(sqlQuery, data)
            
            return json.dumps({"STATUS": 200})
        except Exception as e:
            print(e)
            return json.dumps({"STATUS": 404})    



    """
    Checks if a driver has accepted the rider's request

    Returns:
    FIND_STATUS = 2 -> RET FOUND
    FIND_STATUS = 1 -> RET REJECTED
    FIND_STATUS = 0 -> RET: NO_CHANGE
    ELSE: ERROR
    """
    #@app.route('/checkFindStatus', methods = ['POST'])
    def checkFindStatus(userID):
        try:
            getFindStatus = "SELECT R.FIND_STATUS, D.USER_ID, D.USER_NAME, CAST(D.LATITUDE AS CHAR(12)) AS SOURCE_LAT, CAST(D.LONGITUDE AS CHAR(12)) AS SOURCE_LONG FROM RIDER_DETAILS R LEFT JOIN DRIVER_DETAILS D ON R.DRIVER_ID = D.USER_ID WHERE R.FIND_STATUS IN (1,2) AND R.USER_ID = %s"
            data = (userID)
            
            rows - executeQuery(getFindStatus, data)

            print("checkFindStatus: rowCount is: ", self.cursor.rowcount)
            if (self.cursor.rowcount > 0):
                if (rows[0][0] == 0):
                    results = {"STATUS" : "NO_CHANGE"}
                elif (rows[0][0] == 1):
                    results = {"STATUS" : "REJECTED"}
                elif (rows[0][0] == 2):
                    results = {"STATUS" : "FOUND", "DRIVER_ID": rows[0][1], "DRIVER_NAME" : rows[0][2], "DRIVER_LAT" : rows[0][3], "DRIVER_LONG": rows[0][4]}
            else:
                return {"STATUS": "ERROR"}
            
            return results

        except Exception as e:
            print(e)
            return {"STATUS": "ERROR"}



    """
    Searches for drivers and sends a request

    returns:
    PAIRED -> If driver has been matched
    NOT_PAIRED -> If drivers available but all declined
    NOT_FOUND -> If drivers not available
    """
    def findDriver(self, userID, latitude, longitude):

        REJECTED_ATTEMPT_LIMIT = 5
        ATTEMPT_LIMIT = 60

        try:
            sendRequest = "UPDATE DRIVER_DETAILS SET RIDER_ID = %s, RIDE_STATUS = 1, DEST_LAT = %s, DEST_LONG = %s WHERE USER_ID = %s AND IS_ACTIVE = 1 AND RIDE_STATUS = 0"

            driverFound = False
            driverPaired = False
            res = {"STATUS" : "NOT_FOUND"}

            searchDriver = '''SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS'''

            #data = (latitude-0.1, latitude+0.1, longitude-0.1, longitude+0.1, latitude, longitude)

            rows = executeQuery(searchDriver)

            for row in rows:
                _driver_id = row[0]
                data = (userID, latitude, longitude, _driver_id);

                rows = executeQuery(sendRequest, tuple(data))

                print("Rowcount is", self.cursor.rowcount)

                if (self.cursor.rowcount == 1):
                    driverFound = True
                    break

            if driverFound:
                attempts = 0
                rejectedAttempts = 0

                while attempts < ATTEMPT_LIMIT and rejectedAttempts < REJECTED_ATTEMPT_LIMIT:
                    res = checkFindStatus(userID)
                    if (res["STATUS"] == "ERROR"):
                        print("findDriver() : checkFindStatus() : STATUS ERROR")
                    elif (res["STATUS"] == "NO_CHANGE"):
                        print("findDriver() : checkFindStatus() : NO_CHANGE")
                    elif (res["STATUS"] == "REJECTED"):
                        print("findDriver() : checkFindStatus() : REJECTED. Attempting again...")
                        rejectedAttempts = rejectedAttempts + 1
                    elif (res["STATUS"] == "FOUND"):
                        print("findDriver() : checkFindStatus() : DRIVER FOUND!")
                        driverPaired = True
                        break
                    else:
                        print("findDriver STATUS UNDEFINED ERROR")
                    
                    attempts = attempts + 1
                    print("attempt no: " + str(attempts))
                    time.sleep(2)

                if driverPaired:
                    res["STATUS"] = "PAIRED"
                else:
                    res["STATUS"] = "NOT_PAIRED"

            else:
                res["STATUS"] = "NOT_FOUND"
            
            #results = {"USER_ID" : rows[0][0], "USER_NAME" : rows[0][1], "LATITUDE": rows[0][2], "LONGITUDE" : rows[0][3], "STATUS": 200}

            return json.dumps(res)
        except Exception as e:
            print(e)
            return json.dumps({"STATUS" : "EXCEPTION"})
    # End of Function



    # Sets driver to ACTIVE
    def setMarkActive(self, userID, activeStatus):
        # This function is called from the driver and marks it active
        try:
            print("user_id", userID, "active_status", activeStatus)

            sql = 'UPDATE DRIVER_DETAILS SET IS_ACTIVE = %s WHERE USER_ID = %s'

            data = []
            data.append(activeStatus)
            data.append(userID)
            
            executeQuery(sql, tuple(data))

            return {"STATUS": 200}

        except Exception as e:
            print(e)
            return json.dumps({"STATUS": 404})
    # End of Function


    # This function is called from the driver and checks Ride Status
    def checkRideStatus(self, userID):
        try:
            sql = '''SELECT D.RIDE_STATUS, D.RIDER_ID, R.USER_NAME, R.LATITUDE AS SOURCE_LAT, R.LONGITUDE AS SOURCE_LONG, D.DEST_LAT, D.DEST_LONG FROM DRIVER_DETAILS D LEFT JOIN RIDER_DETAILS R ON D.RIDER_ID = R.USER_ID WHERE D.RIDE_STATUS = 1 AND D.USER_ID = %s'''

            data = []
            data.append(userID)

            rows = executeQuery(sql, tuple(data))

            results = {}

            # If Ride is not assigned
            if (rows[0][0] == 0):
                results = {"STATUS": 404}

            if (rows[0][0] == 1):
                results = {'STATUS': 200, 
                'RIDER_ID': rows[0][1], 
                'USER_NAME': rows[0][2],
                'SOURCE_LAT': str(rows[0][3]),
                'SOURCE_LONG': str(rows[0][4]),
                'DEST_LAT': str(rows[0][5]),
                'DEST_LONG': str(rows[0][6])
                }

            return json.dumps(results)

        except Exception as e:
            print(e)
            return {"STATUS": 404}      


    # Function that confirms Ride by Driver
    def confirmRideRequest(self,driverID, foundStatus, riderID):

        # This function is called from the driver and confirms the ride request assigned
        try:
            sql = 'UPDATE DRIVER_DETAILS SET RIDE_STATUS = %s WHERE USER_ID = %s'
            data = []

            if (foundStatus == 1):
                data.append(2)
            else:
                data.append(0)

            data.append(driverID)

            # Execute First Query
            executeQuery(sql, tuple(data))

            # Second Query
            sql = 'UPDATE RIDER_DETAILS SET FIND_STATUS = %s, DRIVER_ID = %s WHERE USER_ID = %s'
            data = []
            data.append(foundStatus + 1)
            data.append(driverID)
            data.append(riderID)

            # Execute Second Query
            executeQuery(sql, tuple(data))

            res = {"STATUS": 200}
            return res

        except Exception as e:
            res = {"STATUS": 404}
            print(e)
            return res

    #End of function



    # Save Last Location API URL
    def saveRiderLastLocation(self, userID, latitude, longitude, timestamp):
        try:
            # SQL Query to Update
            sql = 'UPDATE RIDER_DETAILS SET LATITUDE = %s, LONGITUDE = %s, TIMESTAMP = CURRENT_TIME() WHERE USER_ID = %s'

            # Appending Placeholder Data
            data = []
            data.append(latitude)
            data.append(longitude)
            data.append(userID)

            executeQuery(sql, tuple(data))

            return {"STATUS": 200}

        except Exception as e:
            print(e)
            return {"STATUS": 404}      


    # Save Last Location API URL
    def saveDriverLastLocation(self, userID, latitude, longitude, timestamp):
        try:
            # SQL Query to Update
            sql = 'UPDATE DRIVER_DETAILS SET LATITUDE = %s, LONGITUDE = %s, TIMESTAMP = CURRENT_TIME() WHERE USER_ID = %s'

            # Appending Placeholder Data
            data = []
            data.append(latitude)
            data.append(longitude)
            data.append(userID)

            executeQuery(sql, tuple(data))

            return {"STATUS": 200}

        except Exception as e:
            print(e)
            return {"STATUS": 404}         
    

    # Get User Location
    def getUserLocations():

        try:
            sql = "SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE, TIMESTAMP FROM RIDER_DETAILS"
            rows = executeQuery(sql, ())

            res = jsonify(rows)
            return res

        except Exception as e:
            print(e)
            return json.dumps({"STATUS": 404})
    # End of Function


    # Get User Location
    def getDriverLocations():
        try:
            sql = "SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE, TIMESTAMP FROM DRIVER_DETAILS"
            rows = executeQuery(sql, ())

            return json.dumps(rows)

        except Exception as e:
            print(e)
            return json.dumps({"STATUS": 404})
    # End of Function
