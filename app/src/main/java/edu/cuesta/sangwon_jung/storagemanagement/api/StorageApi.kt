package edu.cuesta.sangwon_jung.storagemanagement.api

import edu.cuesta.sangwon_jung.storagemanagement.data.Order
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface StorageApi
{
    @POST("adddata")
    fun addData(@Body listOfOrder:List<Order>): Call<ResponseBody>

    @GET("getstorage")
    fun getStorage():Call<List<String>>

    @GET("sendtypes")
    fun getTypes():Call<List<List<String>>>

    @POST("viewstorage")
    fun viewStorage(@Body params:Map<String, String>):Call<List<DetailItem>>

    @POST("login")
    fun logIn(@Body params:Map<String, String>): Call<User>

    @GET("sendnotification")
    fun getNotification():Call<List<Notification>>

    @POST("checkorder")
    fun checkOrder(@Body checkList:List<Check>):Call<List<Int>>

    @GET("showgroups")
    fun showGroups(@Query("order") order:Int):Call<List<ShowGroupReciever>>

    @GET("showdetailsbasedongroupid")
    fun getGroupDetails(@Query("groupId") groupId:String):Call<List<OrderReciever>>

    @POST("updateorder")
    fun updateOrder(@Body orderList:List<Order>):Call<ResponseBody>

    @GET("getcustomerorsource")
    fun getCustomerOrsourceList():Call<List<String>>

    @GET("getcolors")
    fun getColors():Call<List<String>>
}