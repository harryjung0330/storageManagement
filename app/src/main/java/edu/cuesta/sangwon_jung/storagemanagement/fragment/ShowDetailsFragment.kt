package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import edu.cuesta.sangwon_jung.storagemanagement.R
import edu.cuesta.sangwon_jung.storagemanagement.api.DetailItem
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.DetailViewFragViewModel
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.StorageSelectFragViewModel

const private val STORAGE_NAME = "edu.cuesta.sangwon_jung.storagemanagement.ShowDetailsFragment.storageName"
const private val TYPE_NAME = "edu.cuesta.sangwon_jung.storagemanagement.ShowDetailsFragment.typeName"
class ShowDetailsFragment: Fragment()
{
    private val detailViewModel: DetailViewFragViewModel by lazy{
        ViewModelProviders.of(this).get(DetailViewFragViewModel :: class.java)
    }
    private lateinit var storName: String
    private lateinit var typeName: String
    private lateinit var textView: TextView
    private lateinit var linLayoutContainer:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        storName = arguments?.getString(STORAGE_NAME) ?:""
        typeName = arguments?.getString(TYPE_NAME) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_items, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.textView17)
        linLayoutContainer = view.findViewById(R.id.show_item_container)
        textView.setText(typeName)

        val aLiveData = detailViewModel.getDetails(storName, typeName)
        aLiveData.observe(viewLifecycleOwner, Observer {
            for(item in it)
            {
                createViewDynamically(item)
            }
        })

    }

    fun createViewDynamically(aDetailItem: DetailItem)
    {
        val aView = layoutInflater.inflate(R.layout.show_item, null, false)
        linLayoutContainer.addView(aView)

        val color = if(aDetailItem.color == ""){getString(R.string.none)} else {aDetailItem.color}
        val size = if(aDetailItem.size == ""){getString(R.string.none)} else {aDetailItem.size}
        val amount = aDetailItem.amount

        aView.findViewById<TextView>(R.id.textView12).setText(color)
        aView.findViewById<TextView>(R.id.textView13).setText(size)
        aView.findViewById<TextView>(R.id.textView14).setText(amount.toString())
    }

    companion object{
        fun newInstance(storageName:String, typeName:String): ShowDetailsFragment
        {
            val aFrag = ShowDetailsFragment()
            aFrag.arguments = Bundle().apply{
                putString(STORAGE_NAME, storageName)
                putString(TYPE_NAME, typeName)
            }

            return aFrag
        }
    }
}