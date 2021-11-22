package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.data.Order
import edu.cuesta.sangwon_jung.storagemanagement.data.OrderWrapper
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository

class ShowGroupDetailViewModel:ViewModel()
{
    private val aRep = StorageRepository.getInstance()
    var isUpdateButtonEnabled = true

    private var orderListLiveData:LiveData<List<Order>>? = null
    private lateinit var orderWrapperLiveData:LiveData<List<OrderWrapper>>

    private var typeLiveData:LiveData<List<String>>? = null
    private var colorLiveData:LiveData<List<String>>? = null
    private var customLiveData:LiveData<List<String>>? = null

    fun getTypes():LiveData<List<String>>
    {
        if(typeLiveData == null)
        {
            initializeType()
        }

        return typeLiveData?:MutableLiveData()
    }

    fun getColors():LiveData<List<String>>
    {
        if(colorLiveData == null)
        {
            initializeColors()
        }

        return colorLiveData?: MutableLiveData()
    }

    fun getCustomerOrSource():LiveData<List<String>>
    {
        if(customLiveData == null)
        {
            initializeCustom()
        }

        return customLiveData?:MutableLiveData()
    }

    fun initializeType()
    {
        typeLiveData = aRep.getTypes()
    }

    fun initializeColors()
    {
        colorLiveData = aRep.getColors()
    }

    fun initializeCustom()
    {
        customLiveData = aRep.customOrSrouce()
    }

    private fun getGroupDetails(groupId:String)
    {
        orderListLiveData = aRep.getGroupDetails(groupId)
        orderWrapperLiveData = Transformations.map(orderListLiveData?:MutableLiveData<List<Order>>(
            emptyList())){
            generateOrderWrapper(it)
        }
    }


    private fun generateOrderWrapper(aList:List<Order>):List<OrderWrapper>
    {
        val aMutableList:MutableList<OrderWrapper> = mutableListOf()
        for(order in aList)
        {
            aMutableList.add(OrderWrapper(order))
        }
        return aMutableList
    }


    fun getGroupDetailsLiveData(groupId:String):LiveData<List<OrderWrapper>>
    {
        if(orderListLiveData == null)
        {
            Log.d("ShowGroupDetails", "orderListLiveData is" + orderListLiveData)
            getGroupDetails(groupId)
        }

        return orderWrapperLiveData
    }

    fun updateTheOrders(orderWrapperList:List<OrderWrapper>)
    {
        val aList = mutableListOf<Order>()

        for(orderWrapper in orderWrapperList)
        {
            if(orderWrapper.didChange)
            {
                aList.add(orderWrapper.order)
            }
        }
        Log.d("ShowGroupDetails", " " + aList)
        updateOrders(aList)
    }

    private fun updateOrders(orderList:List<Order>)
    {
        aRep.updateOrder(orderList)
    }


}