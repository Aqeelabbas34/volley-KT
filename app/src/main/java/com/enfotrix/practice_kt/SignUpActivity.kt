package com.enfotrix.practice_kt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.enfotrix.practice_kt.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private val db=Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
        signUpBtn.setOnClickListener{
            val fName = fNameET.text.toString()
            val lName = LNameET.text.toString()
            val mail = EmailBox.text.toString()
            val pass = PasswordBox.text.toString()
            val modelUser = ModelUser(firstName = fName, lastName = lName, email = mail, password = pass)
            lifecycleScope.launch { saveUser(modelUser) }
        }
        }
    }
   private suspend fun saveUser(modelUser:ModelUser)
    {
       withContext(Dispatchers.IO){
           try {
               db.collection("User")
                   .add(modelUser)
                   .await()
           }
           catch (e:Exception){
               withContext(Dispatchers.Main){
                   Toast.makeText(this@SignUpActivity,"Failed",Toast.LENGTH_SHORT).show()
               }
               return@withContext
           }
       }
           withContext(Dispatchers.Main){
               Toast.makeText(this@SignUpActivity,"User Saved",Toast.LENGTH_SHORT).show()
               startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
           }


    }
}