package com.example.questium

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

object GlobalData {
    var user_list = mutableListOf<User>()
}

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val usernameEt = findViewById<EditText>(R.id.etUsername)
        val emailEt = findViewById<EditText>(R.id.etEmail)
        val passwordEt = findViewById<EditText>(R.id.etPassword)
        val birthdateEt = findViewById<EditText>(R.id.etBirthdate)
        val tosCb = findViewById<CheckBox>(R.id.cbTos)

        birthdateEt.setOnClickListener { datePicker(birthdateEt) }

        val registerBtn = findViewById<Button>(R.id.btnRegister)

        registerBtn.setOnClickListener {
            if (tosCb.isChecked) {
                val username = usernameEt.text.toString()
                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()
                val dob = birthdateEt.text.toString()
                val newUser = User(username, email, password, dob)

                GlobalData.user_list.add(newUser)
                Log.i("Monti", GlobalData.user_list.toString())
                register()
            }
        }
    }

    fun datePicker(et : EditText) {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val dialog = DatePickerDialog(this,
            {_, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                et.setText(selectedDate)
            },
            year, month, day)
        dialog.show()
    }

    fun register() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
