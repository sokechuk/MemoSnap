package com.example.MemoSnap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.MemoSnap.data.MemoSnapDatabase
import com.example.MemoSnap.sync.PhotoSyncWorker
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var logoutBtn: Button
    private lateinit var welcomeMsg: TextView
    private var isUnlocked = false
    private var currentUserEmail: String? = null
    private val idleTimeoutMillis: Long = 5 * 60 * 1000 // 5 minutes
    private val idleHandler = Handler(Looper.getMainLooper())
    private val idleRunnable = Runnable {
        isUnlocked = false
        authenticateUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutBtn = findViewById(R.id.buttonLogout)
        welcomeMsg = findViewById(R.id.textWelcome)

        // Retrieve the logged-in user's email from intent
        currentUserEmail = intent.getStringExtra("user_email")

        lifecycleScope.launch {
            val user = currentUserEmail?.let { email ->
                MemoSnapDatabase.getDatabase(applicationContext)
                    .userDao().getUserByEmail(email)
            }
            user?.let {
                welcomeMsg.text = "Welcome, ${it.email}"
            } ?: run {
                welcomeMsg.text = "Welcome"
            }
        }

        logoutBtn.setOnClickListener {
            confirmLogout()
        }

        schedulePhotoSync()
    }

    override fun onResume() {
        super.onResume()
        if (!isUnlocked) authenticateUser()
        resetIdleTimer()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetIdleTimer()
    }

    private fun resetIdleTimer() {
        idleHandler.removeCallbacks(idleRunnable)
        idleHandler.postDelayed(idleRunnable, idleTimeoutMillis)
    }

    private fun authenticateUser() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ) == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            showBiometricPrompt()
        } else {
            showPasswordDialog()
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock MemoSnap")
            .setSubtitle("Authenticate to continue")
            .setNegativeButtonText("Use password or PIN")
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    isUnlocked = true
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showPasswordDialog()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showPasswordDialog() {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        }

        AlertDialog.Builder(this)
            .setTitle("Enter PIN")
            .setView(input)
            .setPositiveButton("Unlock") { _, _ ->
                val entered = input.text.toString()
                if (verifyPassword(entered)) {
                    isUnlocked = true
                } else {
                    Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Cancel") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun verifyPassword(entered: String): Boolean {
        val storedHash = SecurePrefsHelper.getInstance(this).getPasswordHash()
        val enteredHash = sha256(entered)
        return enteredHash == storedHash
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }


    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                // Clear only stored password, not all users
                SecurePrefsHelper.getInstance(applicationContext).clearPasswordHash()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun schedulePhotoSync() {
        val syncRequest = PeriodicWorkRequestBuilder<PhotoSyncWorker>(
            60, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "photo_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
