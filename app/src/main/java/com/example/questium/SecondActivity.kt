package com.example.questium

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

var score = 0;

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var logoutBtn = findViewById<Button>(R.id.btnLogout)
        logoutBtn.setOnClickListener {
            logout();
        }

        var pointsTV = findViewById<TextView>(R.id.tvPoints)
        var plusBtn = findViewById<Button>(R.id.btnPlus)
        var minusBtn = findViewById<Button>(R.id.btnMinus)

        plusBtn.setOnClickListener { add(pointsTV) }
        minusBtn.setOnClickListener { minusOne(pointsTV) }
    }

    fun logout() {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }

    fun add(pts: TextView) {
        score++
        pts.text = "Punti esperienza: $score"
    }
    fun minusOne(pts: TextView) {
        if (score > 0) score--
        pts.text = "Punti esperienza: $score"
    }
}