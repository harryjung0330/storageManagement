package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import edu.cuesta.sangwon_jung.storagemanagement.R
import edu.cuesta.sangwon_jung.storagemanagement.data.Order
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.AddFragmentViewModel
import java.util.*


class AddFragment: Fragment()
{
    private lateinit var addViewButton:Button
    private lateinit var calculateButton:Button
    private lateinit var date:EditText
    private lateinit var addLinearLayout:LinearLayout
    private lateinit var inputOrOutput:TextView
    private lateinit var finishButton:Button
    private lateinit var customerOrSource:AutoCompleteTextView
    private lateinit var storage:EditText
    private val addViewModel: AddFragmentViewModel by lazy{
        ViewModelProviders.of(this).get(AddFragmentViewModel :: class.java)
    }
    private var isStopped = false



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        date = view.findViewById(R.id.date_edit_text)
        storage = view.findViewById(R.id.storage_number)
        customerOrSource = view.findViewById(R.id.customer_edit_text)
        inputOrOutput = view.findViewById<TextView>(R.id.inputOutput)
        calculateButton = view.findViewById(R.id.calculate_button)
        finishButton = view.findViewById(R.id.add_button_add_activity)

        finishButton.setOnClickListener {
            if(checkFirstType() && testPart(date.text.toString(), customerOrSource.text.toString()))
            {
                fillEmptyField()
                addViewModel.finish()
                finishButton.isEnabled = false
                addViewModel.isFinishButtonEnabled = false
            }
        }

        calculateButton.setOnClickListener{
            val tempList = addViewModel.filterOrder()
            val calFragment = CalculateFragment.getInstance(tempList)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.activity_add, calFragment)
                .addToBackStack(null).commit()
        }


        addLinearLayout = view.findViewById(R.id.add_linear_layout)
        addViewButton = view.findViewById(R.id.add_view_button)

        addViewButton.setOnClickListener{
            createViewDynamically()
        }
    }

    override fun onStart() {
        super.onStart()
        if(isStopped)
        {
            return
        }
        init()
        isStopped = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isStopped = false
    }

    fun init()
    {
        finishButton.isEnabled = addViewModel.isFinishButtonEnabled
        if(addViewModel.orderList.size == 0)
        {
            addViewModel.createOrder()
        }

        for(order in addViewModel.orderList)
        {
                createViewDynamically(order)
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

                addViewModel.orderList[0].date = s.toString()
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

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        }

        date.setText(addViewModel.orderList[0].date)
        date.addTextChangedListener(mDateEntryWatcher)

        storage.addTextChangedListener(object:TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addViewModel.orderList[0].storage = s.toString()
            }

            override fun afterTextChanged(s: Editable?){
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        customerOrSource.addTextChangedListener(object:TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addViewModel.orderList[0].customerOrSource = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        addViewModel.getCustomerOrSource().observe(viewLifecycleOwner, Observer {
            val customAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            customerOrSource.setAdapter(customAdapter)
        })

        if(addViewModel.orderList[0].isIn)
        {
            inputOrOutput.setText(getString(R.string.input))
            customerOrSource.setHint(getString(R.string.source))
        }
        else
        {
            inputOrOutput.setText(getString(R.string.output))
            customerOrSource.setHint(getString(R.string.customer))
        }

        inputOrOutput.setOnClickListener{

            if(inputOrOutput.text == getString(R.string.input))
            {
                inputOrOutput.setText(getString(R.string.output))
                customerOrSource.setHint(getString(R.string.customer))
                addViewModel.orderList[0].isIn = false
            }
            else
            {
                inputOrOutput.setText(getString(R.string.input))
                customerOrSource.setHint(getString(R.string.source))
                addViewModel.orderList[0].isIn = true
            }
        }
    }

    fun createViewDynamically()
    {
        createViewDynamically(addViewModel.createOrder())
    }

    fun createViewDynamically(order:Order)
    {
        val aView = layoutInflater.inflate(R.layout.add_item, null, false)
        addLinearLayout.addView(aView)

        val type = aView.findViewById<AutoCompleteTextView>(R.id.type)
        val color = aView.findViewById<AutoCompleteTextView>(R.id.color)
        val size = aView.findViewById<EditText>(R.id.size)
        val amount = aView.findViewById<EditText>(R.id.amount)
        val cost = aView.findViewById<EditText>(R.id.cost)

        addViewModel.getType().observe(viewLifecycleOwner, Observer {
            val typeAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            type.setAdapter(typeAdapter)
        })

        addViewModel.getColors().observe(viewLifecycleOwner, Observer {
            val colorAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            color.setAdapter(colorAdapter)
        })

        type.setText(order.type)
        color.setText(order.color)
        size.setText(order.size)
        amount.setText(order.amount.toString())
        cost.setText(order.priceOrCost.toString())

        type.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                order.type = s.toString()
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
                order.color = s.toString()
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
                order.size = s.toString()
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

                if(s?.length != 0) {
                    order.amount = s.toString().toInt()
                }
                else
                {
                    order.amount = 0
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

                if(s?.length != 0)
                {
                    order.priceOrCost = s.toString().toInt()
                }
                else
                {
                    order.priceOrCost = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    fun testPart(date:String, customer:String):Boolean
    {
        if( customer == "")
        {
            Toast.makeText(requireContext(), R.string.customer_missing, Toast.LENGTH_SHORT).show()
            return false
        }

        if(date == "")
        {
            return true
        }

        if(date.length < 9)
        {
            Toast.makeText(requireContext(), R.string.date_fill_error, Toast.LENGTH_SHORT).show()
            return false

        }
        val month = date.substring(5, 7).toInt()
        if(month > 12)
        {
            Toast.makeText(requireContext(), R.string.date_fill_error, Toast.LENGTH_SHORT).show()
            return false
        }

        if(date.length == 9)
        {
            if(date[8].toInt() > 31)
            {
                Toast.makeText(requireContext(), R.string.date_fill_error, Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if(date.length == 10)
        {
            if(date.substring(8, 10).toInt() > 31)
            {
                Toast.makeText(requireContext(), R.string.date_fill_error, Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }

    fun checkFirstType():Boolean
    {
        val tempBool = addViewModel.orderList[0].type == ""
        if(tempBool)
        {
            Toast.makeText(requireContext(), R.string.first_type_missin, Toast.LENGTH_SHORT).show()
        }

        return !tempBool

    }

    fun fillEmptyField()
    {
        if(date.text.toString() == "")
        {
            val tempDate = getCurrentDate()
            addViewModel.orderList[0].date = tempDate
            date.setText(tempDate)
        }

        if(storage.text.toString() == "")
        {
            addViewModel.orderList[0].storage = getString(R.string.default_storage)
            storage.setText(getString(R.string.default_storage))
        }
    }

    fun getCurrentDate():String
    {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DAY_OF_MONTH)

        return year.toString() + '-' + month.toString() + '-' + date.toString()
    }


    companion object
    {
        fun newInstance():AddFragment
        {
            return AddFragment()
        }
    }
}