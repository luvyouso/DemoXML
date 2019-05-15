package com.example.demoxml.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {

    fun hideSoftKeyboard(act: Activity) {
        val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = act.currentFocus
        if (v != null) {
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun showSoftKeyboard(act: Activity, view: View) {
        val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

}