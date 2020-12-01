"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""

# =========================================================================
# 						 		RIDER CLASS
# =========================================================================

from User import *

# Nabeel Danish
class Rider(User):

	# Constructors
	def __init__(self, *args, **kwargs):

		# Calling Super Constructor
		super(Rider, self).__init__(*args, **kwargs)
	# End of __init__

# End of class: Rider