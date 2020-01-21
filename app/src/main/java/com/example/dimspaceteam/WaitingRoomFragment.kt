package com.example.dimspaceteam

import android.content.Context
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


class WaitingRoomFragment : Fragment(){

    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.waiting_room_fragment,container,false);
        var readyBtn = view.findViewById<Button>(R.id.btnReady)
        var labelRoom = view.findViewById<TextView>(R.id.roomNameLb)
        var roomName=(activity as GameActivity).roomName
        var userId =(activity as GameActivity).userId
        labelRoom.setText(roomName)
        WebSocketClient.create("${roomName}", "${userId}")
        readyBtn.setOnClickListener {ready(view,WebSocketClient)}

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventViewModel(WebSocketClient) as T
            }
        }
        viewModel = ViewModelProviders.of(activity!!,factory).get(EventViewModel::class.java)
        viewModel.getCurrentEvent().observe(this, Observer{e ->
            when(e.type){
                EventType.WAITING_FOR_PLAYER->{
                    var users = (e as Event.WaitingForPlayer).userList
                    var list=""
                    for(user in users){
                        list="${list} ${user.name} : ${user.state}\n"
                    }
                    view.findViewById<TextView>(R.id.playersList).text=list
                    Log.i("Waiting",list)
                }
                EventType.GAME_STARTED->{
                    viewModel?.gameStartedUI=(e as Event.GameStarted).uiElementList
                    view?.findNavController()?.navigate(R.id.questionFragment)
                }
                else->{}
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Sensey.getInstance().stop()
    }

    fun ready(view: View?,webSocketClient: WebSocketClient){
        webSocketClient.webSocket?.send("{\"type\":\"READY\", \"value\":true}")
    }

    /*fun displayAction(context: Context,view: View,nextAction: Event.NextAction){
        //TODO("Display Question")

    }
    fun questionBuilder(context: Context,view: View,list: List<UIElement>){
        var container = view?.findViewById<LinearLayout>(R.id.uiContainer)
        for(el in list){
            when(el.type){
                UIType.BUTTON->{
                    var bt = Button(context)
                    bt.id=el.id
                    bt.text=el.content
                    bt.setOnClickListener{
                        Log.i("UI","Click on ${bt.text}")
                    }
                    container.addView(bt)
                }
                UIType.SWITCH->{
                    var sw = Switch(context)
                    sw.id=el.id
                    sw.text=el.content
                    sw.setOnClickListener{
                        Log.i("UI","Click on ${sw.text}")
                    }
                    container.addView(sw)
                }
                UIType.SHAKE->{
                    var shkLb = TextView(context)
                    shkLb.text=el.content
                    Sensey.getInstance().init(context)
                    val shakeListener: ShakeDetector.ShakeListener = object :
                        ShakeDetector.ShakeListener {
                        override fun onShakeDetected() { // Shake detected, do something
                            Log.i("UI","Shake on ${el.content}")
                        }
                        override fun onShakeStopped() {

                        }
                    }
                    container.addView(shkLb)
                    Sensey.getInstance().startShakeDetection(shakeListener)
                }
            }
        }
    }*/
}

