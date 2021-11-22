package edu.cuesta.sangwon_jung.storagemanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.cuesta.sangwon_jung.storagemanagement.R
import edu.cuesta.sangwon_jung.storagemanagement.api.ShowGroupReciever
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.AddFragmentViewModel
import edu.cuesta.sangwon_jung.storagemanagement.viewModel.ShowGroupViewModel
import org.w3c.dom.Text
import java.security.acl.Group
import java.util.*

class ShowGroupsFragment: Fragment()
{
    private lateinit var groupRecyclerView:RecyclerView
    private lateinit var previousButton:Button
    private lateinit var nextButton: Button
    private val showGroupViewModel: ShowGroupViewModel by lazy{
        ViewModelProviders.of(this).get(ShowGroupViewModel :: class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aView = layoutInflater.inflate(R.layout.fragment_show_groups, container, false)

        return aView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        groupRecyclerView = view.findViewById(R.id.group_recycler_view)
        previousButton = view.findViewById(R.id.previous_button)
        nextButton = view.findViewById(R.id.next_button)

        groupRecyclerView.layoutManager = LinearLayoutManager(context)
        groupRecyclerView.adapter = GroupAdapter(emptyList())

        var aLiveData:LiveData<List<ShowGroupReciever>> = requestCurrentGroups()

        aLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it.size > 0)
            {
                groupRecyclerView.adapter = GroupAdapter(it)
            }
            else
            {
                disableIfEmpty()
            }
        })

        nextButton.setOnClickListener{
            aLiveData = showGroupViewModel.getNext()
            aLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                groupRecyclerView.adapter = GroupAdapter(it)
                if(it.size <= 0)
                {
                    disableIfEmpty()
                }

            })
        }

        previousButton.setOnClickListener{
            val temp = showGroupViewModel.getPrevious()
            if(temp != null)
            {
                aLiveData = temp
                aLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    groupRecyclerView.adapter = GroupAdapter(it)
                    enableNext()
                })
            }
        }


    }

    fun disableIfEmpty()
    {
        nextButton.isEnabled = false
    }

    fun enableNext()
    {
        nextButton.isEnabled = true
    }

    fun requestCurrentGroups():LiveData<List<ShowGroupReciever>>
    {
        return showGroupViewModel.getCurrentGroup()
    }

    private inner class GroupHolder(val aView:View):RecyclerView.ViewHolder(aView), View.OnClickListener
    {
        val customerOrSource = aView.findViewById<TextView>(R.id.group_customer_or_source)
        val isIn = aView.findViewById<TextView>(R.id.group_isIn)
        val date = aView.findViewById<TextView>(R.id.group_date)
        var groupId: String? = null

        init{
            aView.setOnClickListener(this)
        }
        fun bind(temp:ShowGroupReciever)
        {
            groupId = temp.groupId
            customerOrSource.setText(temp.customerOrSource)
            if(temp.isIn == 1)
            {
                isIn.setText(getString(R.string.input))
            }
            else
            {
                isIn.setText(getString(R.string.output))
            }
            date.setText(temp.date)

        }

        override fun onClick(v: View?) {
            val showGroupDetailFragment = ShowGroupDetailFragment.getInstance(groupId?:"")
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.show_recent_container, showGroupDetailFragment)
                .addToBackStack(null).commit()

        }
    }

    private inner class GroupAdapter(val aList:List<ShowGroupReciever>):RecyclerView.Adapter<GroupHolder>()
    {
        override fun getItemCount(): Int {
            return aList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
            val aView = layoutInflater.inflate(R.layout.show_group_item, parent, false)

            return GroupHolder(aView)
        }

        override fun onBindViewHolder(holder: GroupHolder, position: Int) {
            holder.bind(aList[position])
        }
    }

    companion object
    {
        fun getInstance():ShowGroupsFragment
        {
            return ShowGroupsFragment()
        }
    }






}