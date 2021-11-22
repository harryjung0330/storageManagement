package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.api.Notification
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository

class MainPageViewModel: ViewModel()
{
    val aRepo = StorageRepository.getInstance()

    val aLiveData
        get() = aRepo.getTypes()

    fun getNotification(): LiveData<List<Notification>>
    {
        return aRepo.getNotification()
    }
}