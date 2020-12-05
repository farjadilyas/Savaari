"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""

from DBHandlerFactory import DBHandlerFactory
from flask import jsonify
from Rider import *
from Driver import *
from DBHandler import DBHandler
import json

# =========================================================================
# 						 	CONTROLLER CLASS
# =========================================================================
class Controller(object):

	# Constructors
	def __init__(self, *args, **kwargs):
		super(Controller, self).__init__(*args, **kwargs)

		# Main Attributes
		self.__dbHandler = DBHandlerFactory.getDBHandler()
		self.__lastUserID = 1

	# End of __init__

	# -------------------------------------------------------------------
	# 				Methods called from API (System Calls)
	# -------------------------------------------------------------------

	# Sign-up Rider
	def addRider(self, username, email_address, password):

		rider = Rider(user_id = self.__lastUserID, username = username, password = password, email_address = email_address)
		self.__lastUserID += 1

		queryResult = self.__dbHandler.add_raddRiderider(rider)

		# Formating Output
		if queryResult is not None:
			return json.dumps({"STATUS_CODE" : 200})
		else:
			return json.dumps({"STATUS_CODE": 404})

	# End of Function: Sign-up Rider

	# Sign-up Driver
	def addDriver(self, username, email_address, password):

		driver = Driver(user_id = self.__lastUserID, username = username, password = password, email_address = email_address)
		self.__lastUserID+=1

		if self.__dbHandler.addDriver(driver) is not None:
			return json.dumps({"STATUS_CODE" : 200})
		else:
			return json.sumps({"STATUS_CODE": 404})

	# End of Function: Sign-up Driver

	# Login Rider
	def loginRider(self, username, password):

		rider = Rider(username = username, password = password)
		rows = self.__dbHandler.login_rider(rider)

		if rows is not None:
			# Response Logic
			if (len(rows) == 0):
				results = {"USER_ID": -1, "STATUS_CODE": 200}
			else:
				res = jsonify(rows)
				res.status_code = 200
				results = {"USER_ID" : rows[0][0], "STATUS_CODE" : 200}
			
			results = json.dumps(results)
			print(results)

			return results
		else:
			return json.dumps({"USER_ID": -1, "STATUS_CODE": 404})

	# End of Function: Login Rider

	# Login Driver
	def loginDriver(self, username, password):

		driver = Driver(username = username, password = password)
		rows = self.__dbHandler.login_driver(driver)

		if rows is not None:
			# Response Logic
			if (len(rows) == 0):
				results = {"USER_ID": -1, "STATUS_CODE": 200}
			else:
				res = jsonify(rows)
				res.status_code = 200
				results = {"USER_ID" : rows[0][0], "STATUS_CODE" : 200}
			
			results = json.dumps(results)
			print(results)

			return results
		else:
			return json.dumps({"USER_ID": -1, "STATUS_CODE": 404})

	# End of Function: Login Driver

	# Rider Details
	def riderDetails(self):
		rows =  self.__dbHandler.riderDetails()
		if rows is not None:
			return json.dumps(rows)
		else:
			return json.dumps({"STATUS": 404})
	
	# End of function: riderDetails

	# Driver Details
	def driverDetails(self):
		rows =  self.__dbHandler.driverDetails()
		if rows is not None:
			return json.dumps(rows)
		else:
			return json.dumps({"STATUS": 404})
	
	# End of function: driverDetails

	# Rider Data
	def riderData(self, user_id):

		rider = Rider(user_id = user_id)
		rows = self.__dbHandler.riderData(rider)

		if rows is not None:
			results = {"STATUS": 200, "USER_NAME": rows[0][0], "EMAIL_ADDRESS" : rows[0][1]}
			return json.dumps(results)
		else:
			return json.dumps({"STATUS" : 404})
	# End of function: riderData			

	# Driver Data
	def driverData(self, user_id):

		driver = Driver(user_id = user_id)
		rows = self.__dbHandler.driverData(driver)

		if rows is not None:
			results = {"STATUS": 200, "USER_NAME": rows[0][0], "EMAIL_ADDRESS" : rows[0][1]}
			return json.dumps(results)
		else:
			return json.dumps({"STATUS" : 404})

	# End of function: driverData

	# Update User using PUT
	# def updateRider(self, userID, username, emailAddress, password):
	# 	try:
	# 		sql = "UPDATE student SET first_name=%s, last_name=%s, class=%s, age=%s, address=%s WHERE id=%s"
	# 		data = []

	# 		isDirty = False
	# 		if (username is not None):
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
	def deleteRider(self, userID):
		rider = Rider(user_id = userID)

		if self.__dbHandler.deleteRider(rider) is not None:
			return json.dumps({"STATUS": 200})
		else:
			return json.dumps({"STATUS": 404})
	# End of function: deleteRider	

	# Delete record from the Database
	def deleteDriver(self, driverID):
		driver = Driver(user_id = driverID)

		if self.__dbHandler.deleteDriver(driver) is not None:
			return json.dumps({"STATUS": 200})
		else:
			return json.dumps({"STATUS": 404})
	# End of function: deleteDriver 

	"""
	Checks if a driver has accepted the rider's request
	Returns:
	FIND_STATUS = 2 -> RET FOUND
	FIND_STATUS = 1 -> RET REJECTED
	FIND_STATUS = 0 -> RET: NO_CHANGE
	ELSE: ERROR
	"""
	def __checkFindStatus(self, userID):
		rider = Rider(user_id = userID)

		rows = self.__dbHandler.checkFindStatus(rider)

		if rows is not None:
			print("checkFindStatus: rowCount is: ", len(rows))

			if (len(rows) > 0):
				if (rows[0][0] == 0):
					results = {"STATUS" : "NO_CHANGE"}
				elif (rows[0][0] == 1):
					results = {"STATUS" : "REJECTED"}
				elif (rows[0][0] == 2):
					results = {"STATUS" : "FOUND", "DRIVER_ID": rows[0][1], "DRIVER_NAME" : rows[0][2], "DRIVER_LAT" : rows[0][3], "DRIVER_LONG": rows[0][4]}
			else:
				return {"STATUS": "ERROR"}
			
			return results
		else:
			return {"STATUS": "ERROR"}

	# End of function: __checkFindStatus

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

			rows = self.__dbHandler.searchDriverForRide()

			for row in rows:
				driverID = row[0]
				data = (userID, latitude, longitude, driverID);

				rider = Rider(userID = userID)
				driver = Driver(driverID = driverID)
				rows = self.__dbHandler.sendRideRequest()
				#rows = executeQuery(sendRequest, tuple(data))

				print("Rowcount is", self.cursor.rowcount)

				if (self.cursor.rowcount == 1):
					driverFound = True
					break

			if driverFound:
				attempts = 0
				rejectedAttempts = 0

				while attempts < ATTEMPT_LIMIT and rejectedAttempts < REJECTED_ATTEMPT_LIMIT:
					res = __checkFindStatus(userID)
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
		
	# End of Function: setMarkActive


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


# End of class: Controller