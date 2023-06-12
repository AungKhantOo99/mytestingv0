package com.example.pushnoti.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.pushnoti.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {
    private val auth=FirebaseAuth.getInstance()
    private val TAG="AUth"
    lateinit var phone:EditText
    lateinit var edtcode:EditText
    lateinit var summit:Button
    lateinit var verify:Button
    var verificationIdforcode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        auth.setLanguageCode("mm")
        phone=findViewById(R.id.phone)
        edtcode=findViewById(R.id.code)
        summit=findViewById(R.id.summit)
        verify=findViewById(R.id.verify)
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                val code=credential.smsCode
                edtcode.setText(code)
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
         //       val credential = PhoneAuthProvider.getCredential(verificationId!!, code.text.toString())
                // Save verification ID and resending token so we can use them later
//                storedVerificationId = verificationId
//                resendToken = token
                verificationIdforcode=verificationId
            }
        }
        summit.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone.text.toString()) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        verify.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(verificationIdforcode!!, edtcode.text.toString())
            signInWithPhoneAuthCredential(credential)
        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}