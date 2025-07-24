package com.example.MemoSnap
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences("MemoSnapPrefs", MODE_PRIVATE)

        val emailField = findViewById<EditText>(R.id.editTextEmail)
        val passwordField = findViewById<EditText>(R.id.editTextPassword)
        val loginBtn = findViewById<Button>(R.id.buttonLogin)
        val registerBtn = findViewById<Button>(R.id.buttonRegister)

        registerBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            prefs.edit().putString("email", email)
                .putString("password", password)  // stored in plaintext (insecure)
                .apply()
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
        }

        loginBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val storedEmail = prefs.getString("email", "")
            val storedPassword = prefs.getString("password", "")

            if (email == storedEmail && password == storedPassword) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
