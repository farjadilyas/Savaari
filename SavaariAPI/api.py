"""
    Savaari Application Programming Interface
    Nabeel Danish               18I-0579
    Muhammad Farjad Ilyas       18I-0436
"""

import pymysql
import json
from app import app
from config import mysql
from flask import jsonify
from flask import flash, request, redirect, url_for
from hashlib import sha256

import time

# create User			
@app.route('/add_rider', methods=['POST'])
def add_rider():
    try:
        _json = request.json
        _email_address = _json['email_address']
        _username = _json['username']
        _password = _json['password']

        print(_email_address)

        
        # insert record in database
        sqlQuery = "INSERT INTO `RIDER_DETAILS` (`USER_ID`, `USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (%s, %s, %s, %s)"
        data = (0, _username, sha256(_password.encode()).hexdigest(), _email_address)
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(sqlQuery, data)
        conn.commit()
        res = {"STATUS_CODE" : 200}

        return json.dumps(res)
    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()

# create Driver		
@app.route('/add_driver', methods=['POST'])
def add_driver():
    try:
        _json = request.json
        _email_address = _json['email_address']
        _username = _json['username']
        _password = _json['password']

        print(_email_address)

        
        # insert record in database
        sqlQuery = "INSERT INTO `DRIVER_DETAILS` (`USER_ID`, `USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (%s, %s, %s, %s)"
        data = (0, _username, sha256(_password.encode()).hexdigest(), _email_address)
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(sqlQuery, data)
        conn.commit()

        res = {"STATUS_CODE" : 200}

        return json.dumps(res)
    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()

# Login User
@app.route('/login_rider', methods=['POST', 'GET'])
def login_rider():
    try:
        _json = request.get_json()
        _username = _json['username']
        _password = _json['password']
        
        # insert record in database
        sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE EMAIL_ADDRESS = %s AND PASSWORD = %s"
        data = (_username, sha256(_password.encode()).hexdigest())

        conn = mysql.connect()

        cursor = conn.cursor()
        cursor.execute(sqlQuery, data)

        rows = cursor.fetchall()

        if (cursor.rowcount == 0):
            results = {"USER_ID": -1, "STATUS_CODE": 200}
        else:
            res = jsonify(rows)
            res.status_code = 200
            results = {"USER_ID" : rows[0][0], "STATUS_CODE" : 200}
        
        results = json.dumps(results)
        print(results)

        #return redirect('http://localhost:5000/user_details', 302)

        return results
    except Exception as e:
        print(e)
        results = {"USER_ID": -1, "STATUS_CODE": 404}
        return json.dumps(results)
    finally:
        cursor.close() 
        conn.close()


# Login User
@app.route('/login_driver', methods=['POST', 'GET'])
def login_driver():
    try:
        _json = request.json
        _username = _json['username']
        _password = _json['password']
        
        # insert record in database
        sqlQuery = "SELECT USER_ID FROM DRIVER_DETAILS WHERE EMAIL_ADDRESS = %s AND PASSWORD = %s"
        data = (_username, sha256(_password.encode()).hexdigest())

        conn = mysql.connect()

        cursor = conn.cursor()
        cursor.execute(sqlQuery, data)

        rows = cursor.fetchall()

        if (cursor.rowcount == 0):
            results = {"USER_ID": -1, "STATUS_CODE": 200}
        else:
            res = jsonify(rows)
            res.status_code = 200
            results = {"USER_ID" : rows[0][0], "STATUS_CODE" : 200}
        
        results = json.dumps(results)
        print(results)

        #return redirect('http://localhost:5000/user_details', 302)

        return results
    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()

# Get Method for User Details
@app.route('/rider_details')
def rider_details():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM RIDER_DETAILS")
        rows = cursor.fetchall()

        res = jsonify(rows)
        #res.status_code = 200

        return res
    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()


# Get Method for User Details
@app.route('/driver_details')
def driver_details():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM DRIVER_DETAILS")
        rows = cursor.fetchall()

        res = jsonify(rows)
        #res.status_code = 200

        return res
    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()


# Load User Dat
@app.route('/rider_data', methods=['POST', 'GET'])
def rider_data():
    try:
        _json = request.get_json()
        _user_id = _json['USER_ID']
        
        # insert record in database
        sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM RIDER_DETAILS WHERE USER_ID = %s"
        data = (_user_id)

        conn = mysql.connect()

        cursor = conn.cursor()
        cursor.execute(sqlQuery, data)

        rows = cursor.fetchall()

        results = {"USER_NAME": rows[0][0], "EMAIL_ADDRESS" : rows[0][1]}
        results = json.dumps(results)

        return results

    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()


# Load User Dat
@app.route('/driver_data', methods=['POST', 'GET'])
def driver_data():
    try:
        _json = request.get_json()
        _user_id = _json['USER_ID']
        
        # insert record in database
        sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM DRIVER_DETAILS WHERE USER_ID = %s"
        data = (_user_id)

        conn = mysql.connect()

        cursor = conn.cursor()
        cursor.execute(sqlQuery, data)

        rows = cursor.fetchall()

        results = {"USER_NAME": rows[0][0], "EMAIL_ADDRESS" : rows[0][1]}
        results = json.dumps(results)

        return results

    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()




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
    finally:
        cursor.close() 
        conn.close()
"""
"""
Return codes: 
    500 - Invalid request
    404, 200
"""
# Update User using PUT
@app.route('/update_rider', methods=['PUT'])
def update_rider():
    try:
        _json = request.get_json()
        _username = _json.get("username")
        _password = _json.get("password")
        _email_address = _json.get("email_address")
        _user_id = _json.get("user_id");

        # update record in database
        sql = "UPDATE student SET first_name=%s, last_name=%s, class=%s, age=%s, address=%s WHERE id=%s"
        data = []

        isDirty = False
        if (_username is not None):
            sql += "USER_NAME = %s "
            data.append(_username)
            isDirty = True
        if (_password is not None):
            sql+= "PASSWORD = %s "
            data.append(_password)
            isDirty = True
        if (_email_address is not None):
            sql+= "EMAIL_ADDRESS = %s "
            data.append(_email_address)
            isDirty = True

        if (isDirty):
            sql += "WHERE USER_ID = %s"
            data.append(_user_id)
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
    finally:
        cursor.close() 
        conn.close()

# Delete record from the Database
@app.route('/delete_rider', methods=['POST', 'DELETE'])
def delete_rider(student_id):
    try:
        _json = request.json
        _userID = _json.get('userID')
        _email_address = _json.get('emailAddress')
        _username = _json.get('username')
        
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute("DELETE FROM RIDER_DETAILS WHERE USER_ID=%s", (student_id,))
        conn.commit()
        res = jsonify('Student deleted successfully.')
        res.status_code = 200
        return res

    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()


# Delete record from the Database
@app.route('/delete_driver', methods=['POST', 'DELETE'])
def delete_driver(student_id):
    try:
        _json = request.json
        _userID = _json.get('userID')
        _email_address = _json.get('emailAddress')
        _username = _json.get('username')
        
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute("DELETE FROM DRIVER_DETAILS WHERE USER_ID=%s", (student_id,))
        conn.commit()
        res = jsonify('Student deleted successfully.')
        res.status_code = 200
        return res

    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()



"""
Checks if a driver has accepted the rider's request

Returns:
FIND_STATUS = 2 -> RET FOUND
FIND_STATUS = 1 -> RET REJECTED
FIND_STATUS = 0 -> RET: NO_CHANGE
ELSE: ERROR
"""
#@app.route('/checkFindStatus', methods = ['POST'])
def checkFindStatus(_user_id):
    try:
        getFindStatus = "SELECT R.FIND_STATUS, D.USER_ID, D.USER_NAME, CAST(D.LATITUDE AS CHAR(12)) AS SOURCE_LAT, CAST(D.LONGITUDE AS CHAR(12)) AS SOURCE_LONG FROM RIDER_DETAILS R LEFT JOIN DRIVER_DETAILS D ON R.DRIVER_ID = D.USER_ID WHERE R.FIND_STATUS IN (1,2) AND R.USER_ID = %s"

        data = (_user_id)

        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(getFindStatus, data)
        rows = cursor.fetchall()
        conn.commit()

        print("checkFindStatus: rowCount is: ", cursor.rowcount)
        if (cursor.rowcount > 0):
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

    finally:
        cursor.close() 
        conn.close()




"""
Searches for drivers and sends a request

returns:
PAIRED -> If driver has been matched
NOT_PAIRED -> If drivers available but all declined
NOT_FOUND -> If drivers not available
"""
@app.route('/findDriver', methods=['POST'])
def findDriver():

    REJECTED_ATTEMPT_LIMIT = 5
    ATTEMPT_LIMIT = 60

    try:
        # Receiving Request Params
        _json = request.get_json()

        _user_id = _json['USER_ID']
        _latitude = _json['LATITUDE']
        _longitude = _json['LONGITUDE']

        sendRequest = "UPDATE DRIVER_DETAILS SET RIDER_ID = %s, RIDE_STATUS = 1, DEST_LAT = %s, DEST_LONG = %s WHERE USER_ID = %s AND IS_ACTIVE = 1 AND RIDE_STATUS = 0"

        driverFound = False
        driverPaired = False
        res = {"STATUS" : "NOT_FOUND"}

        searchDriver = '''SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS'''

        #data = (_latitude-0.1, _latitude+0.1, _longitude-0.1, _longitude+0.1, _latitude, _longitude)

        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(searchDriver)
        rows = cursor.fetchall()
        conn.commit()

        for row in rows:
            _driver_id = row[0]
            data = (_user_id, _latitude, _longitude, _driver_id);

            conn = mysql.connect()
            cursor = conn.cursor()
            cursor.execute(sendRequest, tuple(data))
            cursor.fetchall()
            conn.commit()

            print("Rowcount is", cursor.rowcount)

            if (cursor.rowcount == 1):
                driverFound = True
                break

        if (driverFound):
            attempts = 0
            rejectedAttempts = 0

            while (attempts < ATTEMPT_LIMIT and rejectedAttempts < REJECTED_ATTEMPT_LIMIT):
                res = checkFindStatus(_user_id)
                if (res["STATUS"] == "ERROR"):
                    print("findDriver() : checkFindStatus() : STATUS ERROR")
                elif (res["STATUS"] == "NO_CHANGE"):
                    print("findDriver() : checkFindStatus() : NO_CHANGE")
                elif (res["STATUS"] == "REJECTED"):
                    print("findDriver() : checkFindStatus() : REJECTED. Attempting again...")
                    ++rejectedAttempts
                elif (res["STATUS"] == "FOUND"):
                    print("findDriver() : checkFindStatus() : DRIVER FOUND!")
                    driverPaired = True
                    break
                else:
                    print("findDriver STATUS UNDEFINED ERROR")

                ++attempts
                time.sleep(2)

            if (driverPaired):
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

    finally:
        cursor.close()
        conn.close()

# End of Function



# Sets driver to ACTIVE
@app.route('/findRider', methods=['POST'])
def findRider():
    # This function is called from the driver and marks it active
    try:
        _json = request.json
        _user_id = _json['USER_ID']
        _active_status = _json['ACTIVE_STATUS']

        print("user_id", _user_id, "active_status", _active_status)

        sql = 'UPDATE DRIVER_DETAILS SET IS_ACTIVE = %s WHERE USER_ID = %s'

        data = []
        data.append(_active_status)
        data.append(_user_id)

        # Conection to MYSQL
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(sql, tuple(data))
        conn.commit()

        res = {"STATUS": 200}
        return res

    except Exception as e:
        print(e)
        return json.dumps({"STATUS": 404})

    finally:
        cursor.close() 
        conn.close()

# End of Function



@app.route('/checkRideStatus', methods = ['POST'])
def checkRideStatus():

	# This function is called from the driver and checks Ride Status
	try:
		_json = request.get_json()
		_user_id = _json['USER_ID']

		sql = '''SELECT D.RIDE_STATUS, D.RIDER_ID, R.USER_NAME, R.LATITUDE AS SOURCE_LAT, R.LONGITUDE AS SOURCE_LONG, D.DEST_LAT, D.DEST_LONG FROM DRIVER_DETAILS D LEFT JOIN RIDER_DETAILS R ON D.RIDER_ID = R.USER_ID WHERE D.RIDE_STATUS = 1 AND D.USER_ID = %s'''

		data = []
		data.append(_user_id)

		# Conection to MYSQL
		conn = mysql.connect()
		cursor = conn.cursor()
		cursor.execute(sql, tuple(data))

		rows = cursor.fetchall();

		# If Ride is not assigned
		if (rows[0][0] == 0):
			res = {"STATUS": 404}
			return res

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
		res = {"STATUS": 404}
		return res

	finally:
		cursor.close() 
		conn.close()


# Function that confirms Ride by Driver
@app.route('/confirmRideRequest', methods=['POST'])
def confirmRideRequest():

    # This function is called from the driver and confirms the ride request assigned
    try:
        _json = request.get_json()

        _driver_id = _json['USER_ID']
        _found_status = _json['FOUND_STATUS']
        _rider_id = _json['RIDER_ID']

        sql = 'UPDATE DRIVER_DETAILS SET RIDE_STATUS = %s WHERE USER_ID = %s'

        data = []

        if (_found_status == 1):
            data.append(2)
        else:
            data.append(0)

        data.append(_driver_id)

        # Conection to MYSQL
        conn = mysql.connect()
        cursor = conn.cursor()

        # First Query
        cursor.execute(sql, tuple(data))

        # Second Query
        sql = 'UPDATE RIDER_DETAILS SET FIND_STATUS = %s, DRIVER_ID = %s WHERE USER_ID = %s'
        data = []
        data.append(_found_status + 1)
        data.append(_driver_id)
        data.append(_rider_id)

        cursor.execute(sql, tuple(data))

        # Final Commit
        conn.commit()

        res = {"STATUS": 200}
        return res

    except Exception as e:
        res = {"STATUS": 404}
        print(e)
        return res

    finally:
        cursor.close() 
        conn.close()
#End of function



# Save Last Location API URL
@app.route('/saveRiderLocation', methods=['POST'])
def saveRiderLastLocation():
    try:
        # Receiving Request Params
        _json = request.get_json()

        _user_id = _json['USER_ID']
        _latitude = _json['LATITUDE']
        _longitude = _json['LONGITUDE']
        _timestamp = _json['TIMESTAMP']

        # SQL Query to Update
        sql = 'UPDATE RIDER_DETAILS SET LATITUDE = %s, LONGITUDE = %s, TIMESTAMP = CURRENT_TIME() WHERE USER_ID = %s'

        # Appending Placeholder Data
        data = []
        data.append(_latitude)
        data.append(_longitude)
        #data.append(_timestamp)
        data.append(_user_id)

        # Conection to MYSQL
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(sql, tuple(data))
        conn.commit()

        res = {"STATUS": 200}

        return res

    except Exception as e:
        print(e)

    finally:
        cursor.close()
        conn.close()


# Save Last Location API URL
@app.route('/saveDriverLocation', methods=['POST'])
def saveDriverLastLocation():
    try:
        # Receiving Request Params
        _json = request.get_json()

        _user_id = _json['USER_ID']
        _latitude = _json['LATITUDE']
        _longitude = _json['LONGITUDE']
        _timestamp = _json['TIMESTAMP']

        # SQL Query to Update
        sql = 'UPDATE DRIVER_DETAILS SET LATITUDE = %s, LONGITUDE = %s, TIMESTAMP = CURRENT_TIME() WHERE USER_ID = %s'

        # Appending Placeholder Data
        data = []
        data.append(_latitude)
        data.append(_longitude)
        #data.append(_timestamp)
        data.append(_user_id)

        # Conection to MYSQL
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(sql, tuple(data))
        conn.commit()

        res = {"STATUS": 200}

        return res

    except Exception as e:
        print(e)

    finally:
        cursor.close()
        conn.close()

# Get User Location
@app.route('/getRiderLocations', methods=['POST'])
def getUserLocations():

    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE, TIMESTAMP FROM RIDER_DETAILS")
        rows = cursor.fetchall()

        res = jsonify(rows)
        return res

    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()

# End of Function


# Get User Location
@app.route('/getDriverLocations', methods=['POST'])
def getDriverLocations():

    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE, TIMESTAMP FROM DRIVER_DETAILS")
        rows = cursor.fetchall()

        res = jsonify(rows)
        return res

    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()

# End of Function
        
# Error Handler
@app.errorhandler(404)
def not_found(error=None):
    message = {
        'status': 404,
        'message': 'There is no record: ' + request.url,
    }
    res = jsonify(message)
    res.status_code = 404

    return res

# Running the Main App
if __name__ == "__main__":
    app.run(host = '0.0.0.0', port = 4000)
