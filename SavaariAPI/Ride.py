# =========================================================================
# 						 	  RIDE CLASS
# =========================================================================

# Nabeel Danish
class Ride(object):

	# Main Constructor
	def __init__(self, *args, **kwargs):

		# Class attributes
		attributeName = ['rider', 'driver', 'vehicle', 'pickup', 'destination', 'duration', 'fare', 'payment_method', 'payment']

		# Main Attributes
		self.__attribute = {}
		for attribute in attributeName:
			if attribute in kwargs:
				self.__attribute[attribute] = kwargs[attribute]

		super(Ride, self).__init__()

	# End of __init__

	# Getter and Setters


# End of Class: Ride