package edu.cuesta.sangwon_jung.storagemanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.cuesta.sangwon_jung.storagemanagement.fragment.MainPageFragment
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        if (savedInstanceState == null) {
            val fragment = MainPageFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_page_container, fragment)
                .commit()
        }

    }

}
