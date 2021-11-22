package edu.cuesta.sangwon_jung.storagemanagement.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.cuesta.sangwon_jung.storagemanagement.data.UserPs

@TypeConverters(Converter::class)
@Database(entities = [UserPs::class], version = 1)
abstract class LogInDatabase:RoomDatabase()
{
    abstract fun getDao():StorageDao

}