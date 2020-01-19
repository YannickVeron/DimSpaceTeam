package com.example.dimspaceteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dimspaceteam.model.User
import com.example.dimspaceteam.model.UserPost
import com.example.dimspaceteam.network.Api
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*interface ApiService{

    @GET("/api/users?sort=top")
    fun getTopUsers(): Call<List<User>>

    @POST("/api/user/register")
    fun registerUser(@Body newUser: UserPost) : Call<User>

    @GET("/api/user/find/{name}")
    fun getUser(@Path("name") name: String): Call<User>
}*/


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
        var api = Api("http://vps769278.ovh.net")//put this in .env equivalent (global)
        btStart.setOnClickListener{displayJoinModal(api)}
        api.getTopUsers(object: Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>) {
                response.body()?.let {users->
                    var str =""
                    for(user in users) {
                        str="${str} ${user.score} : ${user.name}\n"
                    }
                    highScores.setText(str)
                }
            }
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.e("Scores",t.toString())
                highScores.setText("Failed to retrieved highscores")
            }
        })

        /*
        //recherche user
        getUser("jojo")
        // list user score top
        getTopUsers()
        // registre user
        var username= "test"
        registrerUser(username)*/
    }

    fun displayJoinModal(api: Api){
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_join, null)
        val username  = dialogLayout.findViewById<EditText>(R.id.username)
        val room = dialogLayout.findViewById<EditText>(R.id.room)
        AlertDialog.Builder(this).setTitle("Login")
            .setView(dialogLayout)
            .setPositiveButton("Connect") {
                _,_ ->
                var context = this
                api.getUser(username.text.toString(),object: Callback<User> {//check if user exist
                    override fun onResponse(call: Call<User>?, response: Response<User>) {
                        response.body()?.also{user->
                            Log.i("Login","${user.name} : ${user.id}")
                            api.logUser(user.id,object: Callback<User>{//log in user
                                override fun onResponse(call: Call<User>?, response: Response<User>?){
                                    Log.i("Login","user successfully logged in")
                                    val intent = Intent(context,GameActivity::class.java).apply {
                                        putExtra("room_name",room.text.toString())
                                        putExtra("user_id",user.id)
                                    }
                                    startActivity(intent)
                                }
                                override fun onFailure(call: Call<User>?, t: Throwable?) {
                                    Log.i("Login","Failed to log user")
                                }
                            })
                        }?:let{
                            api.registerUser(UserPost(username.text.toString()),object: Callback<User>{//register user
                                override fun onResponse(call: Call<User>?, response: Response<User>?) {
                                    Log.i("Login","user successfully registered")
                                }
                                override fun onFailure(call: Call<User>?, t: Throwable?) {
                                    Log.i("Login","Failed to register user")
                                    TODO("Log user in ")
                                }
                            })
                        }
                    }
                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        Log.i("Login","Failed to find if user exist")
                    }
                })
        }.show()
    }

    // Sert pour les connection en GET ou en POST
    /*val apiUrl = "http://vps769278.ovh.net"

    val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ApiService::class.java)

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
    }*/


}
