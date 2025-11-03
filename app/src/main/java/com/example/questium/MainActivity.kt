package com.example.questium

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        val errorLoginTv = findViewById<TextView>(R.id.tvErrorLoginText)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val usernameTv = findViewById<TextView>(R.id.tvUsername)
        val passwordTv = findViewById<TextView>(R.id.tvPassword)
        val registerTv = findViewById<TextView>(R.id.tvRegister)

        loginBtn.isEnabled = false;
        loginBtn.alpha = 0.3f

        loginBtn.setOnClickListener {
            login(usernameEt, usernameTv, passwordEt, passwordTv, errorLoginTv)
        }

        registerTv.setOnClickListener { register() }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                watcher(loginBtn, usernameEt, passwordEt)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        usernameEt.addTextChangedListener(textWatcher)
        passwordEt.addTextChangedListener(textWatcher)

        intent.getBooleanExtra("fromRegisterActivity", false)
        Toast.makeText(this, "Registrazione completata con successo", Toast.LENGTH_SHORT).show()
    }

    fun login(usernameEt : EditText, usernameTv : TextView, passwordEt : EditText, passwordTv : TextView, errorLoginTv : TextView){
        val userPresent = GlobalData.user_list.any {
            it.username == usernameEt.text.toString() && it.password == passwordEt.text.toString()
        }

        if (!userPresent) {
            errorLoginTv.visibility = TextView.VISIBLE
            usernameEt.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
            usernameTv.setTextColor(ContextCompat.getColorStateList(this, R.color.red))
            passwordEt.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
            passwordTv.setTextColor(ContextCompat.getColorStateList(this, R.color.red))

            usernameEt.text.clear()
            passwordEt.text.clear()

            Log.i("Monti", "User not found")
        } else {
            val intent = Intent(this, SecondActivity::class.java);
            startActivity(intent);
        }
    }

    fun register(){
        val intent = Intent(this, RegisterActivity::class.java);
        startActivity(intent);
    }

    fun watcher(loginBtn : Button, usernameEt : EditText, passwordEt : EditText) {
        if (usernameEt.text.isNotBlank() && passwordEt.text.isNotBlank()) {
            loginBtn.isEnabled = true
            loginBtn.alpha = 1.0f
        } else {
            loginBtn.isEnabled = false
            loginBtn.alpha = 0.3f
        }
    }
}