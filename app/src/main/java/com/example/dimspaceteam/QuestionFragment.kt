package com.example.dimspaceteam

import android.content.Context
import android.graphics.Movie
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var timer: CountDownTimer

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
        var gameView = inflater.inflate(R.layout.question_fragment,container,false)
        viewModel = ViewModelProviders.of(activity!!,factory).get(EventViewModel::class.java)
        viewModel.getCurrentEvent().observe(this, Observer{e ->handleEvent(e)})
        questionBuilder(context,gameView,viewModel.gameStartedUI)

        return gameView
    }

    fun handleEvent(event: Event){
        when(event.type){
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
        var progressBar=view?.findViewById<ProgressBar>(R.id.progressBar)
        Log.i("progress",progressBar.toString())
        progressBar?.max=nextAction.action.time.toInt()
        progressBar?.progress=nextAction.action.time.toInt()
        if(::timer.isInitialized){ timer.cancel() }
        timer = object : CountDownTimer(nextAction.action.time, 20) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar?.progress=millisUntilFinished.toInt()
            }
            override fun onFinish() {}
        }
        timer.start()
    }

    fun questionBuilder(context: Context?, view: View?, list: List<UIElement>){
        var container = view?.findViewById<LinearLayout>(R.id.ActionContainer)
        container?.removeAllViews()

        for(el in list){
            when(el.type){
                UIType.BUTTON->{
                    var button:Button = with(Button(context)) {
                        id = el.id
                        text=el.content
                        setOnClickListener{
                            Log.i("UI","Click on ${text}")
                            var playerAction: String? = ObjetParser.toJson(Event.PlayerAction(el))
                            WebSocketClient.webSocket?.send(playerAction)
                        }
                        this
                    }
                    container?.addView(button)
                }
                UIType.SWITCH->{
                    var switch:Switch = with(Switch(context)){
                        id=el.id
                        text=el.content
                        setOnClickListener{
                            Log.i("UI","Click on ${text}")
                            var playerAction: String? = ObjetParser.toJson(Event.PlayerAction(el))
                            WebSocketClient.webSocket?.send(playerAction)
                        }
                        this
                    }
                    container?.addView(switch)

                }
                UIType.SHAKE->{
                    Sensey.getInstance().init(context)
                    val shakeListener: ShakeDetector.ShakeListener = object :
                        ShakeDetector.ShakeListener {
                        override fun onShakeDetected() { // Shake detected, do something
                            Log.i("UI","Shake on ${el.content}")
                            var playerAction: String? = ObjetParser.toJson(Event.PlayerAction(el))
                            WebSocketClient.webSocket?.send(playerAction)
                        }
                        override fun onShakeStopped(){}
                    }
                    Sensey.getInstance().startShakeDetection(shakeListener)
                }
            }
        }
    }
}