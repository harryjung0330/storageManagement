package edu.cuesta.sangwon_jung.storagemanagement.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.google.gson.JsonParser
import edu.cuesta.sangwon_jung.storagemanagement.data.Order
import okhttp3.OkHttpClient
import retrofit2.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

class CallMaker(val token:String)
{
    val baseUrl = "https://osd66owm36.execute-api.ap-northeast-2.amazonaws.com/test/"
    val client = OkHttpClient.Builder().addInterceptor(StorageInterceptor(token)).build()
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).
        addConverterFactory(ScalarsConverterFactory.create()).client(client).build()
    val storageApi = retrofit.create(StorageApi::class.java)

    fun addData(listOfOrder:List<Order>)
    {
        Log.d("addData", "dataAdded")
        val addCall: Call<ResponseBody> = storageApi.addData(listOfOrder)
        val callBack = object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("addData", "Failed to add Data", t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("addData"," " + response.body()?.string())

            }

        }
        addCall.enqueue(callBack)
    }

    fun getStorage(): LiveData<List<String>>
    {
        var aLiveData = MutableLiveData<List<String>>()
        val getStorCall = storageApi.getStorage()
                val callBack = object: Callback<List<String>>{
            override fun onFailure(call: Call<List<String>>, t: Throwable)
            {
                Log.e("getStorage", "Failed to get Storage")
            }

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val responsList = response.body()
                Log.d("getStorage", "response is ")
                aLiveData.value = responsList
                Log.d("getStorage", " " + aLiveData.value)
            }
        }

        getStorCall.enqueue(callBack)
        Log.d("getStorage", "enqueued")

        return aLiveData

    }

    fun getTypes():MutableLiveData<List<String>>
    {
        val aMutableLiveData = MutableLiveData<List<String>>()
        val typeCall = storageApi.getTypes()
        val callback = object : Callback<List<List<String>>>
        {
            override fun onFailure(call: Call<List<List<String>>>, t: Throwable)
            {
                Log.e("getTypes", "Failed to get types")
            }

            override fun onResponse(call: Call<List<List<String>>>, response: Response<List<List<String>>>) {
                val responsList = response.body()
                val resultList = mutableListOf<String>()

                for(list in responsList?: emptyList())
                {
                    resultList.add(list[0])
                }
                Log.d("getTypes", "resultList: " + response)

                aMutableLiveData.value = resultList

            }
        }

        typeCall.enqueue(callback)

        return aMutableLiveData
    }

    fun getDetails(storageName: String, typeName:String):MutableLiveData<List<DetailItem>>
    {
        val params = mapOf("type" to typeName, "storage" to storageName)
        val aMutableLiveData = MutableLiveData<List<DetailItem>>()

        val aCall = storageApi.viewStorage(params)

        val callback = object : Callback<List<DetailItem>>
        {
            override fun onFailure(call: Call<List<DetailItem>>, t: Throwable)
            {
                Log.e("viewStorage", "Failed to get details")
            }

            override fun onResponse(call: Call<List<DetailItem>>, response: Response<List<DetailItem>>)
            {
                val aList = response.body()
                Log.d("viewStorage", "list is " + response.body())
                aMutableLiveData.value = aList

            }
        }

        aCall.enqueue(callback)

        return aMutableLiveData
    }

    fun getNotification():LiveData<List<Notification>>
    {
        val aMutableLiveData = MutableLiveData<List<Notification>>()
        val aCall = storageApi.getNotification()

        val callback = object: Callback<List<Notification>>
        {
            override fun onFailure(call: Call<List<Notification>>, t: Throwable)
            {
                Log.e("getNotification", "Failed to get details")
            }

            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>)
            {
                val aList = response.body()
                Log.d("getNotification", "list is " + response)
                aMutableLiveData.value = aList

            }
        }
        aCall.enqueue(callback)

        return aMutableLiveData

    }

    fun getCheckAmount(checkList:List<Check>):LiveData<List<Int>>
    {
        val aMutableLiveData = MutableLiveData<List<Int>>()
        val aCall = storageApi.checkOrder(checkList)
        val callback = object: Callback<List<Int>>
        {
            override fun onFailure(call: Call<List<Int>>, t: Throwable)
            {
                Log.e("check", "Failed to send checklist")
            }

            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>)
            {
                val aList = response.body()
                Log.d("check", "list is " + response.body())
                aMutableLiveData.value = aList
            }
        }

        aCall.enqueue(callback)
        return aMutableLiveData
    }

    fun getGroups(order:Int):LiveData<List<ShowGroupReciever>>
    {
        val aMutableLiveData = MutableLiveData<List<ShowGroupReciever>>()
        val aCall = storageApi.showGroups(order)
        val callback = object: Callback<List<ShowGroupReciever>>
        {
            override fun onFailure(call: Call<List<ShowGroupReciever>>, t: Throwable)
            {
                Log.e("getGroups", "Failed to get groups")
            }

            override fun onResponse(call: Call<List<ShowGroupReciever>>, response: Response<List<ShowGroupReciever>>)
            {
                val aList = response.body()
                Log.d("getGroups", "list is " + response.body())
                aMutableLiveData.value = aList
            }
        }

        aCall.enqueue(callback)
        return aMutableLiveData
    }

    fun getGroupDetails(groupId:String):LiveData<List<Order>>
    {
        val aMutableLiveData = MutableLiveData<List<OrderReciever>>()
        val orderLiveData = Transformations.map(aMutableLiveData){
            convertToOrder(it)
        }
        val aCall = storageApi.getGroupDetails(groupId)
        val callback = object: Callback<List<OrderReciever>>
        {
            override fun onFailure(call: Call<List<OrderReciever>>, t: Throwable)
            {
                Log.e("ShowGroupDetails", "Failed to get group details")
            }

            override fun onResponse(call: Call<List<OrderReciever>>, response: Response<List<OrderReciever>>)
            {
                val aList = response.body()
                for(ele in aList?: emptyList())
                {
                    Log.d("ShowGroupDetails", " " + ele.costOrPrice)
                }
                Log.d("ShowGroupDetails", "list is " + response.body())
                aMutableLiveData.value = aList
            }
        }
        aCall.enqueue(callback)

        return orderLiveData
    }

    fun convertToOrder(orderRecievers: List<OrderReciever>):List<Order>
    {
        val aList = mutableListOf<Order>()
        for(orderReciever in orderRecievers)
        {
            aList.add(orderReciever.convertToOrder())
        }

        return aList
    }

    fun updateOrders(orderList:List<Order>)
    {
        val aCall = storageApi.updateOrder(orderList)
        val callback = object: Callback<ResponseBody>
        {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("updateOrder", "update failed")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("updateOrder", "update successful " + response)
            }
        }

        aCall.enqueue(callback)

    }

    fun getColors():LiveData<List<String>>
    {
        val aMutableLiveData = MutableLiveData<List<String>>()
        val aCall = storageApi.getColors()
        val callback = object: Callback<List<String>>
        {
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e("getColors", " failed to get colors")
            }
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                Log.d("getColors", " " + response.body())//To change body of created functions use File | Settings | File Templates.
                aMutableLiveData.value = response.body()
            }
        }

        aCall.enqueue(callback)

        return aMutableLiveData
    }

    fun getCustomerOrSource():LiveData<List<String>>
    {
        val aMutableLiveData = MutableLiveData<List<String>>()
        val aCall = storageApi.getCustomerOrsourceList()
        val callback = object: Callback<List<String>>
        {
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e("getCustomOrSource", "failed to get customer or source")
            }

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                Log.d("getCustomerOrSource", "" + response.body())
                aMutableLiveData.value = response.body()
            }
        }
        aCall.enqueue(callback)

        return aMutableLiveData
    }




 }


