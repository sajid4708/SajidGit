package com.sajid.zohogitapp.common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
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
}