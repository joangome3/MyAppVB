package com.digiecsoft.myappvb.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.digiecsoft.myappvb.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.editTextEmail
import kotlinx.android.synthetic.main.activity_login.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        editTextEmail.validate {
            editTextEmail.error =
                if (isValidEmail(it)) null else resources.getText(R.string.sign_up_message_2)
        }

        buttonGoToLogin.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        buttonForgotPassword.setOnClickListener {
            val email = editTextEmail.text.toString()
            if (isValidEmail(email)) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        toast(resources.getText(R.string.sign_up_message_9))
                        goToActivity<LoginActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseNetworkException) {
                            toast("${resources.getText(R.string.firebase_message_2)}")
                        } catch (e: FirebaseAuthInvalidUserException) {
                            toast("${resources.getText(R.string.login_message_1)}")
                            editTextEmail.requestFocus();
                        } catch (e: Exception) {
                            toast("${resources.getText(R.string.sign_up_message_6)}")
                        }
                    }
                }
            } else {
                toast(R.string.sign_up_message_10)
            }
        }
    }
}