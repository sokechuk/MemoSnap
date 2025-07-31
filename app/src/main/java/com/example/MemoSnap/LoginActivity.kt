package com.example.MemoSnap

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.MemoSnap.data.MemoSnapDatabase
import com.example.MemoSnap.data.User
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var biometricBtn: Button
    private lateinit var textMessage: TextView

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.editTextEmail)
        passwordField = findViewById(R.id.editTextPassword)
        loginBtn = findViewById(R.id.buttonLogin)
        registerBtn = findViewById(R.id.buttonRegister)
        biometricBtn = findViewById(R.id.buttonBiometric)
        textMessage = findViewById(R.id.textMessage)

        val db = MemoSnapDatabase.getDatabase(applicationContext)
        val userDao = db.userDao()

        setupBiometricPrompt(userDao)

        biometricBtn.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        registerBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                val hashedPassword = HashUtil.sha256(password)
                lifecycleScope.launch {
                    userDao.insert(User(email = email, token = hashedPassword))
                    showMessage("Registered successfully.")
                }
            } else {
                showMessage("Please enter valid credentials.")
            }
        }

        loginBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val hashedPassword = HashUtil.sha256(password)

            lifecycleScope.launch {
                val user = userDao.getUserByEmail(email)
                if (user != null && user.token == hashedPassword) {
                    SecurePrefsHelper.getInstance(applicationContext).savePasswordHash(user.token)
                    goToMain(email)
                } else {
                    showMessage("Invalid login.")
                }
            }
        }
    }

    private fun setupBiometricPrompt(userDao: com.example.MemoSnap.data.UserDao) {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        lifecycleScope.launch {
                            val users = userDao.getAllUsers()
                            if (users.isNotEmpty()) {
                                val user = users.last()
                                SecurePrefsHelper.getInstance(applicationContext).savePasswordHash(user.token)
                                goToMain(user.email)
                            } else {
                                showMessage("No users available for biometric login.")
                            }
                        }
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        showMessage("Biometric error: $errString")
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        showMessage("Fingerprint not recognized.")
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use fingerprint to access MemoSnap")
                .setNegativeButtonText("Cancel")
                .build()

            lifecycleScope.launch {
                val users = userDao.getAllUsers()
                if (users.isNotEmpty()) {
                    biometricPrompt.authenticate(promptInfo)
                }
            }
        } else {
            biometricBtn.isEnabled = false
            biometricBtn.alpha = 0.5f
            showMessage("Biometric authentication not available.")
        }
    }

    private fun goToMain(email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_email", email)
        startActivity(intent)
        finish()
    }

    private fun showMessage(msg: String) {
        runOnUiThread {
            textMessage.text = msg
        }
    }
}
