package edu.cuesta.sangwon_jung.storagemanagement.data

import java.util.*

data class Order (val orderId: String? = null, var groupId:String? = null, var type:String = "", var color:String = "",
                    var size:String = "", var isIn: Boolean = true, var priceOrCost: Int = 0, var amount:Int = 0, var date:String = "",
                    var storage:String = "", var customerOrSource:String = "")