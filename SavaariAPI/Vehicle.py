"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""
# =========================================================================
# 						 		VEHICLE CLASS
# =========================================================================

# Nabeel Danish
class Vehicle(object):

	# Main Constructor
	def __init__(self, *args, **kwargs):

		# Class attributes
		attributeName = ['make', 'model', 'registration_no', 'year', 'registration_document']

		# Main Attributes
		self.__attribute = {}
		for attribute in attributeName:
			if attribute in kwargs:
				self.__attribute[attribute] = kwargs[attribute]

		super(Vehicle, self).__init__()

	# End of __init__