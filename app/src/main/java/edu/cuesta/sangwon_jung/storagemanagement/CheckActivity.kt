package edu.cuesta.sangwon_jung.storagemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.cuesta.sangwon_jung.storagemanagement.fragment.AddFragment
import edu.cuesta.sangwon_jung.storagemanagement.fragment.CheckFragment

class CheckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        if (savedInstanceState == null) {
            val fragment = CheckFragment.getInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.check_container, fragment)
                .commit()
        }
    }
}
