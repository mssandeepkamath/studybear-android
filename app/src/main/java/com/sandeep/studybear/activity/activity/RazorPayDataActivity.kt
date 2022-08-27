package com.sandeep.studybear.activity.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.sandeep.studybear.R
import com.sandeep.studybear.activity.model.UserDataClass
import com.sandeep.studybear.activity.util.ConnectionManager
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import org.json.JSONObject


class RazorPayDataActivity : AppCompatActivity() {

    lateinit var textName: TextView
    lateinit var textEmail: TextView
//    lateinit var editPhone: EditText
    lateinit var spinnerSemester: PowerSpinnerView
    lateinit var buttonPay: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var user: UserDataClass
    var reference: DatabaseReference? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_razor_pay_data)
        val mAdView1 = findViewById<AdView>(R.id.adViewPayment)
        val adRequest = AdRequest.Builder().build()
        mAdView1.loadAd(adRequest)
        Checkout.preload(getApplicationContext());
        textName = findViewById(R.id.txtRazorName)
        textEmail = findViewById(R.id.txtRazorEmail)
//        editPhone = findViewById(R.id.edtRazorPhone)
        spinnerSemester = findViewById(R.id.spinnerRazor)
        buttonPay = findViewById(R.id.btnRazorPay)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val current_user = auth.currentUser
        var semester: Int=4
        var flag = false
        val name = current_user!!.displayName
        val email = current_user.email
        val photourl = current_user.photoUrl
        textName.text = name
        textEmail.text = email
        val arraySemester = resources.getStringArray(R.array.semesters)
        spinnerSemester.lifecycleOwner = MainActivity()
        spinnerSemester.setOnSpinnerItemSelectedListener(object :
            OnSpinnerItemSelectedListener<Any?> {
            override fun onItemSelected(
                oldIndex: Int,
                oldItem: Any?,
                newIndex: Int,
                newItem: Any?,
            ) {
                semester = newIndex+1
                flag = true
            }

        })

        buttonPay.setOnClickListener {
            if(semester in 1..8)//phone number length should be 10 if there
            {
                if (ConnectionManager().checkConnectivity(this) == true) {

//                    val phonenumber = editPhone.text.toString()
                    user =
                    UserDataClass(name, email, "1234567890",
                            photourl.toString(), true, semester, 0, 0, 0, "1", "1", "1")
                    database.child("users").child(current_user.uid)
                        .setValue(user, object : DatabaseReference.CompletionListener {
                            override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                                reference = ref
                                if (error != null) {
                                    Toast.makeText(this@RazorPayDataActivity,
                                        "Sorry,Something went wrong..",
                                        Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    val intent=Intent(this@RazorPayDataActivity,AccountVerificationActivity()::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        })

                } else {
                    ConnectionManager().createDialog(findViewById(R.id.parentRazor), this)
                }
            } else {
                val message = if (flag == false) {
                    "Choose a semester"
                } else {
                    "Something went wrong"
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onBackPressed() {

        if (spinnerSemester.isShowing) {
            spinnerSemester.dismiss()
        } else {
            super.onBackPressed()
        }
    }

//    private fun startPayment(email: String, phoneNumber: String) {
//
//        val checkout = Checkout()
//        checkout.setKeyID("rzp_live_9JdTljlGTGUHDM")
//        checkout.setImage(R.drawable.company_logo)
//        val activity: Activity = this
//        try {
//            val options = JSONObject()
//            options.put("name", "Studybear")
//            options.put("description", "Studybear access plan")
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
//            options.put("theme.color", "#3399cc")
//            options.put("currency", "INR")
//            options.put("amount", "3000") //pass amount in currency subunits
//            options.put("prefill.email", email)
//            options.put("prefill.contact", phoneNumber)
//            val retryObj = JSONObject()
//            retryObj.put("enabled", true)
//            retryObj.put("max_count", 4)
//            options.put("retry", retryObj)
//            checkout.open(activity, options)
//        } catch (e: Exception) {
//            Toast.makeText(this, "Sorry, Something went wrong..", Toast.LENGTH_SHORT).show()
//            Log.e("Error", "Error in starting Razorpay Checkout", e);
//        }
//    }

//    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
//        val uid = auth.currentUser!!.uid
//        reference?.child("paidbit")?.setValue(true)
//        reference?.child("razorpayorderid")?.setValue(p1?.orderId)
//        reference?.child("razorpaysignature")?.setValue(p1?.signature)
//        reference?.child("razorpaypaymentid")?.setValue(p1?.paymentId)
//        Toast.makeText(this@RazorPayDataActivity, "Payment successful", Toast.LENGTH_SHORT).show()
//        val intent = Intent(this@RazorPayDataActivity, com.sandeep.studybear.activity.activity.AccountVerificationActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
//        Toast.makeText(this, "Payment unsuccessful", Toast.LENGTH_SHORT).show()
//        val intent = Intent(this@RazorPayDataActivity, com.sandeep.studybear.activity.activity.AccountVerificationActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

}