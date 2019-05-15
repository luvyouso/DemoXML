package com.example.demoxml.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.demoxml.Adapter.AbstractAdapter
import com.example.demoxml.Adapter.UserAdapter
import com.example.demoxml.R
import com.example.demoxml.model.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL
import java.util.Arrays.asList
import java.util.HashMap


class MainActivity : AppCompatActivity(), AbstractAdapter.ListItemInteractionListener {

    companion object {
        const val BASE_URL = "http://18.216.8.144:8069"
        const val DATABASE = "odoo11-dev"
        const val PASSWORD = "12345"
        const val USERNAME = "demo@appex.co"
        const val URL_COMMON = "%s/xmlrpc/2/common"
        const val URL_OBJECT = "%s/xmlrpc/2/object"
        const val MODEL = "todo.task"
        const val AUTHENTICATE = "authenticate"
        const val SEARCH_READ = "search_read"
        const val WRITE = "write"
        const val EXTRA_DATA = "data_user"
    }

    private var mUserAdapter: UserAdapter? = null
    private var mListUser: MutableList<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListUser = ArrayList()
        mUserAdapter = UserAdapter(applicationContext, mListUser)
        mUserAdapter!!.setItemInteractionListener(this)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerViewUsers.layoutManager = mLayoutManager
        mRecyclerViewUsers.setHasFixedSize(true)
        mRecyclerViewUsers.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        mRecyclerViewUsers.adapter = mUserAdapter


    }

    override fun onInteraction(view: View, model: Any, position: Int) {
        val user = model as User
        val intent = Intent(applicationContext, UserDetailActivity::class.java)
        intent.putExtra(EXTRA_DATA, Gson().toJson(user))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        //fetch data
        mProgressBar.visibility = View.VISIBLE
        fetchData().execute()
    }


    @SuppressLint("StaticFieldLeak")
    inner class fetchData() : AsyncTask<Void, String, String>() {

        override fun doInBackground(vararg params: Void?): String? {
            var data = ""
            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)

                val client = XmlRpcClient()
                val commonConfig = XmlRpcClientConfigImpl()
                commonConfig.serverURL = URL(String.format(URL_COMMON, BASE_URL))
                val uid = client.execute(
                    commonConfig, AUTHENTICATE, asList<Any>(DATABASE, USERNAME, PASSWORD, emptyMap<Any, Any>())
                ) as Int
                Log.e("uid", uid.toString() + "")

                val models = object : XmlRpcClient() {
                    init {
                        setConfig(object : XmlRpcClientConfigImpl() {
                            init {
                                serverURL = URL(String.format(URL_OBJECT, BASE_URL))
                            }
                        })
                    }
                }

                val dataUser = asList(*models.execute("execute_kw", asList<Any>(
                    DATABASE, uid, PASSWORD,
                    MODEL, SEARCH_READ,
                    object : HashMap<Any, Any>() {
                        init {
                            asList<String>("domain", "=", "[]")
                            asList<Any>(
                                "fields",
                                "=",
                                asList<String>("name", "is_checked", "user_name", "date_submitted", "quantity")
                            )
                        }
                    }
                )) as Array<*>)
                data = Gson().toJson(dataUser)

            } catch (e: Exception) {
            }
            return data
        }

        override fun onPostExecute(result: String?) {
            mProgressBar.visibility = View.GONE
            if (result!!.isEmpty()){
                Toast.makeText(applicationContext, "Please, check internet!", Toast.LENGTH_LONG).show()
                return
            }
            if (mListUser?.size!! > 0) mListUser?.clear()
            val data = Gson().fromJson(result, Array<User>::class.java)
            mListUser!!.addAll(data)
            mUserAdapter?.notifyDataSetChanged()
        }
    }
}
