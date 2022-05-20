package com.example.studybear.activity.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.studybear.R
import com.example.studybear.activity.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import org.json.JSONObject


class RazorPayDataActivity : AppCompatActivity(),PaymentResultListener {

    lateinit var textName:TextView
    lateinit var textEmail:TextView
    lateinit var editPhone:EditText
    lateinit var spinnerSemester:PowerSpinnerView
    lateinit var buttonPay:Button
    private lateinit var auth:FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_razor_pay_data)
        textName=findViewById(R.id.txtRazorName)
        textEmail=findViewById(R.id.txtRazorEmail)
        editPhone=findViewById(R.id.edtRazorPhone)
        spinnerSemester=findViewById(R.id.spinnerRazor)
        buttonPay=findViewById(R.id.btnRazorPay)
        auth= FirebaseAuth.getInstance()
        val current_user=auth.currentUser
        var semester:String="Demo"
        var flag=false
        val arraySemester=resources.getStringArray(R.array.semesters)
        textName.text=current_user!!.displayName
        textEmail.text=current_user.email
        spinnerSemester.lifecycleOwner= MainActivity()//prevent memory leakage
        spinnerSemester.setOnSpinnerItemSelectedListener(object : OnSpinnerItemSelectedListener<Any?>
        {
            override fun onItemSelected(oldIndex: Int, oldItem: Any?, newIndex: Int, newItem: Any?) {
                //TODO
                //update the semester in the data base
                semester=newItem as String
                flag=true
            }

        })

        Checkout.preload(getApplicationContext());


        buttonPay.setOnClickListener {
            if((editPhone.text.toString().trim().length==10) && (arraySemester.contains(semester)==true))
            {
                if(ConnectionManager().checkConnectivity(this)==true)
                {

                        startPayment(current_user.email.toString(),current_user.displayName.toString(),editPhone.text.toString())
                }
                else{
                    ConnectionManager().createDialog(findViewById(R.id.parentRazor),this)
                }

            }else
            {
                val message=if(flag==false)
                {
                   "Choose a semester"
                }
                else
                {
                    "Enter 10 digit phone number"
                }
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

            }
        }


    }

    override fun onBackPressed() {

        if(spinnerSemester.isShowing)
        {
            spinnerSemester.dismiss()
        }else
        {
            super.onBackPressed()
        }

    }

// key_id
    //C55VyOvueLqmnfepkQKSAESt  secret


    private fun startPayment(email:String,name:String,phoneNumber:String)
    {

        val checkout = Checkout()
        checkout.setKeyID("rzp_test_fCzx5egRVmVFFA")
        checkout.setImage(R.drawable.company_logo)
        val activity: Activity = this
        try {
            val options = JSONObject()
            options.put("name", name)
            options.put("description", "Studybear access plan")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", "10000") //pass amount in currency subunits
            options.put("prefill.email",email )
            options.put("prefill.contact", phoneNumber)
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(this,"Sorry, Something went wrong..",Toast.LENGTH_SHORT).show()
            Log.e("Error", "Error in starting Razorpay Checkout", e);
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment successful",Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment unsuccessful",Toast.LENGTH_SHORT).show()
    }
}