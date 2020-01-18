package com.example.dimspaceteam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.waiting_room_fragment.*

class WaitingRoomFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.waiting_room_fragment,container,false);
        var startQuestionBtn = view.findViewById<Button>(R.id.btnStart)
        startQuestionBtn.setOnClickListener {Start(view)}
        var labelRoom = view.findViewById<TextView>(R.id.roomNameLb)
        labelRoom.setText((activity as GameActivity).roomName)
        return view
    }

    fun Start(view: View?){
        view?.findNavController()?.navigate(R.id.questionFragment)
    }
}

