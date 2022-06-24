package com.sandeep.studybear.activity.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.sandeep.studybear.R
import java.util.*

class EmailLoginActivity : AppCompatActivity() {
    lateinit var emailBox:EditText
    lateinit var passwordBox:EditText
    lateinit var button:Button
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)
        emailBox=findViewById(R.id.email_id_test)
        passwordBox=findViewById(R.id.password_test)
        button=findViewById(R.id.email_login_button_test)
        auth= FirebaseAuth.getInstance()
        button.setOnClickListener {
            val email:String = emailBox.text.toString().trim().lowercase(Locale.getDefault())
            val password:String=passwordBox.text.toString()

            if(email.isEmpty() || password.isEmpty())
            {
                Toast.makeText(this,"Fields cannot be left empty",Toast.LENGTH_LONG).show()
            }else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }

                    }
            }
        }



    }
}