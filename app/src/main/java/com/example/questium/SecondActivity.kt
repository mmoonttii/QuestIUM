package com.example.questium

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val pointsTV = findViewById<TextView>(R.id.tvPoints)
        val plusBtn = findViewById<Button>(R.id.btnPlus)
        val minusBtn = findViewById<Button>(R.id.btnMinus)
        plusBtn.setOnClickListener { add(pointsTV) }
        minusBtn.setOnClickListener { minusOne(pointsTV) }

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
            showDeleteDialog(taskList, recyclerView)
        }

        val selectAllIb = findViewById<ImageButton>(R.id.ibSelectAll)
        selectAllIb.setOnClickListener {
            if (taskList.all { it.isCompleted }){
                taskList.forEach { it.isCompleted = false }
            } else {
                taskList.forEach { it.isCompleted = true }
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val logoutItem = menu?.findItem(R.id.itemLogout)
        val spannableString = SpannableString(logoutItem?.title)
        spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, spannableString.length, 0)
        logoutItem?.title = spannableString
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemValuta -> { showSeekbarDialog() }
            R.id.itemLogout -> { logout() }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    fun showSeekbarDialog() {
        val seekbarDialog = layoutInflater.inflate(R.layout.dialog_seekbar, null)

        val seekbar = seekbarDialog.findViewById<SeekBar>(R.id.seekbar)
        val voteTv = seekbarDialog.findViewById<TextView>(R.id.tvVote)

        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                voteTv.text = "${progress - 5}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        val dialog = AlertDialog.Builder(this)
            .setTitle("Valuta la tua giornata")
            .setPositiveButton("OK", null)
            .setNegativeButton("ANNULLA", null)
            .setView(seekbarDialog)
            .create()

        dialog.show()
    }

    fun showDeleteDialog(taskList: MutableList<Task>, recyclerView: RecyclerView) {
        AlertDialog.Builder(this)
            .setTitle("Sei sicuro di voler eliminare tutte le attività completate?")
            .setNegativeButton("ANNULLA", null)
            .setPositiveButton("OK", { _, _ ->
                taskList.removeAll { it.isCompleted }
                recyclerView.adapter?.notifyDataSetChanged()
            })
            .create()
            .show()
    }
}
