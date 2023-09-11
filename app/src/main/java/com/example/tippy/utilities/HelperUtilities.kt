package com.example.tippy.utilities

import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar

//Helper utility to hold functions that can be resused
object HelperUtilities {
    //Helper object that checks if current Android OS running on Device is API 31 and above
    val isOSSandAbove: Boolean
        get() {
            val sdkInt = Build.VERSION.SDK_INT
            return sdkInt >= 31
        }

    //Function to show snackbar with a message within a parent layout
    fun showSnackBarInView(parentLayout: View, message:String){
        Snackbar.make(parentLayout,  message, Snackbar.LENGTH_SHORT)
            .show()
    }
}