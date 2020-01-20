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



        //Test Events
        /*var bt1 = UIElement.Button(1,"Some text")
        var sw1 = UIElement.Switch(2,"SomeOtherText")
        var sk1 = UIElement.Shake(3,"Shake Your Phone")
        var lui : List<UIElement> = listOf(bt1,sw1,sk1)
        var eventGS = Event.GameStarted(lui)
        var eventNL = Event.NextLevel(lui,2)
        var eventGO = Event.GameOver(250,true,3)

        var eh = EventHandler
        eh.fragment = this
        eh.view = view
        eh.Handle(eventGS)
        //WebSocketJoinRoom(roomName,userId)*/

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventViewModel(WebSocketClient) as T
            }
        }
        viewModel = ViewModelProviders.of(this,factory).get(EventViewModel::class.java)
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
                    view?.findNavController()?.navigate(R.id.questionFragment)
                }
                else->{}
            }
        })
        viewModel.action.observe(this, Observer<Event.NextAction>{

        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Sensey.getInstance().stop()
    }

    fun ready(view: View?,webSocketClient: WebSocketClient){
        webSocketClient.webSocket?.send("{\"type\":\"READY\", \"value\":true}")
        //view?.findNavController()?.navigate(R.id.questionFragment)
        //TODO("Send ready")
    }


    /*fun EventHandler(view: View,event: Event){
        event?.let { e->
            when(e.type){
                //IN
                EventType.WAITING_FOR_PLAYER->{/*TODO("Update waiting room player list")*/}
                EventType.GAME_STARTED->{context?.let { questionBuilder(it,view,(e as Event.GameStarted).uiElementList)}}
                EventType.NEXT_ACTION->{context?.let { displayAction(it,view,(e as Event.NextAction)) }}
                EventType.GAME_OVER->{
                    var eGO = (e as Event.GameOver)
                    if(eGO.win){
                        //TODO("Navigate to win Screen")
                    }else{
                        //TODO("Navigate to gameOver Screen")
                    }
                }
                EventType.NEXT_LEVEL->{ context?.let { questionBuilder(it,view,(e as Event.NextLevel).uiElementList) } }
                //OUT
                EventType.READY->{/*TODO("Send event ready")*/}
                EventType.PLAYER_ACTION->{/*TODO("Send action")*/}//Should not exist
                //IN/OUT
                EventType.ERROR->{
                    Log.e("EventType","Something bad happened on the server")
                    //TODO("Return to the main menu")
                }
            }
        }
    }*/
    fun displayAction(context: Context,view: View,nextAction: Event.NextAction){
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
    }
}

