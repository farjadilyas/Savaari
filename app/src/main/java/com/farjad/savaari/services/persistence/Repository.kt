package com.farjad.savaari.services.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import com.farjad.savaari.services.network.OnDataLoadedListener
import com.farjad.savaari.services.network.NetworkUtil
import com.farjad.savaari.services.network.WebService
import com.farjad.savaari.services.persistence.user.UserDao
import com.farjad.savaari.services.persistence.user.UserDataModel
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class Repository internal constructor(private val executor: Executor,
                                      private val webService: WebService,
                                      private val userDao: UserDao
                                      )
{

    private var url : String = "";

    init {
        url = loadDataSourceUrl()
        Log.d("Repository url: ", url)
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
        val TAG = Repository::class.java.name
    }

    private fun loadDataSourceUrl(): String {
        val prop: Properties
        val propFileName = "config.properties"
        val inputStream: InputStream?
        return try {
            prop = Properties()
            inputStream = javaClass.classLoader!!.getResourceAsStream(propFileName)
            if (inputStream != null) {
                prop.load(inputStream)
            } else {
                throw FileNotFoundException("property file '$propFileName' not found in the classpath")
            }

            // Get property value
            prop.getProperty("dataSourceUrl")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    fun getUser(userId: Int): LiveData<UserDataModel> {
        refreshUser(userId)
        // Returns a LiveData object directly from the database.
        return userDao.load(userId)
    }

    private fun refreshUser(userID : Int) {

        Log.d(TAG, "refreshUser called!");
        // Runs in a background thread.
        executor.execute {
            // Check if user data was fetched recently.
            val userExists = userDao.hasUser(userID, FRESH_TIMEOUT)

            if (userExists > 0) {
                Log.d(TAG, "loadRoomData: User does not exist, refreshing data")
                // Refreshes the data.
                val response = webService.getUser(userID).execute()

                // Check for errors here.

                // Updates the database. The LiveData object automatically
                // refreshes, so we don't need to do anything else here.
                userDao.save(response.body()!!)
            }
            else {
                Log.d(TAG, "loadRoomData: User already exists!")
            }
        }
    }

    //Find Driver
    fun findDriver(callback: OnDataLoadedListener, currentUserID: Int, srcLatitude: Double,
                   srcLongitude: Double, destLatitude: Double, destLongitude: Double, paymentMode: Int, rideType: Int) {
        executor.execute {
            callback.onDataLoaded(NetworkUtil.getInstance().findDriver(url, currentUserID,
                    srcLatitude, srcLongitude, destLatitude, destLongitude, paymentMode, rideType))
        }
    }

    // Sign-Up
    fun signup(callback: OnDataLoadedListener, nickname: String?, username: String?, password: String?) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().signup(url, nickname, username, password)) }
    }

    // Login
    fun login(callback: OnDataLoadedListener, username: String?, password: String?) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().login(url, username, password)) }
    }

    fun persistLogin(callback: OnDataLoadedListener, userID: Int?) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().persistLogin(url, userID)) }
    }

    fun logout(callback: OnDataLoadedListener, userID: Int?) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().logout(url, userID)) }
    }

    // Loading User Data
    fun loadUserData(callback: OnDataLoadedListener, currentUserID: Int) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().loadUserData(url, currentUserID)) }
    }

    // Get User Locations
    fun getUserLocations(callback: OnDataLoadedListener) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().getUserLocations(url)) }
    }

    // Send Last Location
    fun sendLastLocation(currentUserID: Int, latitude: Double, longitude: Double) {
        executor.execute { NetworkUtil.getInstance().sendLastLocation(url, currentUserID, latitude, longitude) }
    }

    fun getDriverLocation(callback: OnDataLoadedListener, driverID: Int) {
        callback.onDataLoaded(NetworkUtil.getInstance().getDriverLocation(url, driverID))
    }

    fun getRide(callback: OnDataLoadedListener, riderID: Int) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().getRide(url, riderID)) }
    }

    fun getRideStatus(callback: OnDataLoadedListener, rideID: Int) {
        callback.onDataLoaded(NetworkUtil.getInstance().getRideStatus(url, rideID))
    }

    fun acknowledgeEndOfRide(callback: OnDataLoadedListener, rideID: Int, riderID: Int) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().acknowledgeEndOfRide(url, rideID, riderID)) }
    }

    fun giveFeedbackForDriver(callback: OnDataLoadedListener, rideID: Int, driverID: Int, rating: Float) {
        executor.execute { callback.onDataLoaded(NetworkUtil.getInstance().giveFeedbackForDriver(url, rideID, driverID, rating)) }
    }
}