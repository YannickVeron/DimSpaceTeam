package com.example.dimspaceteam

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.dimspaceteam.model.Event
import com.example.dimspaceteam.model.EventType
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.WebSocket



object ObjetParser{

    var eventParser: JsonAdapter<Event>? = null

    private fun createInstance() {
        if (eventParser == null) {
            var moshi = Moshi.Builder()
                .add(
                    PolymorphicJsonAdapterFactory.of(Event::class.java, "type")
                        .withSubtype(Event.Ready::class.java, EventType.READY.name)
                        .withSubtype(Event.Error::class.java, EventType.ERROR.name)
                        .withSubtype(Event.GameStarted::class.java, EventType.GAME_STARTED.name)
                        .withSubtype(Event.NextLevel::class.java, EventType.NEXT_LEVEL.name)
                        .withSubtype(Event.PlayerAction::class.java, EventType.PLAYER_ACTION.name)
                        .withSubtype(Event.NextAction::class.java, EventType.NEXT_ACTION.name)
                        .withSubtype(Event.WaitingForPlayer::class.java, EventType.WAITING_FOR_PLAYER.name)
                        .withSubtype(Event.GameOver::class.java, EventType.GAME_OVER.name)
                )
                .add(KotlinJsonAdapterFactory())
                .build()
            eventParser = moshi.adapter(Event::class.java)
        }
    }

    fun request(text:String): Event? {
        if(eventParser==null)
        {
            this.createInstance()
        }
        return eventParser?.fromJson(text)
    }
}


object WebSocketClient: WebSocketListener() {


    var webSocket: WebSocket? = null
    var event = MutableLiveData <Event>()


    fun create( room: String , id : String) {

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://vps769278.ovh.net:8081/ws/join/${room}/${id}")
            .build()

        webSocket=OkHttpClient().newWebSocket(request, this)

    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("response","Votre socket es ouverte")
        this.webSocket = webSocket
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("response","fail because : " + t.message)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        webSocket.cancel()
        Log.d("response","socket close")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("test", text)

        var d=ObjetParser.request(text)

        event.value = d

        Log.d("toto","${d}")


    }


}