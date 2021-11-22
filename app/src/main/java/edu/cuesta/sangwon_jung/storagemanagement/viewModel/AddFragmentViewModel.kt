package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.data.Order
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository
import java.util.*

class AddFragmentViewModel: ViewModel()
{
    val repository = StorageRepository.getInstance()
    var isFinishButtonEnabled = true
    val orderList = mutableListOf<Order>()

    fun createOrder():Order
    {
        val tempOrder = Order()
        orderList.add(tempOrder)
        return tempOrder
    }

    fun filterOrder():List<Order>
    {
        val temp = mutableListOf<Order>()
        for(order in orderList)
        {
            if(order.type != "")
            {
                temp.add(order)
            }
        }

        return temp
    }

    fun fillDateStorageCustomerIsIn(ordList:List<Order>)
    {
        val tempStorage = ordList[0].storage
        val tempDate = ordList[0].date
        val tempCustomer = ordList[0].customerOrSource
        val tempIsIn = ordList[0].isIn

        for(order in ordList)
        {
            order.storage =tempStorage
            order.customerOrSource =tempCustomer
            order.isIn = tempIsIn
            order.date = tempDate
        }

    }

    fun finish()
    {
        val listToSend = filterOrder()
        fillDateStorageCustomerIsIn(listToSend)
        repository.addData(listToSend)
    }

    private var typeLiveData:LiveData<List<String>>? = null
    private var colorLiveData:LiveData<List<String>>? = null
    private var customLiveData:LiveData<List<String>>? = null

    fun getType():LiveData<List<String>>
    {
        if(typeLiveData == null)
        {
            initializeType()
        }

        return typeLiveData?: MutableLiveData()
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

        return customLiveData?: MutableLiveData()
    }

    fun initializeType()
    {
        typeLiveData = repository.getTypes()
    }

    fun initializeColors()
    {
        colorLiveData = repository.getColors()
    }

    fun initializeCustom()
    {
        customLiveData = repository.customOrSrouce()
    }
}