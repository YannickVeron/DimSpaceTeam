package com.example.dimspaceteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btStart.setOnClickListener{displayJoinModal()}
    }
    fun displayJoinModal(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Login")
        val dialogLayout = inflater.inflate(R.layout.dialog_join, null)
        val username  = dialogLayout.findViewById<EditText>(R.id.username)
        val room = dialogLayout.findViewById<EditText>(R.id.room)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Connect") {
                dialogInterface, i -> Toast.makeText(applicationContext, "EditText is " + username.text.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        }
        builder.show()
    }
}
