"""
    Savaari Application Programming Interface
    Nabeel Danish               18I-0579
    Muhammad Farjad Ilyas       18I-0436
"""

import pymysql
import json
from app import app
from Controller import Controller
from config import mysql
from flask import jsonify
from flask import flash, request, redirect, url_for
from hashlib import sha256
import time

controller = Controller()

# create User			
@app.route('/add_rider', methods=['POST'])
def add_rider():
    _json = request.json
    _email_address = _json['email_address']
    _username = _json['username']
    _password = _json['password']

    return controller.add_rider(_username, _email_address, _password)

# create Driver		
@app.route('/add_driver', methods=['POST'])
def add_driver():
    _json = request.json
    _email_address = _json['email_address']
    _username = _json['username']
    _password = _json['password']

    return controller.add_driver(_username, _email_address, _password)

# Login User
@app.route('/login_rider', methods=['POST', 'GET'])
def login_rider():
    _json = request.get_json()
    _username = _json['username']
    _password = _json['password']
    
    controller.login_rider(_username, _password)


# Login User
@app.route('/login_driver', methods=['POST', 'GET'])
def login_driver():
        _json = request.json
        _username = _json['username']
        _password = _json['password']
        
        controller.login_driver(_username, _password)

# Get Method for User Details
@app.route('/rider_details')
def rider_details():
    return controller.rider_details()


# Get Method for User Details
@app.route('/driver_details')
def driver_details():
    return controller.driver_details()


# Load User Dat
@app.route('/rider_data', methods=['POST', 'GET'])
def rider_data():
    _json = request.get_json()
    _user_id = _json['USER_ID']

    return controller.rider_data(_user_id)
        


# Load User Dat
@app.route('/driver_data', methods=['POST', 'GET'])
def driver_data():
    _json = request.get_json()
    _user_id = _json['USER_ID']
    
    return controller.driver_data(_user_id)


"""
Return codes: 
    500 - Invalid request
    404, 200
"""
# Update User using PUT
@app.route('/update_rider', methods=['PUT'])
def update_rider():
    _json = request.get_json()
    _username = _json.get("username")
    _password = _json.get("password")
    _email_address = _json.get("email_address")
    _user_id = _json.get("user_id");

    controller.update_rider(_user_id, _username, _email_address, _password)



# Delete record from the Database
@app.route('/delete_rider', methods=['POST', 'DELETE'])
def delete_rider(student_id):
    _json = request.json
    _userID = _json.get('USER_ID')
    
    return controller.delete_rider(_userID)


# Delete record from the Database
@app.route('/delete_driver', methods=['POST', 'DELETE'])
def delete_driver(student_id):
    _json = request.json
    _userID = _json.get('USER_ID')
    
    return controller.delete_driver(_userID)



"""
Searches for drivers and sends a request

returns:
PAIRED -> If driver has been matched
NOT_PAIRED -> If drivers available but all declined
NOT_FOUND -> If drivers not available
"""
@app.route('/findDriver', methods=['POST'])
def findDriver():

    # Receiving Request Params
    _json = request.get_json()

    _user_id = _json['USER_ID']
    _latitude = _json['LATITUDE']
    _longitude = _json['LONGITUDE']
    
    return controller.findDriver(_user_id, _latitude, _longitude)
# End of Function



# Sets driver to ACTIVE
@app.route('/setMarkActive', methods=['POST'])
def setMarkActive():
    # This function is called from the driver and marks it active
    _json = request.json
    _user_id = _json['USER_ID']
    _active_status = _json['ACTIVE_STATUS']
    
    return controller.setMarkActive(_user_id, _active_status)
# End of Function


# This function is called from the driver and checks Ride Status
@app.route('/checkRideStatus', methods = ['POST'])
def checkRideStatus():
    json = request.get_json()
	_user_id = _json['USER_ID']
	
    return controller.checkRideStatus(_user_id)


# This function is called from the driver and confirms the ride request assigned
@app.route('/confirmRideRequest', methods=['POST'])
def confirmRideRequest():
     _json = request.get_json()

    _driver_id = _json['USER_ID']
    _found_status = _json['FOUND_STATUS']
    _rider_id = _json['RIDER_ID']
    
    return controller.confirmRideRequest(_driver_id, _found_status, _rider_id)
#End of function



# Save Last Location API URL
@app.route('/saveRiderLocation', methods=['POST'])
def saveRiderLastLocation():
    _json = request.get_json()

    _user_id = _json['USER_ID']
    _latitude = _json['LATITUDE']
    _longitude = _json['LONGITUDE']
    _timestamp = _json['TIMESTAMP']

    return controller.saveRiderLastLocation(_user_id, _latitude, _longitude, _timestamp)


# Save Last Location API URL
@app.route('/saveDriverLocation', methods=['POST'])
def saveDriverLastLocation():
    _json = request.get_json()

    _user_id = _json['USER_ID']
    _latitude = _json['LATITUDE']
    _longitude = _json['LONGITUDE']
    _timestamp = _json['TIMESTAMP']

    return controller.saveDriverLastLocation(_user_id, _latitude, _longitude, _timestamp)

# Get User Location
@app.route('/getRiderLocations', methods=['POST'])
def getUserLocations():
    return controller.getUserLocations()


# Get User Location
@app.route('/getDriverLocations', methods=['POST'])
def getDriverLocations():
    return controller.getDriverLocations()

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
