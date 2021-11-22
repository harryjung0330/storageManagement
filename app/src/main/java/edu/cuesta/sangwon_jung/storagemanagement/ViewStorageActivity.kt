package edu.cuesta.sangwon_jung.storagemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.cuesta.sangwon_jung.storagemanagement.fragment.AddFragment
import edu.cuesta.sangwon_jung.storagemanagement.fragment.StorageSelectFragment

class ViewStorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_storage)

        if (savedInstanceState == null) {
            val fragment = StorageSelectFragment.createInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.view_storage, fragment)
                .commit()
        }
    }
}
