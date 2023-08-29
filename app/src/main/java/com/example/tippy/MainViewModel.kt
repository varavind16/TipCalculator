package com.example.tippy
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
//import android.text.Editable
//import android.text.TextWatcher
import android.util.Log
import android.widget.*


class MainViewModel: ViewModel() {

    lateinit var etBillAmount: EditText
    lateinit var seekBarTip: SeekBar
    lateinit var tvPercent: TextView
    lateinit var tvTipAmount: TextView
    lateinit var tvTotalAmount: TextView
    fun computeTipandTotal() {
        if (etBillAmount.text.isEmpty()){
            tvTotalAmount.text = ""
            tvTipAmount.text = ""
            return
        }
        // 1. Get the values of tip and total
        val BaseAmount = etBillAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // 2. Calculate tip and total
        val tipAmount = BaseAmount * tipPercent / 100
        val totalAmount = BaseAmount + tipAmount
        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}