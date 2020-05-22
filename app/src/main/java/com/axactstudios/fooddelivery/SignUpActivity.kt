package com.axactstudios.fooddelivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        btn_signup.setOnClickListener {
            signUpUser()
        }
        btn_goToLogin2.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
    private fun signUpUser() {
        if (txt_email.text.toString().isEmpty()) {
            txt_email.error = "Please enter email"
            txt_email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txt_email.text.toString()).matches()) {
            txt_email.error = "Please enter valid email"
            txt_email.requestFocus()
            return
        }

        if (txt_password.text.toString().isEmpty()) {
            txt_password.error = "Please enter password"
            txt_password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(txt_email.text.toString(), txt_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, PhoneLoginActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

