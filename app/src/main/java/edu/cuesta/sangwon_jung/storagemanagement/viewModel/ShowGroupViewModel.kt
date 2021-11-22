package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.api.ShowGroupReciever
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository

class ShowGroupViewModel: ViewModel()
{
    val aRep = StorageRepository.getInstance()
    var curOrder = 0

    private fun getGroups(order:Int): LiveData<List<ShowGroupReciever>>
    {
        return aRep.getGroups(order)
    }

    fun getCurrentGroup():LiveData<List<ShowGroupReciever>>
    {
        return getGroups(curOrder)
    }

    fun getNext(): LiveData<List<ShowGroupReciever>>
    {
        curOrder += 1
        return getGroups(curOrder)
    }

    fun getPrevious():LiveData<List<ShowGroupReciever>>?
    {
        if(curOrder > 0)
        {
            curOrder -= 1
            return getGroups(curOrder)
        }

        return null
    }

}