package com.example.filmschecker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmschecker.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        with(binding){
            btnRegister.setOnClickListener {
                if(registerPassword.text.isNotBlank() && registerPassword.text.toString() == passwordConfirm.text.toString()){
                    toRegister()
                }else{
                    Log.d("FRA","ECHEC")
                }
            }
        }

    }

    private fun toRegister() {

        with(binding){
            if (registerEmail.text.isEmpty()) {
                registerEmail.error = "Please enter email id"
                registerEmail.requestFocus()
            } else if (passwordConfirm.text.isEmpty()) {
                passwordConfirm.setError("Please enter a password")
                passwordConfirm.requestFocus()
            } else if (passwordConfirm.text.length < 6) {
                passwordConfirm.setError("The password must be at least 6 characters long")
            }else{
                auth.createUserWithEmailAndPassword(binding.registerEmail.text.toString(), binding.registerPassword.text.toString())
                    .addOnCompleteListener(this@RegisterActivity) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Votre compte a bien été créé", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@RegisterActivity, MenuActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w("Register Activity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}