package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.cuesta.sangwon_jung.storagemanagement.R
import edu.cuesta.sangwon_jung.storagemanagement.data.Order

private const val TYPE_LIST_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CalculateFragment.type_list_key"
private const val COLOR_LIST_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CalculateFragment.color_list_key"
private const val SIZE_LIST_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CalculateFragment.size_list_key"
private const val COST_LIST_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CalculateFragment.cost_list_key"
private const val AMOUNT_LIST_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CalculateFragment.amount_list_key"
private const val CUSTOMER = "edu.cuesta.sangwon_jung.storagemanagement.CalculateFragment.customer"

class CalculateFragment: Fragment()
{
    private lateinit var totalCost:TextView
    private lateinit var customer:TextView
    private lateinit var typeList:ArrayList<String>
    private lateinit var colorList:ArrayList<String>
    private lateinit var sizeList:ArrayList<String>
    private lateinit var amountList:ArrayList<Int>
    private lateinit var costList:ArrayList<Int>
    private lateinit var customerName:String
    private lateinit var linearContainer:LinearLayout
    private var totalSum = 0
    private var isStopped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeList = arguments?.getStringArrayList(TYPE_LIST_KEY)?: ArrayList<String>()
        colorList = arguments?.getStringArrayList(COLOR_LIST_KEY)?:ArrayList<String>()
        sizeList = arguments?.getStringArrayList(SIZE_LIST_KEY)?: ArrayList()
        amountList = arguments?.getIntegerArrayList(AMOUNT_LIST_KEY)?: ArrayList()
        costList = arguments?.getIntegerArrayList(COST_LIST_KEY)?: ArrayList()
        customerName = arguments?.getString(CUSTOMER)?:""

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aView = inflater.inflate(R.layout.fragment_calculate, container, false)

        return aView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        totalCost = view.findViewById(R.id.total_cost)
        customer = view.findViewById(R.id.calc_customer)
        customer.setText(customerName)
        linearContainer = view.findViewById(R.id.calc_container)

    }

    override fun onStart() {
        super.onStart()
        if(isStopped)
        {
            return
        }

        createViewsDynamically()
        isStopped = true
    }

    fun createViewsDynamically()
    {
        for(index in 0 until typeList.size)
        {
            val cost = costList[index] * amountList[index]
            totalSum += cost
            createAViewDynamically(typeList[index], colorList[index], sizeList[index], cost)
        }

        totalCost.setText(totalSum.toString())
    }

    fun createAViewDynamically(type:String, color:String, size:String, cost:Int)
    {
        val aView = layoutInflater.inflate(R.layout.calc_item, null, false)
        linearContainer.addView(aView)

        val typeView = aView.findViewById<TextView>(R.id.calc_type)
        val colorView = aView.findViewById<TextView>(R.id.calc_color)
        val sizeView = aView.findViewById<TextView>(R.id.calc_size)
        val costView = aView.findViewById<TextView>(R.id.calc_cost)

        typeView.setText(type)
        colorView.setText(color)
        sizeView.setText(size)
        costView.setText(cost.toString())


    }

    companion object
    {
        fun getInstance(orderList:List<Order>):CalculateFragment
        {
            val typeList = ArrayList<String>()
            val colorList = ArrayList<String>()
            val sizeList = ArrayList<String>()
            val amountList = ArrayList<Int>()
            val costList = ArrayList<Int>()

            for(order in orderList)
            {
                typeList.add(order.type)
                colorList.add(order.color)
                sizeList.add(order.size)
                amountList.add(order.amount)
                costList.add(order.priceOrCost)
            }

            val tempFragment = CalculateFragment()
            tempFragment.arguments = Bundle().apply {
                putStringArrayList( TYPE_LIST_KEY, typeList)
                putStringArrayList(COLOR_LIST_KEY, colorList)
                putStringArrayList(SIZE_LIST_KEY, sizeList)
                putIntegerArrayList(AMOUNT_LIST_KEY, amountList)
                putIntegerArrayList(COST_LIST_KEY, costList)
                putString(CUSTOMER, orderList[0].customerOrSource)
            }

            return tempFragment
        }
    }



}