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



# create Student			
@app.route('/add_user', methods=['POST'])
def create_student():
    try:
        _json = request.json
        _email_address = _json['email_address']
        _username = _json['username']
        _password = _json['password']

        print(_email_address)

        
        # insert record in database
        sqlQuery = "INSERT INTO `user_details` (`USER_ID`, `USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (%s, %s, %s, %s)"
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


#signin
@app.route('/login', methods=['POST', 'GET'])
def login():
    try:
        _json = request.json
        _username = _json['username']
        _password = _json['password']
        
        # insert record in database
        sqlQuery = "SELECT USER_ID FROM USER_DETAILS WHERE (USER_NAME = %s OR EMAIL_ADDRESS = %s) AND PASSWORD = %s"
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

@app.route('/user_details')
def student():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM user_details")
        rows = cursor.fetchall()

        res = jsonify(rows)
        res.status_code = 200

        return res
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
@app.route('/update', methods=['PUT'])
def update_student():
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

# delete student record from database
# 



@app.route('/delete', methods=['POST', 'DELETE'])
def delete_student(student_id):
    try:
        _json = request.json
        _userID = _json.get('userID')
        _email_address = _json.get('emailAddress')
        _username = _json.get('username')
        
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute("DELETE FROM USER_DETAILS WHERE USER_ID=%s", (student_id,))
        conn.commit()
        res = jsonify('Student deleted successfully.')
        res.status_code = 200
        return res

    except Exception as e:
        print(e)
    finally:
        cursor.close() 
        conn.close()
        
@app.errorhandler(404)
def not_found(error=None):
    message = {
        'status': 404,
        'message': 'There is no record: ' + request.url,
    }
    res = jsonify(message)
    res.status_code = 404

    return res

if __name__ == "__main__":
    app.run(host = '0.0.0.0', port = 5000)	
