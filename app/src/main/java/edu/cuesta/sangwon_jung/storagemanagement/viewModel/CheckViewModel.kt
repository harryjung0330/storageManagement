package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.api.Check
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository
import java.time.temporal.TemporalAmount

class CheckViewModel :ViewModel()
{
    var aRep = StorageRepository.getInstance()
    var typeList = ArrayList<String>()
    var colorList = ArrayList<String>()
    var sizeList = ArrayList<String>()
    var amountList = ArrayList<String>()

    private var typeLiveData: LiveData<List<String>>? = null
    private var colorLiveData:LiveData<List<String>>? = null

    fun takeData(type:ArrayList<String>, color:ArrayList<String>, size:ArrayList<String>, amount:ArrayList<String>)
    {
        typeList = type
        colorList = color
        sizeList = size
        amountList = amount
    }

    fun sendCheckList(aList :List<Check>): LiveData<List<Int>>
    {
        return aRep.getCheckedAmount(aList)
    }

    fun getTypes():LiveData<List<String>>
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
            initializeColor()
        }

        return colorLiveData?: MutableLiveData()
    }

    private fun initializeType()
    {
        typeLiveData = aRep.getTypes()
    }

    private fun initializeColor()
    {
        colorLiveData = aRep.getColors()
    }



}