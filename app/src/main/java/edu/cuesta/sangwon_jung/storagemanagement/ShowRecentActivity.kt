package edu.cuesta.sangwon_jung.storagemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.cuesta.sangwon_jung.storagemanagement.fragment.AddFragment
import edu.cuesta.sangwon_jung.storagemanagement.fragment.ShowGroupsFragment

class ShowRecentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_recent)

        if (savedInstanceState == null) {
            val fragment = ShowGroupsFragment.getInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.show_recent_container, fragment)
                .commit()
        }
    }
}
