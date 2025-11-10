package com.example.questium

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

var score = 0

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

        val logoutBtn = findViewById<Button>(R.id.btnLogout)
        logoutBtn.setOnClickListener {
            logout()
        }

        val pointsTV = findViewById<TextView>(R.id.tvPoints)
        val plusBtn = findViewById<Button>(R.id.btnPlus)
        val minusBtn = findViewById<Button>(R.id.btnMinus)

        plusBtn.setOnClickListener { add(pointsTV) }
        minusBtn.setOnClickListener { minusOne(pointsTV) }

        val seekbar = findViewById<SeekBar>(R.id.seekbar)
        val voteTV = findViewById<TextView>(R.id.tvVote)

        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                voteTV.text = "${progress - 5}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        val taskList = mutableListOf<Task>()
        val newTaskBtn = findViewById<Button>(R.id.btnAddTask)
        val newTaskEt = findViewById<EditText>(R.id.etNewTask)
        val recyclerView = findViewById<RecyclerView>(R.id.rvTask)
        val taskCounterTv = findViewById<TextView>(R.id.tvTaskCounter)

        recyclerView.adapter = TaskAdapter(taskList, taskCounterTv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        newTaskBtn.setOnClickListener {
            val taskDescription = newTaskEt.text.toString()
            taskList.add(Task(taskDescription, false))
            recyclerView.adapter?.notifyItemInserted(taskList.size - 1)
        }

        val clearTasksBtn = findViewById<Button>(R.id.btnClearTasks)
        clearTasksBtn.setOnClickListener {
            taskList.removeAll { it.isCompleted }
            recyclerView.adapter?.notifyDataSetChanged()
        }


    }

    fun logout() {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
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