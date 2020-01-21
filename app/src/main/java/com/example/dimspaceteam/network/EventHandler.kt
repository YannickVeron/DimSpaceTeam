package com.example.dimspaceteam.network
/*
import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.dimspaceteam.WaitingRoomFragment
import com.example.dimspaceteam.model.Event
import com.example.dimspaceteam.model.EventType

object EventHandler {
    var view :View?=null
    var fragment: Fragment?=null

    fun Handle(event: Event){
        var context = view?.context
        Log.i("EventHandler",context.toString())
        event?.let { e->
            when(e.type){
                //IN
                EventType.WAITING_FOR_PLAYER->{/*TODO("Update waiting room player list")*/}
                EventType.GAME_STARTED->{context?.let { (fragment as WaitingRoomFragment).questionBuilder(it,view!!,(e as Event.GameStarted).uiElementList)}}
                EventType.NEXT_ACTION->{context?.let { (fragment as WaitingRoomFragment).displayAction(it,view!!,(e as Event.NextAction)) }}
                EventType.GAME_OVER->{
                    var eGO = (e as Event.GameOver)
                    if(eGO.win){
                        //TODO("Navigate to win Screen")
                    }else{
                        //TODO("Navigate to gameOver Screen")
                    }
                }
                EventType.NEXT_LEVEL->{ context?.let { (fragment as WaitingRoomFragment).questionBuilder(it,view!!,(e as Event.NextLevel).uiElementList) } }
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
    }
}*/