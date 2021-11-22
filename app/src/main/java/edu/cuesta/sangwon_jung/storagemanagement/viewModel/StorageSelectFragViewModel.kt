package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository

class StorageSelectFragViewModel: ViewModel()
{
    val repo = StorageRepository.getInstance()

    fun getStorage(): LiveData<List<String>>
    {
        return repo.getStorage()
    }
}