package com.axactstudios.fooddelivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        btn_goToSignUp2.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        btn_login.setOnClickListener{
            doLogin()
        }
    }
    private fun doLogin() {
        if (txt_loginemail.text.toString().isEmpty()) {
            txt_loginemail.error = "Please enter email"
            txt_loginemail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txt_loginemail.text.toString()).matches()) {
            txt_loginemail.error = "Please enter valid email"
            txt_loginemail.requestFocus()
            return
        }

        if (txt_loginpassword.text.toString().isEmpty()) {
            txt_loginpassword.error = "Please enter password"
            txt_loginpassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(txt_loginemail.text.toString(), txt_loginpassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else{
                Toast.makeText(
                    baseContext, "Please verify your email address.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                baseContext, "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
