package com.example.dimspaceteam

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity

class GameActivity : FragmentActivity() {
    //Game activity will load waiting room fragment then question fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("frag","onCreateFrag")
        setContentView(R.layout.game)
    }
}