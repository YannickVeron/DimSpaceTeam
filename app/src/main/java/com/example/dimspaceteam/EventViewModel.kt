package com.example.dimspaceteam

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dimspaceteam.model.Event
import com.example.dimspaceteam.model.EventType
import com.example.dimspaceteam.model.UIElement
//import com.example.dimspaceteam.network.EventHandler

class EventViewModel(val webSocketClient: WebSocketClient) : ViewModel() {
    var elements = MutableLiveData<List<UIElement>>()
    lateinit var gameStartedUI : List<UIElement>

    fun getCurrentEvent():LiveData<Event> = webSocketClient.event




    /*fun handle(event: Event){
        var context = EventHandler.view?.context
        Log.i("EventHandler",context.toString())
        event?.let { e->
            when(e.type){
                //IN
                EventType.WAITING_FOR_PLAYER->{/*TODO("Update waiting room player list")*/}
                EventType.GAME_STARTED->{
                    elements.value = (e as Event.GameStarted).uiElementList
                }
                EventType.NEXT_ACTION->{/*TODO("Update action")*/}
                EventType.GAME_OVER->{/*TODO("navigate to gameover")*/}
                EventType.NEXT_LEVEL->{/*TODO("Update elements")*/}
                //OUT
                EventType.READY->{/*TODO("Send event ready")*/}
                EventType.PLAYER_ACTION->{/*TODO("Send action")*/

                  //  webSocketClient.webSocket.send("")

                }//Should not exist
                //IN/OUT
                EventType.ERROR->{
                    Log.e("EventType","Something bad happened on the server")
                    //TODO("Return to the main menu")
                }
            }
        }
    }*/

}