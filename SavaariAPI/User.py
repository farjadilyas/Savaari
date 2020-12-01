"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""
# =========================================================================
# 						 		USER CLASS
# =========================================================================

# Nabeel Danish
class User(object):

	# Main Constructor
	def __init__(self, *args, **kwargs):

		# Class attributes
		self.__user_id = kwargs.get('user_id')
		self.__username = kwargs.get('username')
		self.__password = kwargs.get('password')
		self.__email_address = kwargs.get('email_address')
		self.__firstName = kwargs.get('firstName')
		self.__lastName = kwargs.get('lastName')
		self.__phoneNo = kwargs.get('phoneNo')
	
		# Calling Super Constructor
		super(User, self).__init__()

	# End of __init__

	# Getters and Setters
	def getUserID(self):
		return self.__user_id

	def setUser_Id(self, user_id):
		self.__user_id = user_id

	def getUserName(self):
		return self.__username

	def setUserName(self, username):
		self.__username = username

	def getPassword(self):
		return self.__password

	def setPassword(self, password):
		self.__password = password

	def getEmailAddress(self):
		return self.__email_address

	def setEmailAddress(self, email_address):
		self.__email_address = email_address

	def getFirstName(self):
		return self.__firstName

	def setFirstName(self, firstName):
		self.__firstName = firstName

	def getLastName(self):
		return self.__lastName

	def setLastName(self, lastName):
		self.__lastName = lastName

	def getPhoneNo(self):
		return self.__phoneNo

	def setPhoneNo(self, phoneNo):
		self.__phoneNo = phoneNo

# End of Class: User