"""
	Savaari Application Programming Interface
	Nabeel Danish               18I-0579
	Muhammad Farjad Ilyas       18I-0436
"""
# =========================================================================
# 						 		DOCUMENT CLASS
# =========================================================================

# Nabeel Danish
class Document(object):

	# Main Constructor
	def __init__(self, *args, **kwargs):

		# Main Attributes
		self.__type = kwargs.get('type')
		self.__scanned_copy = kwargs.get('scanned_copy')

		# Super Constructor
		super(Document, self).__init__()

	# End of __init__

	# Getters and Setters
	def getDocumentType(self):
		return self.__type

	def setDocumentType(self, typeDoc):
		self.__type = typeDoc

	def getScannedCopy(self):
		return self.__scanned_copy

	def setScannedCopy(self, scanned_copy):
		self.__scanned_copy = scanned_copy

# End of Class: Document