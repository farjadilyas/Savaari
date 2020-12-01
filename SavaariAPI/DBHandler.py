from abc import ABC,abstractmethod 
  
class DBHandler(ABC): 
    @abstractmethod
    def addRider(self, rider):
        pass

    @abstractmethod
    def addDriver(self, driver):
        pass

    @abstractmethod
    def loginRider(self, rider):
        pass

    @abstractmethod
    def loginDriver(self, driver):
        pass

    @abstractmethod
    def riderDetails(self):
        pass
    
    @abstractmethod
    def driverDetails(self):
        pass

    @abstractmethod
    def riderData(self, rider):
        pass

    @abstractmethod
    def driverData(self, driver):
        pass

    @abstractmethod
    def updateRider(self, userID, username, emailAddress, password):
        pass

    @abstractmethod
    def deleteRider(self, userID):
        pass

    @abstractmethod
    def deleteDriver(self, driverID):
        pass

    @abstractmethod
    def searchDriverForRide(self):
        pass

    @abstractmethod
    def sendRideRequest(self, ride):
        pass

    @abstractmethod
    @abstractmethod
    @abstractmethod
