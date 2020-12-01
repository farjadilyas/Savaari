# =========================================================================
# 						 		LOCATION CLASS
# =========================================================================

# Nabeel Danish
class Location(object):

	# Main Constructor
	def __init__(self, *args, **kwargs):

		# Main Attributes
		self.__latitude = kwargs.get('latitude')
		self.__longitude = kwargs.get('longitude')
		self.__timestamp = kwargs.get('timestamp')

		# Super Constructor
		super(Location, self).__init__()

	# End of __init__
	
	# Getters and Setters
	def getLatitude(self):
		return self.__latitude

	def setLatitude(self, latitude):
		self.__latitude = latitude

	def getLongitude(self):
		return self.__longitude

	def setLongitude(self, longitude):
		self.__longitude = longitude

	def getTimstamp(self):
		return self.__timestamp

	def setTimestamp(self, timestamp):
		self.__timestamp = timestamp

# End of Class: Location