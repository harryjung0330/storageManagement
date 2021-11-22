package edu.cuesta.sangwon_jung.storagemanagement.viewModel

import androidx.lifecycle.ViewModel
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository

class TypeSelectFragViewModel: ViewModel()
{
    val aRepo = StorageRepository.getInstance()
    val typeLiveData
        get() = aRepo.getTypes()


}