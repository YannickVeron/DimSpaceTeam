package com.example.dimspaceteam.network

import android.util.Log
import okhttp3.*
import okio.ByteString

private val socketUrl = "ws://vps769278.ovh.net:8081/ws/join/"
private val TAG = "websocket"

fun WebSocketJoinRoom(roomName: String?, userId: Int?) {
    Log.d(TAG, "webSocket")
    val client = OkHttpClient()
    val requestJoinRoom = Request.Builder().url(socketUrl+"/"+roomName+"/"+userId).build()

    val webSocketListenerJoinRoom = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "onOpen")
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Log.d(TAG, "MESSAGE: " + text!!)
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
            Log.d(TAG, "MESSAGE: " + bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
            webSocket.close(1000, null)
            webSocket.cancel()
            Log.d(TAG, "CLOSE: $code $reason")
        }

        override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
            //TODO: stuff
        }

        override  fun onFailure(webSocket: WebSocket, t: Throwable, response: Response) {
            //TODO: stuff
        }
    }

    client.newWebSocket(requestJoinRoom, webSocketListenerJoinRoom)
    client.dispatcher().executorService().shutdown()
}