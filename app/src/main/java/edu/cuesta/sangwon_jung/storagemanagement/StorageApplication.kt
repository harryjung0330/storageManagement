package edu.cuesta.sangwon_jung.storagemanagement

import android.app.Application
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository

class StorageApplication: Application()
{

    override fun onCreate() {
        super.onCreate()
        val aRep = StorageRepository.createInstance(this)

    }
}