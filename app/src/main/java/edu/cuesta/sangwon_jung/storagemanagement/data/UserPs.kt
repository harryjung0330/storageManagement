package edu.cuesta.sangwon_jung.storagemanagement.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserPs(@PrimaryKey val id: UUID = UUID.randomUUID(), var userName:String, var password:String)