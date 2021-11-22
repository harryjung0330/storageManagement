package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import edu.cuesta.sangwon_jung.storagemanagement.R
import edu.cuesta.sangwon_jung.storagemanagement.data.Order
import edu.cuesta.sangwon_jung.storagemanagement.data.OrderWrapper
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.ShowGroupDetailViewModel

private const val GROUP_ID = "edu.cuesta.sangwon_jung.storagemanagement.ShowGroupDetailFragment.groupId"

class ShowGroupDetailFragment: Fragment()
{
    private lateinit var date:EditText
    private lateinit var customerOrSource:AutoCompleteTextView
    private lateinit var storage:EditText
    private lateinit var isIn: TextView
    private lateinit var linContainer:LinearLayout
    private lateinit var groupId:String
    private lateinit var updateButton:Button
    private var isStopped = false
    private lateinit var groupNetworkLiveData:LiveData<List<OrderWrapper>>
    private lateinit var calculateButton: Button

    private val showGroupDetailViewModel: ShowGroupDetailViewModel by lazy{
        ViewModelProviders.of(this).get(ShowGroupDetailViewModel :: class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getString(GROUP_ID) ?:""
        groupNetworkLiveData = showGroupDetailViewModel.getGroupDetailsLiveData(groupId)

        Log.d("ShowDetailsFragment", "oncreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aView = inflater.inflate(R.layout.fragment_show_recent_detail, container, false)
        Log.d("ShowDetailFragment", "onCreateView")
        return aView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ShowDetailFragment", "onViewCreated")

        date = view.findViewById(R.id.group_detail_date)
        storage = view.findViewById(R.id.group_detail_storage_number)
        isIn = view.findViewById(R.id.group_detail_isIn)
        linContainer = view.findViewById(R.id.group_detail_container)
        customerOrSource = view.findViewById(R.id.group_detail_customer_or_source)
        updateButton = view.findViewById(R.id.update_button)
        calculateButton = view.findViewById(R.id.update_calculate_button)
        calculateButton.setOnClickListener{
            val tempLiveData = showGroupDetailViewModel.getGroupDetailsLiveData(groupId)
            tempLiveData.observe(viewLifecycleOwner, Observer {
                val tempList = mutableListOf<Order>()

                for(orderWrapper in it)
                {
                    tempList.add(orderWrapper.order)
                }

                val calFragment = CalculateFragment.getInstance(tempList)
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.show_recent_container, calFragment)
                    .addToBackStack(null).commit()
            })

        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("ShowDetailFragment", "onviewstaterestored")
    }

    override fun onStart() {
        super.onStart()
        Log.d("ShowDetailFragment", "onStart")

        if(isStopped)
        {
            return
        }


        groupNetworkLiveData.observe(viewLifecycleOwner, Observer {
            createViewsDynamically(it)
        })

        updateButton.setOnClickListener{
            if(testPart(date.text.toString(), storage.text.toString(), customerOrSource.text.toString()) &&
                    checkOrders(groupNetworkLiveData.value?: emptyList()))
            {
                showGroupDetailViewModel.updateTheOrders(groupNetworkLiveData.value?: emptyList())
                updateButton.isEnabled = false
                showGroupDetailViewModel.isUpdateButtonEnabled = false
            }
        }

        isStopped = true
    }

    override fun onStop() {
        super.onStop()
        Log.d("ShowDetailFragment", "onstop")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ShowDetailFragment", "onresume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ShowDetailFragment", "onpaused")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ShowDetailFragment", "ondestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ShowDetailFragment", "ondestroyview")
        isStopped = false
    }
    fun createViewsDynamically(orderWrapperList:List<OrderWrapper>)
    {
        updateButton.isEnabled = showGroupDetailViewModel.isUpdateButtonEnabled
        date.setText(orderWrapperList[0].order.date)
        storage.setText(orderWrapperList[0].order.storage)
        customerOrSource.setText(orderWrapperList[0].order.customerOrSource)
        if(orderWrapperList[0].order.isIn)
        {
            isIn.setText(getString(R.string.input))
            customerOrSource.setHint(getString(R.string.source))
        }
        else
        {
            isIn.setText(getString(R.string.output))
            customerOrSource.setHint(getString(R.string.customer))
        }


        val mDateEntryWatcher = object : TextWatcher {

            var edited = false
            val dividerCharacter = "-"

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edited) {
                    edited = false
                    return
                }

                var working = getEditText()

                working = manageDateDivider(working, 4, start, before)
                working = manageDateDivider(working, 7, start, before)

                edited = true
                date.setText(working)
                date.setSelection(date.text.length)

                for(orderWrapper in orderWrapperList)
                {
                    orderWrapper.didChange = true
                    orderWrapper.order.date = s.toString()
                }
            }

            private fun manageDateDivider(working: String, position : Int, start: Int, before: Int) : String{
                if (working.length == position) {
                    return if (before <= position && start < position)
                        working + dividerCharacter
                    else
                        working.dropLast(1)
                }
                return working
            }

            private fun getEditText() : String {
                return if (date.text.length >= 10)
                    date.text.toString().substring(0,10)
                else
                    date.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
                Log.d("textChange", " " + orderWrapperList[0].order.date)
                Log.d("textChange", " " + s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        }

        date.addTextChangedListener(mDateEntryWatcher)

        storage.addTextChangedListener(object:TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                for(orderWrapper in orderWrapperList)
                {
                    orderWrapper.didChange = true
                    orderWrapper.order.storage = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?){
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        customerOrSource.addTextChangedListener(object:TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                for(orderWrapper in orderWrapperList)
                {
                    orderWrapper.didChange = true
                    orderWrapper.order.customerOrSource = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        showGroupDetailViewModel.getCustomerOrSource().observe(viewLifecycleOwner, Observer {
            val customAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            customerOrSource.setAdapter(customAdapter)
        })

        isIn.setOnClickListener{

            if(isIn.text == getString(R.string.input))
            {
                isIn.setText(getString(R.string.output))
                customerOrSource.setHint(getString(R.string.customer))
                for(orderWrapper in orderWrapperList)
                {
                    orderWrapper.didChange = true
                    orderWrapper.order.isIn = false
                }
            }
            else
            {
                isIn.setText(getString(R.string.input))
                customerOrSource.setHint(getString(R.string.source))
                for(orderWrapper in orderWrapperList)
                {
                    orderWrapper.didChange = true
                    orderWrapper.order.isIn = true
                }
            }
        }

        var counter = 0
        Log.d("ShowGroupDetails", " " + orderWrapperList)
        for(orderWrapper in orderWrapperList)
        {
            counter += 1
            Log.d("ShowGroupDetails", " " + counter)
            createAViewDynamically(orderWrapper)
        }
    }

    fun createAViewDynamically(orderWrapper:OrderWrapper)
    {
        val aView = layoutInflater.inflate(R.layout.add_item, null, false)
        linContainer.addView(aView)

        val type = aView.findViewById<AutoCompleteTextView>(R.id.type)
        val color = aView.findViewById<AutoCompleteTextView>(R.id.color)
        val size = aView.findViewById<AutoCompleteTextView>(R.id.size)
        val amount = aView.findViewById<EditText>(R.id.amount)
        val cost = aView.findViewById<EditText>(R.id.cost)

        type.setText(orderWrapper.order.type)
        color.setText(orderWrapper.order.color)
        size.setText(orderWrapper.order.size)
        amount.setText(orderWrapper.order.amount.toString())
        cost.setText(orderWrapper.order.priceOrCost.toString())

        showGroupDetailViewModel.getTypes().observe(viewLifecycleOwner, Observer {
            val typeAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            type.setAdapter(typeAdapter)
        })

        showGroupDetailViewModel.getColors().observe(viewLifecycleOwner, Observer {
            val colorAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            color.setAdapter(colorAdapter)
        })

        type.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                orderWrapper.didChange = true
                orderWrapper.order.type = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                 //To change body of created functions use File | Settings | File Templates.
            }
        })

        color.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                orderWrapper.didChange = true
                orderWrapper.order.color = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })

        size.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                orderWrapper.didChange = true
                orderWrapper.order.size = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })

        amount.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                orderWrapper.didChange = true
                if(s?.length != 0) {
                    orderWrapper.order.amount = s.toString().toInt()
                }
                else
                {
                    orderWrapper.order.amount = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })

        cost.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                orderWrapper.didChange = true
                if(s?.length != 0)
                {
                    orderWrapper.order.priceOrCost = s.toString().toInt()
                }
                else
                {
                    orderWrapper.order.priceOrCost = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun testPart(date:String, stor:String, customer:String):Boolean
    {
        if(stor == "" || customer == "")
        {
            return false
        }

        if(date.length < 9)
        {
            return false
        }
        val month = date.substring(5, 7).toInt()
        if(month > 12)
        {
            return false
        }

        if(date.length == 9)
        {
            if(date[8].toInt() > 31)
            {
                return false
            }
        }

        if(date.length == 10)
        {
            if(date.substring(8, 10).toInt() > 31)
            {
                return false
            }
        }

        return true
    }

    fun checkOrders(orderWrapperList:List<OrderWrapper>):Boolean
    {
        for(orderWrapper in orderWrapperList)
        {
            if(orderWrapper.didChange)
            {
                if(orderWrapper.order.type == "")
                {
                    return false
                }
            }
        }

        return true
    }

    companion object
    {
        fun getInstance(groupId:String):ShowGroupDetailFragment
        {
            Log.d("ShowDetailsFragment", "getInstance called")
            val aFrag = ShowGroupDetailFragment()
            aFrag.arguments = Bundle().apply {
                putString(GROUP_ID, groupId)
            }

            return aFrag
        }
    }
}