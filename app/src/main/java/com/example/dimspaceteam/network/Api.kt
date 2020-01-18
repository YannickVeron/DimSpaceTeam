package com.example.dimspaceteam.network

import com.example.dimspaceteam.model.User
import com.example.dimspaceteam.model.UserPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService2{

    @POST("/api/user/register")
    fun registerUser(@Body newUser: UserPost) : Call<User>

    @GET("/api/user/find/{name}")
    fun getUser(@Path("name") name: String): Call<User>

    @GET("/api/user/{id}")
    fun logUser(@Path("id") id: Int): Call<User>

    @GET("/api/users?sort=top")
    fun getTopUsers(): Call<List<User>>


}

class Api(url: String) {
    init {
        setInstance(url)
    }

    fun getUser(name: String,callback: Callback<User>) {
        SERVICE?.getUser(name)?.enqueue(callback)
    }

    fun logUser(id: Int,callback: Callback<User>) {
        SERVICE?.logUser(id)?.enqueue(callback)
    }

    fun registerUser(newUser: UserPost, callback: Callback<User>) {
        SERVICE?.registerUser(newUser)?.enqueue(callback)
    }

    fun getTopUsers(callback: Callback<List<User>>) {
        SERVICE?.getTopUsers()?.enqueue(callback)
    }

    companion object{
        @Volatile private var SERVICE : ApiService2? = null
        fun setInstance(url: String): ApiService2 =
            SERVICE ?: synchronized(this){
                SERVICE?: buildApi(url).also{ SERVICE =it}
            }
        private fun buildApi(url: String): ApiService2 {
            return Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService2::class.java)
        }
    }
}