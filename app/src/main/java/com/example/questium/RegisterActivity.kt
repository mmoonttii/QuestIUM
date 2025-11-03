package com.example.questium

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import kotlin.text.clear

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
        val passTooltipTv = findViewById<TextView>(R.id.tvPasswordTooltip)
        val confirmEt = findViewById<EditText>(R.id.etConfirmPassword)
        val birthdateEt = findViewById<EditText>(R.id.etBirthdate)
        val tosCb = findViewById<CheckBox>(R.id.cbTos)
        val registerBtn = findViewById<Button>(R.id.btnRegister)

        registerBtn.isEnabled = false
        registerBtn.alpha = 0.3f

        birthdateEt.setOnClickListener {
            datePicker(birthdateEt)
        }

        registerBtn.setOnClickListener {
            if (validatePassword(passwordEt, confirmEt)) {
                register(
                    usernameEt.text.toString(),
                    emailEt.text.toString(),
                    passwordEt.text.toString(),
                    birthdateEt.text.toString()
                )
            }
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                watcher(registerBtn, usernameEt, emailEt, passwordEt, confirmEt, birthdateEt, tosCb)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        usernameEt.addTextChangedListener(textWatcher)
        passwordEt.addTextChangedListener(textWatcher)
        emailEt.addTextChangedListener(textWatcher)
        birthdateEt.addTextChangedListener(textWatcher)
        tosCb.setOnCheckedChangeListener { _, isChecked ->
            watcher(registerBtn, usernameEt, emailEt, passwordEt, confirmEt, birthdateEt, tosCb)
        }

        TooltipCompat.setTooltipText(passTooltipTv, "La password deve contenere almeno 8 caratteri")
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
        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
    }

    fun register(username : String, email : String, password : String, birthdate : String) {
        val newUser = User(username, email, password, birthdate)

        GlobalData.user_list.add(newUser)
        Log.i("Monti", GlobalData.user_list.toString())

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fromRegisterActivity", true)
        startActivity(intent)
    }

    fun validatePassword(passwordEt : EditText, confirmEt : EditText) : Boolean{
        if (passwordEt.text.toString().length < 8) {
            passwordEt.text.clear()
            confirmEt.text.clear()
            passwordEt.error = "La password deve avere almeno 8 caratteri"
            return false
        }

        if (passwordEt.text.toString() != confirmEt.text.toString()) {
            passwordEt.text.clear()
            confirmEt.text.clear()
            confirmEt.error = "Le password non coincidono"
            return false
        }
        return true
    }

    fun watcher(registerBtn : Button, usernameEt : EditText, emailEt : EditText, passwordEt : EditText, confirmEt: EditText, birthdateEt : EditText, tosCb : CheckBox) {
        val userPresent = GlobalData.user_list.any {
            it.username == usernameEt.text.toString()
        }
        val emailValid = Patterns.EMAIL_ADDRESS.matcher(emailEt.text).matches()
        val notBlank =
            usernameEt.text.isNotBlank() && emailEt.text.isNotBlank() && passwordEt.text.isNotBlank() && birthdateEt.text.isNotBlank() && tosCb.isChecked;

        registerBtn.isEnabled = false
        registerBtn.alpha = 0.3f

        if (userPresent) {
            usernameEt.error = "Nome utente già esistente"
        }
        if (!emailValid) {
            emailEt.error = "E-mail non valida"
        }
        if (!userPresent && emailValid && notBlank) {
            registerBtn.isEnabled = true
            registerBtn.alpha = 1.0f
        }
    }
}
