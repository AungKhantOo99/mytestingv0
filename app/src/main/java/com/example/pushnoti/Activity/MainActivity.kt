package com.example.pushnoti.Activity

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pushnoti.Download.FileDownloader
import com.example.pushnoti.Notification.Notification
import com.example.pushnoti.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class MainActivity : AppCompatActivity(){
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var login:Button
    private lateinit var autorefresh:Button
    private lateinit var googlesiginin:Button
    private lateinit var download:Button
    private lateinit var playmusic:Button
    private lateinit var pagination:Button
    private val REQ_ONE_TAP = 2
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)
        login=findViewById(R.id.login)
        autorefresh=findViewById(R.id.goresult)
        googlesiginin=findViewById(R.id.googlesiginin)
        download=findViewById(R.id.download)
        playmusic=findViewById(R.id.playmusic)
        pagination=findViewById(R.id.pagination)
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        firebaseAuth= FirebaseAuth.getInstance()

        googlesiginin.setOnClickListener {
            Toast.makeText(this,"Logging In",Toast.LENGTH_SHORT).show()
            val signInIntent: Intent =mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent,REQ_ONE_TAP)
        }
        pagination.setOnClickListener {
            startActivity(Intent(this,PaginationActivity::class.java))
        }
        playmusic.setOnClickListener {

            startActivity(Intent(this,PlayMusicActivity::class.java))
        }
        download.setOnClickListener {
            val fileDownloader = FileDownloader(this)
            fileDownloader.downloadFile("https://randomuser.me/api/portraits/thumb/men/56.jpg","checkimg.jpg")
        }
        autorefresh.setOnClickListener {
            startActivity(Intent(this, RandomActivity::class.java))
        }

        FirebaseMessaging.getInstance().subscribeToTopic("HELLO")
            .addOnCompleteListener { task ->
                var msg = "Done"
                if (!task.isSuccessful) {
                    msg = "Failed"
                }
                Log.d("TAG", "Reass is "+ msg)
            }
    login.setOnClickListener {
        if(email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()){
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (!keyguardManager.isDeviceSecure) {
                notifyUser("The device does not support Confirm Credential")
            }
            val intent = keyguardManager.createConfirmDeviceCredentialIntent("Confirmation Need", "Your Device password")
            if (intent != null) {
                startActivityForResult(intent, 1)
            } else {
                // Confirm Credential is not available or not set up on the device
            }
        }else{
            notifyUser("Enter filed")
        }

    }

    }

    override fun onStart() {
        super.onStart()
        if(!checkPermission()){
            requestPermission()
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    notifyUser("Confirmation success")
                    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this@MainActivity) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Check", "signInWithEmail:success")
                                val user = firebaseAuth.currentUser
                              notifyUser("Success with id$user")
                                startActivity(Intent(this, PhoneActivity::class.java))
                                Notification.showNotification(
                                    this@MainActivity,
                                    MainActivity::class.java,
                                    "Success",
                                    "Welcome"
                                )
                                //  updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Check", "signInWithEmail:failure", task.exception)
                              notifyUser("Sign up fail")
                                //  updateUI(null)
                            }
                        }
                } else {
                    notifyUser("Fail")
                }
            }
            REQ_ONE_TAP -> {
                try {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account: GoogleSignInAccount? =task.getResult(ApiException::class.java)
                    if (account != null) {
                        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
                        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
                            if(task.isSuccessful) {
                                Toast.makeText(this,"Logging In Success",Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                } catch (e: ApiException){
                    Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun checkPermission(): Boolean {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val result = ContextCompat.checkSelfPermission(this, permission)
        return result == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermissions(permission, 100)
    }
}