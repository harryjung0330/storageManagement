package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.cuesta.sangwon_jung.storagemanagement.R
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.AddFragmentViewModel
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.StorageSelectFragViewModel


class StorageSelectFragment: Fragment()
{
    private val viewStorageViewModel: StorageSelectFragViewModel by lazy{
        ViewModelProviders.of(this).get(StorageSelectFragViewModel :: class.java)
    }
    private var adapter = StorageAdapter(emptyList())
    private lateinit var recycler_view: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater. inflate(R.layout.common_recycler_view, container, false)
        recycler_view = view.findViewById<RecyclerView>(R.id.common_recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var liveData = viewStorageViewModel.getStorage()
        liveData.observe(viewLifecycleOwner, Observer { list ->
            Log.d("getStorage", " " + list + " " + list.size)
            adapter = StorageAdapter(list)
            recycler_view.adapter = adapter
        })

    }

    private inner class StorageHolder(val aView:View): RecyclerView.ViewHolder(aView), View.OnClickListener
    {
        val textView = aView.findViewById<TextView>(R.id.textView16)

        init
        {
            textView.setOnClickListener(this)
        }
        fun bind(storageName:String)
        {
            textView.setText(storageName)
        }

        override fun onClick(v: View?) {
            val typeSelectFragment = TypeSelectFragment.newInstance(textView.text.toString())
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.view_storage, typeSelectFragment)
                .addToBackStack(null).commit()

        }
    }

    private inner class StorageAdapter(val storageList: List<String>): RecyclerView.Adapter<StorageHolder>()
    {
        override fun getItemCount(): Int {
            Log.d("getStorage", "list size:" + storageList.size)
            return storageList.size
        }

        override fun onBindViewHolder(holder: StorageHolder, position: Int) {
            var bindCount = 0
            Log.d("getStorage", "count:" + bindCount)
            bindCount += 1
            val str = storageList[position]?:"null"
            holder.bind(str)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageHolder {
            val view = layoutInflater.inflate(R.layout.item_textview, parent, false)

            return StorageHolder(view)
        }
    }

    companion object
    {
        fun createInstance():StorageSelectFragment
        {
            return StorageSelectFragment()
        }
    }
}