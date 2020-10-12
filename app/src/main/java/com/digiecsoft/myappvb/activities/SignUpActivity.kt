package com.digiecsoft.myappvb.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.digiecsoft.myappvb.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        buttonGoToLogin.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(
                    password,
                    confirmPassword
                )
            ) {
                signUpByEmail(email, password)
            } else {
                toast(resources.getText(R.string.sign_up_message_1))
            }
        }

        editTextEmail.validate {
            editTextEmail.error =
                if (isValidEmail(it)) null else resources.getText(R.string.sign_up_message_2)
        }

        editTextPassword.validate {
            editTextPassword.error =
                if (isValidPassword(it)) null else resources.getText(R.string.sign_up_message_3)
        }

        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if (isValidConfirmPassword(
                    editTextPassword.text.toString(),
                    it
                )
            ) null else resources.getText(R.string.sign_up_message_4)
        }

    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this) {
                    toast(resources.getText(R.string.sign_up_message_5))
                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseNetworkException) {
                    toast("${resources.getText(R.string.firebase_message_2)}")
                } catch (e: FirebaseAuthUserCollisionException) {
                    toast("${resources.getText(R.string.sign_up_message_12)}")
                    editTextEmail.requestFocus()
                } catch (e: Exception) {
                    toast("${resources.getText(R.string.sign_up_message_6)}")
                }
            }
        }
    }
}