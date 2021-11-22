package edu.cuesta.sangwon_jung.storagemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import edu.cuesta.sangwon_jung.storagemanagement.data.UserPs
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var sendButton: Button
    private lateinit var id:EditText
    private lateinit var password:EditText
    private val aRep = StorageRepository.getInstance()
    private var userPs:UserPs? = null
    private var isNew = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton = findViewById(R.id.send_button)
        id = findViewById(R.id.id_input)
        password = findViewById(R.id.password_input)

        val aLiveData = aRep.queryUserPs()
        aLiveData.observe(this, Observer {
            if(it.size != 0)
            {
                userPs = it[0]
                id.setText(userPs?.userName?:"")
                password.setText(userPs?.password?:"")
                isNew = false
            }
            else
            {
                userPs = UserPs(userName = "", password = "")
            }
        })



        sendButton.setOnClickListener{
            sendButton.isEnabled = false
            GlobalScope.launch(Dispatchers.Main) {
                val tempId = id.text.toString()
                val tempPs = password.text.toString()
                val user = aRep.logIn(tempId, tempPs).await()

                if (user?.authenticated ?: false)
                {
                    if(userPs == null)
                    {
                        userPs = UserPs(userName = id.text.toString(), password = password.text.toString())
                    }
                    else
                    {
                        userPs!!.userName = id.text.toString()
                        userPs!!.password = password.text.toString()
                    }

                    if(isNew)
                    {
                        aRep.addUserAndPs(userPs!!)
                    }
                    else
                    {
                        aRep.updateUserPs(userPs!!)
                    }

                    this@MainActivity.sendIntent()
                } else {
                    val msg: String = user?.token ?: getString(R.string.log_in_error)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                }

            }
            sendButton.isEnabled = true
        }

    }

    private fun sendIntent()
    {
        val aIntent = Intent(this, MainPageActivity::class.java)
        startActivity(aIntent)
    }
}
