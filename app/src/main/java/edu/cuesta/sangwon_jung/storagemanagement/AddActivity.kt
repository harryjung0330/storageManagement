package edu.cuesta.sangwon_jung.storagemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.cuesta.sangwon_jung.storagemanagement.fragment.AddFragment
import edu.cuesta.sangwon_jung.storagemanagement.fragment.MainPageFragment

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        if (savedInstanceState == null) {
            val fragment = AddFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_add, fragment)
                .commit()
        }

    }
}
