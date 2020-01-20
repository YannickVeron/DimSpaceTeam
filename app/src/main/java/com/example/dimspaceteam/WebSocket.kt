package com.example.dimspaceteam

import android.util.Log
import okhttp3.*
import okhttp3.WebSocket


class WebSocket{


    fun Create( room: String , id : String) {

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://vps769278.ovh.net:8081/ws/join/${room}/${id}")
            .build()

        val wsListener = SocketListener()

        client.newWebSocket(request, wsListener) // ?
    }


    private class SocketListener : WebSocketListener() {

        private val NORMAL_CLOSURE_STATUS = 1000

        override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
            output("Socket Open")

        }

        override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
            output("Receiving : " + text!!)
        }

        override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            webSocket!!.close(NORMAL_CLOSURE_STATUS, null) // closed socket
            output("Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            output("Error : " + t.message)
        }


        private fun output(txt: String) {
            Log.v("WSS", txt)
        }


        // reception des EVENT Comment on les traites ?

    }



}