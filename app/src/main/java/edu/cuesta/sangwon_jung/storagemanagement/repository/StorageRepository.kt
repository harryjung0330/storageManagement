package edu.cuesta.sangwon_jung.storagemanagement.repository

import android.content.Context
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import edu.cuesta.sangwon_jung.storagemanagement.api.*
import edu.cuesta.sangwon_jung.storagemanagement.data.Order
import edu.cuesta.sangwon_jung.storagemanagement.data.UserPs
import edu.cuesta.sangwon_jung.storagemanagement.database.LogInDatabase
import kotlinx.coroutines.Deferred
import java.util.concurrent.Executors

const val DATABASE_NAME = "LogInDatabase"
class StorageRepository private constructor(context: Context)
{
    private lateinit var callMaker:CallMaker
    private val database : LogInDatabase = Room.databaseBuilder(
        context.applicationContext,
        LogInDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val logInDao = database.getDao()



    fun addUserAndPs(userPs:UserPs)
    {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            logInDao.insertIDPS(userPs)
        }
    }

    fun queryUserPs():LiveData<List<UserPs>>
    {
        return logInDao.getIdAndPs()
    }
    fun logIn(userName:String, password:String): Deferred<User?>
    {
        val logInCall = LogInCall()
        return logInCall.logIn(userName, password)
    }

    fun createCallMaker(token:String)
    {
        callMaker = CallMaker(token)
    }

    fun addData(listOfOrder:List<Order>)
    {
        callMaker.addData(listOfOrder)
    }

    fun getNotification():LiveData<List<Notification>>
    {
        return callMaker.getNotification()
    }

    fun getCheckedAmount(checkList:List<Check>):LiveData<List<Int>>
    {
        return callMaker.getCheckAmount(checkList)
    }

    fun getStorage():LiveData<List<String>>
    {
        Log.d("getStorage", "repository")
        return callMaker.getStorage()
    }

    fun getTypes():LiveData<List<String>>
    {
        return callMaker.getTypes()
    }

    fun getDetails(storageName:String, typeName:String):LiveData<List<DetailItem>>
    {
        return callMaker.getDetails(storageName, typeName)
    }

    fun getGroups(order:Int):LiveData<List<ShowGroupReciever>>
    {
        return callMaker.getGroups(order)
    }

    fun getGroupDetails(groupId:String):LiveData<List<Order>>
    {
        return callMaker.getGroupDetails(groupId)
    }

    fun updateOrder(orderList:List<Order>)
    {
        callMaker.updateOrders(orderList)
    }

    fun getColors():LiveData<List<String>>
    {
        return callMaker.getColors()
    }

    fun customOrSrouce():LiveData<List<String>>
    {
        return callMaker.getCustomerOrSource()
    }

    fun updateUserPs(userPs:UserPs)
    {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            logInDao.updateIDPS(userPs)
        }

    }

    companion object
    {
        var repository: StorageRepository? = null

        fun getInstance(): StorageRepository
        {
            return repository!!
        }

        fun createInstance(con:Context)
        {
            if(repository == null)
            {
                repository = StorageRepository(con)
            }
        }
    }
}