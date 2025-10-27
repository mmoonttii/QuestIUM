package com.example.questium

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usernameEt = findViewById<EditText>(R.id.etUsername)
        val passwordEt = findViewById<EditText>(R.id.etPassword)

        val loginBtn = findViewById<Button>(R.id.btnLogin)
        loginBtn.setOnClickListener {
            if (GlobalData.user_list.any {
                    it.username == usernameEt.text.toString() && it.password == passwordEt.text.toString()
                })
                login()
            else
                Log.i("Monti", "User not found")
        }

        val registerTv = findViewById<TextView>(R.id.tvRegister)
        registerTv.setOnClickListener { register() }
    }

    fun login(){
        val intent = Intent(this, SecondActivity::class.java);
        startActivity(intent);
    }
    fun register(){
        val intent = Intent(this, RegisterActivity::class.java);
        startActivity(intent);
    }
}