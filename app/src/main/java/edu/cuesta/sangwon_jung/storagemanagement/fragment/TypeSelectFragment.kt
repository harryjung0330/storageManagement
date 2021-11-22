package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.cuesta.sangwon_jung.storagemanagement.R
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.StorageSelectFragViewModel
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.TypeSelectFragViewModel
import kotlinx.android.synthetic.main.add_item.*

const private val STORAGE_NAME = "edu.cuesta.sangwon_jung.storagemanagement"

class TypeSelectFragment: Fragment()
{
    private val viewTypeViewModel: TypeSelectFragViewModel by lazy{
        ViewModelProvider(this).get(TypeSelectFragViewModel :: class.java)
    }
    private lateinit var storageName:String
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputType: EditText
    private lateinit var adapter:TypeAdapter
    private var typeList: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        storageName = arguments?.getString(STORAGE_NAME)?:"default"
        val view = inflater.inflate(R.layout.fragment_type_select, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<RecyclerView>(R.id.type_select_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TypeAdapter(emptyList())
        viewTypeViewModel.typeLiveData.observe(viewLifecycleOwner, Observer {
            typeList = it
            if(inputType.text.toString() == "")
            {
                recyclerView.adapter = TypeAdapter(it)
            }
        })

        inputType = view.findViewById(R.id.inputType)

        val textChangeWatcher = object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                val txt = inputType.text.toString()
                val aList = mutableListOf<String>()

                if(txt == "")
                {
                    adapter = TypeAdapter(typeList ?:emptyList())
                    updateUI()
                    return
                }

                for(str in typeList?: emptyList())
                {
                    if(str.contains(txt))
                    {
                        aList.add(str)
                    }
                }

                adapter = TypeAdapter(aList)
                updateUI()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        inputType.addTextChangedListener(textChangeWatcher)
        updateUI()
    }

    private fun updateUI()
    {
        recyclerView.adapter = adapter
    }

    private inner class TypeSelectHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener
    {
        val textView = itemView.findViewById<TextView>(R.id.textView16)

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(typeName: String)
        {
            textView.setText(typeName)
        }

        override fun onClick(v: View?)
        {
            val showDetailsFragment = ShowDetailsFragment.newInstance(storageName ,textView.text.toString() )
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.view_storage, showDetailsFragment)
                .addToBackStack(null).commit()
        }
    }

    private inner class TypeAdapter(val aList:List<String>): RecyclerView.Adapter<TypeSelectHolder>()
    {
        override fun getItemCount(): Int {
            return aList.size
        }

        override fun onBindViewHolder(holder: TypeSelectHolder, position: Int) {
            holder.bind(aList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeSelectHolder {
            val view = layoutInflater.inflate(R.layout.item_textview, parent, false)

            return TypeSelectHolder(view)
        }
    }
    companion object
    {
        fun newInstance(storageName:String):TypeSelectFragment
        {
            val frag = TypeSelectFragment()
            frag.arguments = Bundle().apply{
                putString(STORAGE_NAME, storageName)
            }

            return frag
        }
    }
}