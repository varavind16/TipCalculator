package com.example.tippy.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tippy.R
import com.example.tippy.constants.Constants
import com.example.tippy.databinding.ActivityMainBinding
import com.example.tippy.utilities.HelperUtilities
import com.example.tippy.utilities.SharedPrefsUtil
import com.example.tippy.utilities.SharedPrefsUtil.sharedPrefsInit


//Main activity for the app
class MainActivity : AppCompatActivity() {
    //We are using viewbinding instead of initialising variables
    //View binding replaces findviewbyid()
    private lateinit var binding: ActivityMainBinding

    private lateinit var currentSelectedCurrency: String
    private var dialog: AlertDialog? = null

    //Back button handling
    private var handlerOb: Handler? = null
    private var doubleBackToExitPressedOnce: Boolean = false
    private val mRunnableBackButton = Runnable { doubleBackToExitPressedOnce = false }

    private var curTotalTipPercent: Float? = null
    private var curTotalTipAmount: Double? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Initialise Shared Preferences
        sharedPrefsInit(this@MainActivity)

        currentSelectedCurrency = SharedPrefsUtil.getValueFromPreferences(
            this@MainActivity,
            Constants.SHARED_PREFS_SELECTED_CURRENCY
        )

        binding.changedefaultCurrencyButton.text = currentSelectedCurrency

        //We can replace any component in the xml file using the binding object for the associated class
        //This method is simple and replaces findviewbyid
        binding.seekBarTip.progress = Constants.INITIAL_TIP_PERCENT
        binding.tvPercent.text = "Tip % (" + Constants.INITIAL_TIP_PERCENT + ")"

        binding.seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.etBillAmount.clearFocus()
                Log.i(Constants.MAINACTIVITY_LOG_TAG, "onProgressChanged $p1")
                binding.tvPercent.text = "Tip % ($p1)"
                computeTipandTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        // Billamount logic
        binding.etBillAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(Constants.MAINACTIVITY_LOG_TAG, " Afterbillamount $p0")
                computeTipandTotal()
            }
        })


        //Launches an alert dialog when the change default currency button is pressed
        binding.changedefaultCurrencyButton.setOnClickListener {
            alertDialogLogic()
        }

        //This function handles the onBackpressed event for the main activity
        //Here we close the activity when pressing the back button twice using a hander and a variable
        //If a back button event is pressed, a snackbar view is shown and if a second back button event
        //is pressed withing 1.50 seconds, the app closes
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //part to handle back press
                handlerOb = Handler(Looper.getMainLooper())
                handlerOb?.postDelayed(mRunnableBackButton, 1500) // 1.50 seconds in ms

                if (doubleBackToExitPressedOnce) {
                    finish()
                    return
                } else {
                    val parentLayout = binding.constraintlayoutMain
                    HelperUtilities.showSnackBarInView(parentLayout, Constants.BACK_CLOSE_MESSAGE)
                }
                doubleBackToExitPressedOnce = true
            }
        })
    }


    @SuppressLint("SetTextI18n")
    //This function computes the tip for the selected base amount
    fun computeTipandTotal() {
        if (binding.etBillAmount.text.isEmpty()) {
            binding.tvTotalAmount.text = ""
            binding.tvTipAmount.text = ""
            return
        }

        // 1. Get the values of tip and total
        val baseAmount = binding.etBillAmount.text.toString().toDouble()
        val tipPercent = binding.seekBarTip.progress
        // 2. Calculate tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        // 3. Update the UI
        curTotalTipPercent = tipPercent.toFloat()
        curTotalTipAmount = totalAmount

        binding.tvTipAmount.text =
            "%.2f".format(tipAmount) + " " + currentSelectedCurrency.takeLast(2)
        binding.tvTotalAmount.text =
            "%.2f".format(totalAmount) + " " + currentSelectedCurrency.takeLast(2)
    }


    fun alertDialogLogic() {
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)
        val inflater = LayoutInflater.from(this@MainActivity)

        val nullParent: ViewGroup? = null
        val dialogView = inflater.inflate(R.layout.alert_currency_dialog, nullParent)
        dialogBuilder.setView(dialogView)

        val radioButtonUSD = dialogView.findViewById<RadioButton>(R.id.usdRadioButton)
        val radioButtonINR = dialogView.findViewById<RadioButton>(R.id.inrRadioButton)
        val radioButtoneuro = dialogView.findViewById<RadioButton>(R.id.euroRadioButton)
        val radioButtonpound = dialogView.findViewById<RadioButton>(R.id.poundRadioButton)
        val radioButtonyen = dialogView.findViewById<RadioButton>(R.id.japaneseYenRadioButton)
        val radioButtonyuan = dialogView.findViewById<RadioButton>(R.id.chineseYuanRadioButton)

        //Hides keyboard if open
        try {
            binding.etBillAmount.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        currentSelectedCurrency = SharedPrefsUtil.getValueFromPreferences(
            this@MainActivity,
            Constants.SHARED_PREFS_SELECTED_CURRENCY
        )

        if (currentSelectedCurrency == Constants.US_DOLLAR)
            radioButtonUSD.isChecked = true
        else if (currentSelectedCurrency == Constants.INDIAN_RUPEE)
            radioButtonINR.isChecked = true
        else if (currentSelectedCurrency == Constants.EURO)
            radioButtoneuro.isChecked = true
        else if (currentSelectedCurrency == Constants.BRITISH_POUND)
            radioButtonpound.isChecked = true
        else if (currentSelectedCurrency == Constants.JAPANESE_YEN)
            radioButtonyen.isChecked = true
        else if (currentSelectedCurrency == Constants.CHINESE_YUAN)
            radioButtonyuan.isChecked = true

        val dialogSave = dialogView.findViewById<Button>(R.id.dialog_ok)
        val dialogCancel = dialogView.findViewById<Button>(R.id.dialog_cancel)

        //Close the dialog on dismissing
        dialogCancel.setOnClickListener {
            if (dialog != null)
                dialog!!.dismiss()
        }


        //On clicking the dialog save button save the value
        dialogSave.setOnClickListener {
            if (radioButtonUSD.isChecked)
                currentSelectedCurrency = Constants.US_DOLLAR
            else if (radioButtonINR.isChecked)
                currentSelectedCurrency = Constants.INDIAN_RUPEE
            else if (radioButtoneuro.isChecked)
                currentSelectedCurrency = Constants.EURO
            else if (radioButtonpound.isChecked)
                currentSelectedCurrency = Constants.BRITISH_POUND
            else if (radioButtonyen.isChecked)
                currentSelectedCurrency = Constants.JAPANESE_YEN
            else if (radioButtonyuan.isChecked)
                currentSelectedCurrency = Constants.CHINESE_YUAN


            binding.changedefaultCurrencyButton.text = currentSelectedCurrency

            //Save the selected value to the shared preferences
            SharedPrefsUtil.saveValueToPreferences(
                this@MainActivity,
                Constants.SHARED_PREFS_SELECTED_CURRENCY,
                currentSelectedCurrency
            )

            val parentLayout = binding.constraintlayoutMain
            HelperUtilities.showSnackBarInView(parentLayout, "Selected $currentSelectedCurrency")


            try {
                binding.tvTipAmount.text =
                    "%.2f".format(curTotalTipPercent) + " " + currentSelectedCurrency.takeLast(2)
                binding.tvTotalAmount.text =
                    "%.2f".format(curTotalTipAmount) + " " + currentSelectedCurrency.takeLast(2)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            dialog?.dismiss()
        }

        dialog = dialogBuilder.create()
        try {
            if (!isFinishing)
                dialog?.show()
        } catch (e: Exception) {
            Log.d("Exception Handled", "Handled")
        }
    }

    //Activity onStop Method is executed when the app goes to background
    //We remove the callback for the handler
    override fun onStop() {
        try {
            if (handlerOb != null)
                handlerOb?.removeCallbacks(mRunnableBackButton)
        } catch (e: Exception) {
            Log.d("Exception Handled", "Handled")
        }
        super.onStop()
    }
}

