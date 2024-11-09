package com.enfotrix.practice_kt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.enfotrix.practice_kt.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            signUpBtn.setOnClickListener {
                val fName = fNameET.text.toString()
                val lName = LNameET.text.toString()
                val phone = phoneBox.text.toString()
                val pass = PasswordBox.text.toString()
                val cPass= CPasswordBox.text.toString()
                val modelUser =
                    ModelUser(firstName = fName, lastName = lName, phone = phone, password = pass, cPass = cPass)
                lifecycleScope.launch { registerUser(modelUser)}
            }
        }
    }

    /* private suspend fun saveUser(modelUser:ModelUser)
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


    }*/
    private suspend fun registerUser(user: ModelUser) {
        withContext(Dispatchers.IO) {
            try {
                val url = "https://cricdex.enfotrix.com/api/register"


                val jsonBody = JSONObject().apply {
                    put("first_name", user.firstName)
                    put("last_name", user.lastName)
                    put("phone_number", user.phone)
                    put("password", user.password)
                    put("c_password", user.cPass)
                }

                // Create a RequestQueue (this can be initialized once globally)
                val requestQueue = Volley.newRequestQueue(this@SignUpActivity)

                // Create the StringRequest
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url,jsonBody,com.android.volley.Response.Listener { _ ->

                        Toast.makeText(this@SignUpActivity,"Success",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
                    }, com.android.volley.Response.ErrorListener {

                       Toast.makeText(this@SignUpActivity,"error listner" ,Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        return headers
                    }
                }


                requestQueue.add(jsonObjectRequest)

            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@SignUpActivity,"Error occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}