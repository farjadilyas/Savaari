"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""

from DBHandler import *
from Rider import *

# =========================================================================
# 						 	CONTROLLER CLASS
# =========================================================================
class Controller(object):

	# Constructors
	def __init__(self, *args, **kwargs):
		super(Controller, self).__init__(*args, **kwargs)

		# Main Attributes
		self.__dbHandler = DBHandler()
		self.__lastUserID = 1

	# End of __init__

	# -------------------------------------------------------------------
	# 				Methods called from API (System Calls)
	# -------------------------------------------------------------------

	# Sign-up Rider
	def add_rider(self, username, email_address, password):

		rider = Rider(user_id = self.__lastUserID, username = username, password = password, email_address = email_address)
		self.__lastUserID++

		return self.__dbHandler.add_rider(rider)

	# Sign-up Driver
	def add_driver(self, username, email_address, password):

		driver = Driver(user_id = self.__lastUserID, username = username, password = password, email_address = email_address)
		self.__lastUserID++

		return self.__dbHandler.add_driver(driver)

	# Login Rider
	def login_rider(self, username, password):

		rider = Rider(username = username, password = password)
		return self.__dbHandler.login_rider(rider)

	# Login Driver
	def login_driver(self, username, password):

		driver = Driver(username = username, password = password)
		return self.__dbHandler.login_driver(driver)

	# Rider Details
	def rider_details(self):

		return self.__dbHandler.rider_details()

	# Driver Details
	def driver_details(self):

		return self.__dbHandler.driver_details()

	# Rider Data
	def rider_data(self, user_id):

		rider = Rider(user_id = user_id)
		return self.__dbHandler.rider_data(rider)

	# Driver Data
	def driver_data(self, user_id):

		driver = Driver(user_id = user_id)
		return self.__dbHandler.driver_data(driver)

# End of class: Controller