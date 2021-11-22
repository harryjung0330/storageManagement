package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
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
import edu.cuesta.sangwon_jung.storagemanagement.api.Check
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.AddFragmentViewModel
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.CheckViewModel
import kotlinx.android.synthetic.main.add_item.*

private const val TYPE_ARR_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CheckFragment.type_arr_key"
private const val COLOR_ARR_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CheckFragment.color_arr_key"
private const val SIZE_ARR_KEY = "edu.cuesta.sangwon_jung.storagemanagement.CheckFragment.size_arr_key"
private const val AMOUNT_ARR_KEY = "edu.cuesta.sangwon.storagemanagement.CheckFragment.amount_arr_key"

class CheckFragment:Fragment()
{
    private lateinit var checkLinContainer:LinearLayout
    private lateinit var addViewButton: Button
    private lateinit var sendCheckButton:Button
    private val checkViewModel: CheckViewModel by lazy{
        ViewModelProviders.of(this).get(CheckViewModel :: class.java)
    }
    private var isStopped = false

    private val typeList: MutableList<AutoCompleteTextView> = mutableListOf()
    private val colorList:MutableList<AutoCompleteTextView> = mutableListOf()
    private val sizeList: MutableList<AutoCompleteTextView> = mutableListOf()
    private val amountList:MutableList<TextView> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState != null)
        {
            val tList = savedInstanceState.getStringArrayList(TYPE_ARR_KEY)
            val cList = savedInstanceState.getStringArrayList(COLOR_ARR_KEY)
            val sList = savedInstanceState.getStringArrayList(SIZE_ARR_KEY)
            val aList = savedInstanceState.getStringArrayList(AMOUNT_ARR_KEY)

            checkViewModel.takeData(tList, cList, sList, aList)


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aView = inflater.inflate(R.layout.fragment_order_check, container, false)

        return aView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var aLiveData: LiveData<List<Int>>

        sendCheckButton = view.findViewById(R.id.send_check_button)
        checkLinContainer = view.findViewById(R.id.item_container)
        addViewButton = view.findViewById(R.id.check_add_view_button)

        sendCheckButton.setOnClickListener{

            val checkList = mutableListOf<Check>()
            for(index in 0 until typeList.size)
            {
                checkList.add(Check(typeList[index].text.toString(), colorList[index].text.toString(), sizeList[index].text.toString()))
            }

            aLiveData = checkViewModel.sendCheckList(checkList)
            aLiveData.observe(viewLifecycleOwner, Observer {
                for(index in 0 until amountList.size)
                {
                    amountList[index].setText(it[index].toString())
                }
            })
        }

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
        initView()

        isStopped = true
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        val tList = ArrayList<String>()
        val cList = ArrayList<String>()
        val sList = ArrayList<String>()
        val aList = ArrayList<String>()

        for(index in 0 until typeList.size)
        {
            tList.add(typeList[index].text.toString())
            cList.add(colorList[index].text.toString())
            sList.add(sizeList[index].text.toString())
            aList.add(amountList[index].text.toString())
        }

        outState.putStringArrayList(TYPE_ARR_KEY, tList)
        outState.putStringArrayList(COLOR_ARR_KEY, cList)
        outState.putStringArrayList(SIZE_ARR_KEY, sList)
        outState.putStringArrayList(AMOUNT_ARR_KEY, aList)


    }

    fun initView()
    {
        val tList = checkViewModel.typeList
        val cList = checkViewModel.colorList
        val sList = checkViewModel.sizeList
        val aList = checkViewModel.amountList


        if(tList.size == 0)
        {
            createViewDynamically()
            return
        }
        else
        {
            for(index in 0 until tList.size)
            {
                createViewDynamically()
                typeList[index].setText(tList[index])
                //Log.d("CheckFragment", " " + typeList[index].text.toString())
                colorList[index].setText(cList[index])
                sizeList[index].setText(sList[index])
                amountList[index].setText(aList[index])
            }
        }
    }

    fun createViewDynamically()
    {
        val aView = layoutInflater.inflate(R.layout.checked_item, null, false)
        checkLinContainer.addView(aView)

        val type = aView.findViewById<AutoCompleteTextView>(R.id.type_check)
        val color = aView.findViewById<AutoCompleteTextView>(R.id.color_check)
        val size = aView.findViewById<AutoCompleteTextView>(R.id.size_check)
        val amount = aView.findViewById<TextView>(R.id.amount_check)

        checkViewModel.getTypes().observe(viewLifecycleOwner, Observer {
            val typeAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            type.setAdapter(typeAdapter)
        })

        checkViewModel.getColors().observe(viewLifecycleOwner, Observer {
            val colorAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            color.setAdapter(colorAdapter)
        })

        viewsCreated(type, color, size, amount)
    }

    fun viewsCreated(type:AutoCompleteTextView, color:AutoCompleteTextView, size:AutoCompleteTextView, amount: TextView)
    {
        typeList.add(type)
        colorList.add(color)
        sizeList.add(size)
        amountList.add(amount)
    }

    companion object
    {
        fun getInstance():CheckFragment
        {
            return CheckFragment()
        }
    }

}