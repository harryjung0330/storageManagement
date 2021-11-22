package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.cuesta.sangwon_jung.storagemanagement.*
import edu.cuesta.sangwon_jung.storagemanagement.api.Notification
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.AddFragmentViewModel
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.MainPageViewModel
import org.w3c.dom.Text

class MainPageFragment: Fragment()
{
    private lateinit var addButton: Button
    private lateinit var orderCheckButton:Button
    private lateinit var queryTextView: AutoCompleteTextView
    private lateinit var storageLook: Button
    private lateinit var queryImg: ImageView
    private lateinit var notifRecyc : RecyclerView
    private lateinit var showRecentButton:Button

    private val mainPageViewModel: MainPageViewModel by lazy{
        ViewModelProviders.of(this).get(MainPageViewModel :: class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_page, container, false)
        notifRecyc = view.findViewById(R.id.lacking_noti)
        notifRecyc.layoutManager = LinearLayoutManager(context)
        notifRecyc.adapter = NotificationAdapter(emptyList())


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addButton = view.findViewById(R.id.add_button)
        orderCheckButton = view.findViewById(R.id.order_check_button)
        queryTextView = view.findViewById(R.id.search_type_view)
        storageLook = view.findViewById(R.id.storage_look_button)
        queryImg = view.findViewById(R.id.query_image)
        showRecentButton = view.findViewById(R.id.show_recent_button)

        showRecentButton.setOnClickListener{
            val intent = Intent(requireContext(), ShowRecentActivity::class.java )
            startActivity(intent)
        }

        val recyLiveData = mainPageViewModel.getNotification()
        recyLiveData.observe(viewLifecycleOwner, Observer {
            notifRecyc.adapter = NotificationAdapter(it)
        })

        orderCheckButton.setOnClickListener{
            val intent = Intent(requireActivity(), CheckActivity::class.java)
            startActivity(intent)
        }


        val aLiveData = mainPageViewModel.aLiveData
        aLiveData.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_textview, it)
            queryTextView.setAdapter(adapter)
            Log.d("getAuto", " " + it)
        })

        queryImg.setOnClickListener{
            val type = queryTextView.text.toString()
            if(type != "")
            {
                val showDetailsFragment = ShowDetailsFragment.newInstance("all", type )
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_page_container, showDetailsFragment)
                    .addToBackStack(null).commit()
            }
        }

        storageLook.setOnClickListener{
            val intent = Intent(requireActivity(),ViewStorageActivity:: class.java)
            requireActivity().startActivity(intent)
        }

        addButton.setOnClickListener{
            val intent = Intent(requireActivity(), AddActivity::class.java)
            requireActivity().startActivity(intent)
        }
    }

    private inner class NotifHolder(val view: View):RecyclerView.ViewHolder(view){
        val type = itemView.findViewById<TextView>(R.id.notif_type)
        val color = itemView.findViewById<TextView>(R.id.notif_color)
        val size = itemView.findViewById<TextView>(R.id.notif_size)
        val amount = itemView.findViewById<TextView>(R.id.notif_amount)

        fun bind(aNotif:Notification)
        {
            type.setText(aNotif.type)
            color.setText(aNotif.color)
            size.setText(aNotif.size)
            amount.setText(aNotif.amount.toString())
        }
    }

    private inner class NotificationAdapter(val aList:List<Notification>): RecyclerView.Adapter<NotifHolder>()
    {
        override fun getItemCount(): Int {

            return aList.size
        }

        override fun onBindViewHolder(holder: NotifHolder, position: Int) {
            holder.bind(aList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifHolder {
            val aView = layoutInflater.inflate(R.layout.notif_item, parent, false)

            return NotifHolder(aView)
        }
    }

    companion object
    {
        fun newInstance():MainPageFragment
        {
            return MainPageFragment()
        }
    }
}