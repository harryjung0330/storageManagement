package edu.cuesta.sangwon_jung.storagemanagement.api

import android.provider.Contacts
import android.util.Log
import androidx.annotation.MainThread
import edu.cuesta.sangwon_jung.storagemanagement.repository.StorageRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception
import java.util.concurrent.Executors

class LogInCall {
    private val baseUrl = "https://osd66owm36.execute-api.ap-northeast-2.amazonaws.com/test/"
    val retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create()).build()
    val storageApi = retrofit.create(StorageApi::class.java)

    fun logIn(userName: String, password: String): Deferred<User?>
    {
        val params = mapOf("userName" to userName, "password" to password)
        val logInCall: Call<User> = storageApi.logIn(params)

        var user:User? = null
        val def:Deferred<User?> = GlobalScope.async{
            try {
                val response: Response<User> = logInCall.execute()
                Log.d("logInCall", "response:" + response.body())
                user = response.body()
                Log.d("logInCall", "during call" + user)

                if (user?.authenticated ?: false)
                {
                    StorageRepository.getInstance().createCallMaker(user?.token ?: "")
                }
                else{

                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("logInCall", "Failed to execute")
                Log.e("logInCall", ex.toString())
            }
            user
        }



        Log.d("logInCall", "before return" + user)
        return def
    }
}

