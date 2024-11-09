package com.enfotrix.practice_kt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.enfotrix.practice_kt.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject

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
                val enteredPhone = phoneET.text.toString()
                val enteredPass = PasswordET.text.toString()
                lifecycleScope.launch {
                    loginHandler(ModelUser(firstName = "", lastName = "", phone = enteredPhone, password = enteredPass, cPass = ""))
                }
            }
        }
    }
    private  suspend fun loginHandler(user: ModelUser) {
       withContext(Dispatchers.IO){
           try {
               val url = "https://cricdex.enfotrix.com/api/login"

               val jsonBody = JSONObject().apply {
                   put("phone_number", user.phone)
                   put("password", user.password)
               }

               val jsonObjectRequest =
                   object : JsonObjectRequest(Method.POST, url, jsonBody, Response.Listener {
                       Toast.makeText(this@LoginActivity, "Success", Toast.LENGTH_SHORT).show()
                       startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                   }, Response.ErrorListener {
                       Toast.makeText(this@LoginActivity, "error listener", Toast.LENGTH_SHORT).show()
                   }) {
                       override fun getHeaders(): Map<String, String> {
                           val headers = HashMap<String, String>()
                           headers["Content-Type"] = "application/json" // Set content type to JSON
                           return headers
                       }
                   }
               val requestQueue = Volley.newRequestQueue(this@LoginActivity)
               requestQueue.add(jsonObjectRequest)



           }catch (e:Exception){
               Toast.makeText(this@LoginActivity,"Error occurred",Toast.LENGTH_SHORT).show()
           }
           }
       }
}