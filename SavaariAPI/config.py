from app import app
from flaskext.mysql import MySQL

mysql = MySQL()

# MySQL configurations
app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'thisIsOurSDAProjectPassword'
app.config['MYSQL_DATABASE_DB'] = 'ride_hailing'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)