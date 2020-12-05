from OracleDBHandler import OracleDBHandler

"""
Factory for producing DBHandler Objects
"""
class DBHandlerFactory:

    @staticmethod
    def getDBHandler(dbChoice):
        if (dbChoice == "Oracle"):
            return OracleDBHandler()
