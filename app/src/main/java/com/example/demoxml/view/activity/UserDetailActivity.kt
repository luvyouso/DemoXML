package com.example.demoxml.view.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.demoxml.R
import com.example.demoxml.model.User
import com.example.demoxml.utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL
import java.util.*
import java.util.Arrays.asList

class UserDetailActivity : AppCompatActivity() {

    private var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        intent.extras.let {
            val tempUser = it[MainActivity.EXTRA_DATA]
            mUser = Gson().fromJson(tempUser.toString(), User::class.java)

            mEditTextName.setText(mUser?.name.toString())
            mEditTextQuantity.setText(mUser?.quantity.toString())
        }


        mButtonSubmit.setOnClickListener {
            mProgressBar.visibility = View.VISIBLE
            Utils.hideSoftKeyboard(this)
            updateData().execute()

        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class updateData() : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)

                val client = XmlRpcClient()
                val commonConfig = XmlRpcClientConfigImpl()
                commonConfig.serverURL = URL(String.format(MainActivity.URL_COMMON, MainActivity.BASE_URL))
                val uid = client.execute(
                    commonConfig, MainActivity.AUTHENTICATE,
                    asList<Any>(
                        MainActivity.DATABASE,
                        MainActivity.USERNAME,
                        MainActivity.PASSWORD,
                        emptyMap<Any, Any>()
                    )
                ) as Int

                val models = object : XmlRpcClient() {
                    init {
                        setConfig(object : XmlRpcClientConfigImpl() {
                            init {
                                serverURL = URL(String.format(MainActivity.URL_OBJECT, MainActivity.BASE_URL))
                            }
                        })
                    }
                }
                models.execute("execute_kw", asList<Any>(
                    MainActivity.DATABASE, uid, MainActivity.PASSWORD,
                    MainActivity.MODEL, MainActivity.WRITE,
                    asList(
                        asList(mUser?.id),
                        object : HashMap<Any, Any>() {
                            init {
                                put("name", mEditTextName.text.toString())
                                put("quantity", mEditTextQuantity.text.toString())
                            }
                        }
                    )
                ))


            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            mProgressBar.visibility = View.GONE
            runOnUiThread {
                finish()
            }
        }
    }
}
