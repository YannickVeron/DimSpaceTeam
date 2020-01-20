package com.example.dimspaceteam

import android.content.Context
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
import kotlinx.android.synthetic.main.question_fragment.*
import kotlinx.android.synthetic.main.waiting_room_fragment.*
import okhttp3.WebSocket

class QuestionFragment : Fragment(){

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
        viewModel = ViewModelProviders.of(this,factory).get(EventViewModel::class.java)
        viewModel.getCurrentEvent().observe(this, Observer{e ->handle(e)})
        //handle(viewModel.getCurrentEvent().value!!)//initial handle

        return inflater.inflate(R.layout.question_fragment,container,false)
    }

    fun handle(event: Event){
        when(event.type){
            EventType.GAME_STARTED->{
                Log.i("question","game started")
                var uiElements = (event as Event.GameStarted).uiElementList
                questionBuilder(context,view,uiElements)
            }
            EventType.NEXT_ACTION->{displayAction(context,view,(event as Event.NextAction))}
            EventType.GAME_OVER->{
                if((event as Event.GameOver).win) {
                    view?.findNavController()?.navigate(R.id.gameWin)
                }else{
                    view?.findNavController()?.navigate(R.id.gameOver)
                }
            }
            EventType.NEXT_LEVEL->{questionBuilder(context,view,(event as Event.NextLevel).uiElementList)}
            else->{}
        }
    }

    fun displayAction(context: Context?,view: View?,nextAction: Event.NextAction){
        actionLb.text=nextAction.action.sentence
    }

    fun questionBuilder(context: Context?, view: View?, list: List<UIElement>){
        var container = view?.findViewById<LinearLayout>(R.id.ActionContainer)
        container?.removeAllViews()

        for(el in list){
            when(el.type){
                UIType.BUTTON->{
                    var bt = Button(context)
                    bt.id=el.id
                    bt.text=el.content
                    bt.setOnClickListener{
                        Log.i("UI","Click on ${bt.text}")
                        var playerAction: String? = ObjetParser.toJson(Event.PlayerAction(el))
                        WebSocketClient.webSocket?.send(playerAction)
                    }
                    container?.addView(bt)
                }
                UIType.SWITCH->{
                    var sw = Switch(context)
                    sw.id=el.id
                    sw.text=el.content
                    sw.setOnClickListener{
                        Log.i("UI","Click on ${sw.text}")
                        var playerAction: String? = ObjetParser.toJson(Event.PlayerAction(el))
                        WebSocketClient.webSocket?.send(playerAction)
                    }
                    container?.addView(sw)
                }
                UIType.SHAKE->{
                    var shkLb = TextView(context)
                    shkLb.text=el.content
                    Sensey.getInstance().init(context)
                    val shakeListener: ShakeDetector.ShakeListener = object :
                        ShakeDetector.ShakeListener {
                        override fun onShakeDetected() { // Shake detected, do something
                            Log.i("UI","Shake on ${el.content}")
                            var playerAction: String? = ObjetParser.toJson(Event.PlayerAction(el))
                            WebSocketClient.webSocket?.send(playerAction)
                        }
                        override fun onShakeStopped() {

                        }
                    }
                    container?.addView(shkLb)
                    Sensey.getInstance().startShakeDetection(shakeListener)
                }
            }
        }
    }
}