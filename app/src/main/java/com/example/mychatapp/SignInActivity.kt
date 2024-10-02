package com.example.mychatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import com.example.mychatapp.R

class SignInActivity : AppCompatActivity() {
    // Create the Variables for the inputs.
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var signUpButton: Button
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_sign_up)

        // Assign the inputs to the variables.
        emailInput = findViewById(R.id.et_email)
        passwordInput = findViewById(R.id.et_password)
        signUpButton = findViewById(R.id.btn_sign_up)

        signUpButton.setOnClickListener {
            // Get the email and password from the inputs.
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showError("Email and Password cannot be empty")
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                showError("Invalid Email Format")
                return@setOnClickListener
            }

            Log.i("Test Credentials", "Email: $email and Password: $password")
            signUp(email, password)
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Function to show error message in a notification banner
    private fun showError(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }


    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success
                    showError("Authentication Successful.")
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    // If sign-in fails, display a message to the user.
                    showError("Authentication failed.")
                }
            }
    }
}