"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""
# =========================================================================
# 						 		DRIVER CLASS
# =========================================================================

from User import *
from Location import *

# Nabeel Danish
class Driver(User):

	# Constructors
	def __init__(self, *args, **kwargs):
		super(Driver, self).__init__(*args, **kwargs)

		# Class attributes
		self.__last_location = kwargs.get('last_location')
		self.__activeStatus = kwargs.get('')

	# End of __init__

	# Getters and Setters
	def getLastLocation(self):
		return self.__last_location

	def setLastLocation(self, last_location):
		self.__last_location = last_location

# End of class: Driver