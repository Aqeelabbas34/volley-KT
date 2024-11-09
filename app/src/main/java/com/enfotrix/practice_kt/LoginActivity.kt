package com.enfotrix.practice_kt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.enfotrix.practice_kt.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            loginBtnId.setOnClickListener {
                val enteredMail = EmailET.text.toString()
                val enteredPass = PasswordET.text.toString()
                lifecycleScope.launch { userAuth(enteredMail, enteredPass) }
            }
        }
    }

    private suspend fun userAuth(mail: String, pass: String) {
        try {
            val querySnapshot = withContext(Dispatchers.IO) {
                db.collection("User")
                    .whereEqualTo("email", mail)
                    .get()
                    .await()
            }
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot) {
                    val modelUser = document.toObject(ModelUser::class.java)
                    if (modelUser.password == pass) {
                        withContext(Dispatchers.Main)
                        {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login successFull",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                        return
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                        return
                    }

                }

            } else {
                withContext(Dispatchers.Main) {

                    Toast.makeText(this@LoginActivity, "No user found", Toast.LENGTH_SHORT).show()

                }
            }


        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@LoginActivity,"Error While Fetching data",Toast.LENGTH_SHORT).show()
            }
        }
    }
}