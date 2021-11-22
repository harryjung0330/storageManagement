package edu.cuesta.sangwon_jung.storagemanagement.api

import edu.cuesta.sangwon_jung.storagemanagement.data.Order

class OrderReciever(val orderId: String? = null, var groupId:String? = null, var type:String, var color:String,
                         var size:String, var isIn: Int, var costOrPrice: Int, var amount:Int, var date:String,
                         var storage:String, var customerOrSource:String)
{

        fun convertToOrder():Order
        {
            val tempBoolean = if(isIn == 1){true} else {false}
            return Order(orderId, groupId, type, color, size, tempBoolean, costOrPrice, amount, date, storage, customerOrSource )
        }

}