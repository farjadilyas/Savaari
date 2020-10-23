  
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
        res = jsonify('Student created successfully.')
        res.status_code = 200

        return res
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
        res = jsonify('Student created successfully.')
        res.status_code = 200

        #return redirect('http://localhost:5000/user_details', 302)

        return res
    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()

# Login User
@app.route('/login_rider', methods=['POST', 'GET'])
def login():
    try:
        _json = request.json
        _username = _json['username']
        _password = _json['password']
        
        # insert record in database
        sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE (USER_NAME = %s OR EMAIL_ADDRESS = %s) AND PASSWORD = %s"
        data = (_username, _username, sha256(_password.encode()).hexdigest())

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
def student():
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


# Load User Dat
@app.route('/rider_data', methods=['POST', 'GET'])
def user_data():
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


# Save Last Location API URL
@app.route('/saveRiderLocation', methods=['POST'])
def saveUserLastLocation():
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
    app.run(host = '0.0.0.0', port = 5000)
