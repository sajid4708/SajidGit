package com.sajid.zohogitapp.common

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.sajid.zohogitapp.common.utils.ConnectivityReceiver

abstract class BaseActivity : AppCompatActivity(),ConnectivityReceiver.ConnectivityReceiverListener{
    fun dismissKeyBoard() {
        try {
            val token = findViewById<View>(android.R.id.content).windowToken
            val inputMethodManger =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManger?.hideSoftInputFromWindow(token, 0)
        }
        //This just catches exception to make sure app does not crash this will not impact user
        catch (e: Exception) {
            //This just catches exception to make sure app does not crash
        }
    }

    fun showKeyBoard(view:View){
        val lManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        lManager.showSoftInput(view, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }
}