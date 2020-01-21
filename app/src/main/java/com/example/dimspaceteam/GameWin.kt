package com.example.dimspaceteam

import android.content.Context
import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.dimspaceteam.model.Event
import com.example.dimspaceteam.model.EventType
import com.example.dimspaceteam.model.UIElement
import com.example.dimspaceteam.model.UIType
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.ShakeDetector
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.fragment_game_over.*
import kotlinx.android.synthetic.main.question_fragment.*
import kotlinx.android.synthetic.main.waiting_room_fragment.*
import okhttp3.WebSocket

class GameWin : Fragment() {
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventViewModel(WebSocketClient) as T
            }
        }
        var view = inflater.inflate(R.layout.fragment_game_win,container,false)

        viewModel = ViewModelProviders.of(activity!!,factory).get(EventViewModel::class.java)
        viewModel.getCurrentEvent().observe(this, Observer{})

        view.findViewById<Button>(R.id.btnRestart).setOnClickListener{
            view?.findNavController()?.navigate(R.id.waitingRoomFragment)
        }
        view.findViewById<Button>(R.id.btnLeaveGame).setOnClickListener {
            viewModel.webSocketClient.webSocket?.close(1000,"Game Complete")
            val intent = Intent(context,MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
