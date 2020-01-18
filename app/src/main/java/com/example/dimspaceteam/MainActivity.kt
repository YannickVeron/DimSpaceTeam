package com.example.dimspaceteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Testatrix{

    @GET("/api/users?sort=top")
    fun getTopUsers(): Call<List<User>>

    @POST("/api/user/register")
    fun registerUser(@Body newUser: UserPost) : Call<User>

    @GET("/api/user/find/{name}")
    fun getUser(@Path("name") name: String): Call<User>
}


//Now Obsolete => Use "User" class
/*data class Avatar (
    val id: Int,
    val name: String,
    val avatar: String,
    var score: Int,
    var state: State = State.OVER
)*/
// class servant lors de la crétion d'un utilisateur
//data class AvatarPost(val name: String)


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btStart.setOnClickListener{displayJoinModal()}


        /*
        //recherche user
        getUser("jojo")
        // list user score top
        getTopUsers()
        // registre user
        var username= "test"
        registrerUser(username)*/
    }

    fun displayJoinModal(){
        val dialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        dialog.setTitle("Login")
        val dialogLayout = inflater.inflate(R.layout.dialog_join, null)
        val username  = dialogLayout.findViewById<EditText>(R.id.username)
        val room = dialogLayout.findViewById<EditText>(R.id.room)
        dialog.setView(dialogLayout)
        dialog.setPositiveButton("Connect") {
                dialogInterface, i -> Toast.makeText(applicationContext, "EditText is " + username.text.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

    // Sert pour les connection en GET ou en POST
    val apiUrl = "http://vps769278.ovh.net"

    val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(Testatrix::class.java)


    // obtenir liste de tous les users avec les meilleurs score
    fun getTopUsers(){
        val topUsersCall = service.getTopUsers()
        topUsersCall.enqueue(object: Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>) {
                val topUsers = response.body()
                topUsers?.let {
                    // On parcoure la liste
                    for(user in it) {
                        Log.d("Api","Top player : ${user.name}")
                    }
                }
            }
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                // SI echec Est ce qu'on l'afficher un textview ?
            }
        })
    }

    // Obtenir information d'un user avec son nom
    fun getUser(username: String){
        val getUserCall = service.getUser("${username}")
        getUserCall.enqueue(object: Callback<User> {

            override fun onResponse(call: Call<User>?, response: Response<User>) {
                val user = response.body()
                user?.let {
                    Log.d("Api","User : ${it.name}")
                }
            }
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                // SI l'utilisateur n'exsite pas je n'arrive pas a apeller la fonction RegistreUser()
            }
        })
    }


    // Ajouts d'un utilisateur passage du nom en variable
    fun registrerUser(username : String){
        val user = UserPost("${username}")
        val registerUserCall = service.registerUser(user)

        // obtenir info sur un joueur par son nom
        registerUserCall.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>?, response: Response<User>) {
                val newUser = response.body()
                newUser?.let {
                    Log.d("Api","Player Registered :  ${it.name}")
                }
            }
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e("Api", "Error : $t")
            }
        })
    }


}
