package com.example.tippy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
//import android.text.Editable
//import android.text.TextWatcher
import android.util.Log
import android.widget.*
//import android.widget.EditText
//import android.widget.SeekBar
//import android.widget.TextView

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBillAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBillAmount = findViewById(R.id.etBillAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvPercent = findViewById(R.id.tvPercent)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvPercent.text = "$INITIAL_TIP_PERCENT%"
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvPercent.text = "$p1%"
                computeTipandTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        // Billamount logic
        etBillAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterbillamount $p0")
                computeTipandTotal()
            }
        })
    }


    private fun computeTipandTotal() {
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