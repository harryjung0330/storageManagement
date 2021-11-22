package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.api.DetailItem
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository

class DetailViewFragViewModel: ViewModel()
{
    val aRepo = StorageRepository.getInstance()

    fun getDetails(storageName: String, typeName: String):LiveData<List<DetailItem>>
    {
        return aRepo.getDetails(storageName, typeName)
    }
}