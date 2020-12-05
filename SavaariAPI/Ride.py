# =========================================================================
# 						 	  RIDE CLASS
# =========================================================================

# Nabeel Danish
class Ride(object):

	# Main Constructor
	def __init__(self, *args, **kwargs):

		# Main Attributes
		self.__rideID = kwargs.get('rideID')
		self.__rider = kwargs.get('rider')
		self.__driver = kwargs.get('driver')
		self.__vehicle = kwargs.get('vehicle')
		self.__rideType = kwargs.get('rideType')
		self.__pickup = kwargs.get('pickup')
		self.__destination = kwargs.get('destination')
		self.__startTime = kwargs.get('startTime')
		self.__endTime = kwargs.get('endTime')
		self.__fare = kwargs.get('fare')
		self.__estimatedFare = kwargs.get('estimatedFare')
		self.__payment_method = kwargs.get('payment_method')
		self.__payment = kwargs.get('payment')
		self.__status = kwargs.get('status')

		# Super Constructor
		super(Ride, self).__init__()

	# End of __init__

	# Getter and Setters


# End of Class: Ride