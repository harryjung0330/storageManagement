package edu.cuesta.sangwon_jung.storagemanagement.api

import androidx.lifecycle.MutableLiveData

class TypeLiveDataHolder private constructor()
{
    val aMutableLiveData = MutableLiveData<List<String>>()

    fun getMutableLiveData():MutableLiveData<List<String>>
    {
        return aMutableLiveData
    }

    companion object{
         var inst: TypeLiveDataHolder? = null

        fun getInstance():TypeLiveDataHolder
        {
            if(inst == null)
            {
                inst = TypeLiveDataHolder()
            }

            return inst!!
        }
    }
}