"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""

from Singleton import Singleton
import json
from flask import jsonify
import pymysql
from flask import jsonify
from config import mysql
import time
from hashlib import sha256
from Rider import *
from Driver import *

@singleton
class OracleDBHandler(DBHandler):
	def __init__(self):
		self.conn = mysql.connect()
		self.cursor = self.conn.cursor()

	def executeQuery(self, sqlQuery, data):
		self.cursor.execute(sqlQuery, data)
		self.conn.commit()
		return self.cursor.fetchall()

	# ------------------------------------------------------------------------
	# Main SQL Functions

	# Adding Rider
	def addRider(self, rider):
		# Performing SQL Injection
		try:
			sqlQuery = "INSERT INTO `RIDER_DETAILS` (`USER_ID`, `USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (%s, %s, %s, %s)"
			data = (rider.getUserID(), rider.getUserName(), sha256(rider.getPassword().encode()).hexdigest(), rider.getEmailAddress())
			
			self.executeQuery(sqlQuery, data)

			return True

		except Exception as e:
			print(e)
			return None

	# End of function : Add Rider

	# create Driver		
	def addDriver(self, driver):

		# Performing SQL Injection
		try:            
			# insert record in database
			sqlQuery = "INSERT INTO `DRIVER_DETAILS` (`USER_ID`, `USER_NAME`, `PASSWORD`, `EMAIL_ADDRESS`) VALUES (%s, %s, %s, %s)"
			data = (driver.getUserID(), driver.getUserName(), sha256(driver.getPassword().encode()).hexdigest(), driver.getEmailAddress())
			
			self.executeQuery(sqlQuery, data)

			return True

		except Exception as e:
			print(e)
			return None

	# End of function: add Driver

	# Login User
	def loginRider(self, rider):
		try:
			sqlQuery = "SELECT USER_ID FROM RIDER_DETAILS WHERE EMAIL_ADDRESS = %s AND PASSWORD = %s"
			data = (rider.getUserName(), sha256(rider.getPassword().encode()).hexdigest())

			rows = self.executeQuery(sqlQuery, data)
			
		except Exception as e:
			print(e)
			return None       
	# End of function: loginRider

	# Login User
	def loginDriver(self, driver):
		try:
			sqlQuery = "SELECT USER_ID FROM DRIVER_DETAILS WHERE EMAIL_ADDRESS = %s AND PASSWORD = %s"
			data = (driver.getUserName(), sha256(driver.getPassword().encode()).hexdigest())

			rows = self.executeQuery(sqlQuery, data)
			return rows
		except Exception as e:
			print(e)
			return None

	# End of function: loginDriver
		
	# Get Method for User Details
	def riderDetails(self):
		try:
			sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM RIDER_DETAILS"
			
			rows = self.executeQuery(sqlQuery, ())
			return rows
		except Exception as e:
			print(e)
			return None

	# End of function: riderDetails
			
			

	# Get Method for User Details
	def driverDetails(self):
		try:
			sqlQuery = "SELECT USER_ID, USER_NAME, PASSWORD, EMAIL_ADDRESS FROM DRIVER_DETAILS"
			rows = executeQuery(sqlQuery, ())
			return rows

		except Exception as e:
			print(e)
			return None

	# End of function: driverDetails()
		

	# Load Rider Data
	def riderData(self, rider):
		try:
			sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM RIDER_DETAILS WHERE USER_ID = %s"
			data = (rider.getUserID())

			rows = executeQuery(sqlQuery, data)
			return rows

		except Exception as e:
			print(e)
			return None
	# End of function: riderData


	# Load Driver Data
	def driverData(self, driver):
		try:
			sqlQuery = "SELECT USER_NAME, EMAIL_ADDRESS FROM DRIVER_DETAILS WHERE USER_ID = %s"
			data = (driver.getUserID())

			rows = executeQuery(sqlQuery, data)
			return rows

		except Exception as e:
			print(e)
			return None
	
	# End of function: driverData

	# Update User using PUT
	# def updateRider(self, rider):
	# 	try:
	# 		sql = "UPDATE student SET first_name=%s, last_name=%s, class=%s, age=%s, address=%s WHERE id=%s"
	# 		data = []

	# 		isDirty = False
	# 		if (rider.getUserName() is not None):
	# 			sql += "USER_NAME = %s "
	# 			data.append(username)
	# 			isDirty = True
				
	# 		if (password is not None):
	# 			sql+= "PASSWORD = %s "
	# 			data.append(password)
	# 			isDirty = True
	# 		if (emailAddress is not None):
	# 			sql+= "EMAIL_ADDRESS = %s "
	# 			data.append(emailAddress)
	# 			isDirty = True

	# 		if (isDirty):
	# 			sql += "WHERE USER_ID = %s"
	# 			data.append(userID)
	# 			data = tuple(data)

	# 			conn = mysql.connect()
	# 			cursor = conn.cursor()
	# 			cursor.execute(sql, data)
	# 			conn.commit()
	# 			results = {"UPDATED": 200}
	# 		else:
	# 			results = {"UPDATED": 500}

	# 		return json.dumps(results)
	# 		#else:
	# 		#    return not_found()
	# 	except Exception as e:
	# 		print(e)         
			

	# Delete record from the Database
	def deleteRider(self, rider):
		try:
			sqlQuery = "DELETE FROM RIDER_DETAILS WHERE USER_ID=%s"
			data = (rider.getUserID())
			
			executeQuery(sqlQuery, data)
			
			return True

		except Exception as e:
			print(e)
			return None
	# End of function: deleteRider

	# Delete record from the Database
	def deleteDriver(self, driverID):
		try:
			sqlQuery = "DELETE FROM DRIVER_DETAILS WHERE USER_ID=%s"
			data = (driverID)
			
			executeQuery(sqlQuery, data)
			
			return True
		except Exception as e:
			print(e)
			return None  
	
	# End of function: deleteDriver



	"""
	Checks if a driver has accepted the rider's request
	Returns:
	FIND_STATUS = 2 -> RET FOUND
	FIND_STATUS = 1 -> RET REJECTED
	FIND_STATUS = 0 -> RET: NO_CHANGE
	ELSE: ERROR
	"""
	def checkFindStatus(self, rider):
		try:
			getFindStatus = "SELECT R.FIND_STATUS, D.USER_ID, D.USER_NAME, CAST(D.LATITUDE AS CHAR(12)) AS SOURCE_LAT, CAST(D.LONGITUDE AS CHAR(12)) AS SOURCE_LONG FROM RIDER_DETAILS R LEFT JOIN DRIVER_DETAILS D ON R.DRIVER_ID = D.USER_ID WHERE R.FIND_STATUS IN (1,2) AND R.USER_ID = %s"
			data = (rider.getUserID())
			
			rows = executeQuery(getFindStatus, data)
			return rows

		except Exception as e:
			print(e)
			return None

	# End of functon: checkFindStatus


	
	# Send ride request
	def sendRideRequest(self, ride):
		try:
			sendRequest = "UPDATE DRIVER_DETAILS SET RIDER_ID = %s, RIDE_STATUS = 1, DEST_LAT = %s, DEST_LONG = %s WHERE USER_ID = %s AND IS_ACTIVE = 1 AND RIDE_STATUS = 0"
			return executeQuery(sendRequest)

		except Exception as e:
			print(e)
			return None

	# End of Function
	
	# Find available drivers
	def searchDriverForRide(self):
		try:
			searchDriver = '''SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE FROM DRIVER_DETAILS'''
			return executeQuery(searchDriver)
			
		except Exception as e:
			print(e)
			return None
		
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

			return True
			return {"STATUS": 200}

		except Exception as e:
			print(e)
			return None
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
	def getUserLocations(self):

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
	def getDriverLocations(self):
		try:
			sql = "SELECT USER_ID, USER_NAME, CAST(LATITUDE AS CHAR(12)) AS LATITUDE, CAST(LONGITUDE AS CHAR(12)) AS LONGITUDE, TIMESTAMP FROM DRIVER_DETAILS"
			rows = executeQuery(sql, ())

			return json.dumps(rows)

		except Exception as e:
			print(e)
			return json.dumps({"STATUS": 404})
	# End of Function