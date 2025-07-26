package com.example.MemoSnap

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.MemoSnap.LoginActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finish() // Optional: if you don’t want to keep MainActivity in back stack
    }
}