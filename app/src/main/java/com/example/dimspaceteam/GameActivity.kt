package com.example.dimspaceteam

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class GameActivity : FragmentActivity() {
    //Game activity will load waiting room fragment then question fragment
    var roomName:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("frag","room name ${intent.getStringExtra("room_name")}")
        roomName=intent.getStringExtra("room_name")
        setContentView(R.layout.game)
    }
}