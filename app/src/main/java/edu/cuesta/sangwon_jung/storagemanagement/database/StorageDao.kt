package edu.cuesta.sangwon_jung.storagemanagement.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.cuesta.sangwon_jung.storagemanagement.data.UserPs

@Dao
interface StorageDao
{
    @Query("SELECT * FROM userps")
    fun getIdAndPs():LiveData<List<UserPs>>

    @Insert
    fun insertIDPS(userPs:UserPs)

    @Update
    fun updateIDPS(userPs:UserPs)
}