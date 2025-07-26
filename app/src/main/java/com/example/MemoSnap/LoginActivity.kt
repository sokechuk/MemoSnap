package com.example.MemoSnap
import com.example.MemoSnap.R
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.MemoSnap.RegisterActivity
import com.example.MemoSnap.MainActivity
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvGoRegister = findViewById(R.id.tvGoRegister)

        btnLogin.setOnClickListener {
            loginUser()
        }

        tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        val inputEmail = etEmail.text.toString().trim()
        val inputPassword = etPassword.text.toString()

        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            showToast("Please enter both email and password")
            return
        }

        val sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        val savedEmail = sharedPref.getString("email", null)
        val savedPassword = sharedPref.getString("password", null)

        if (inputEmail == savedEmail && inputPassword == savedPassword) {
            showToast("Login successful")
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        } else {
            showToast("Invalid email or password")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}