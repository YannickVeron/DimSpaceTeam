package com.example.dimspaceteam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.waiting_room_fragment.*

class WaitingRoomFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        btnStart.setOnClickListener{Start()}
        return inflater.inflate(R.layout.waiting_room_fragment,container,false)
    }

    fun Start(){

    }
}

