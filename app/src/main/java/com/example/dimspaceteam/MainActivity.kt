package com.example.dimspaceteam

import android.content.Context
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
                highScores.text = "Failed to retrieved highscores"
            }
        })
    }

    fun displayJoinModal(api: Api){
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_join, null)
        val username  = dialogLayout.findViewById<EditText>(R.id.username)
        val room = dialogLayout.findViewById<EditText>(R.id.room)
        AlertDialog.Builder(this).setTitle("Login")
            .setView(dialogLayout)
            .setPositiveButton("Connect") { _,_ ->
                var context = this
                api.getUser(username.text.toString(),object: Callback<User> {//check if user exist
                    override fun onResponse(call: Call<User>?, response: Response<User>) {
                        response.body()?.also{user->
                            Log.i("Login","${user.name} : ${user.id}")
                            logUser(api,user,context,room.text.toString())
                        }?:let{
                            registerUser(api,username.text.toString(),context,room.text.toString())
                        }
                    }
                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        Log.i("Login","Failed to find if user exist")
                    }
                })
            }.show()
    }

    fun logUser(api: Api, user: User, context: Context, roomName: String){
        api.logUser(user.id,object: Callback<User>{//log in user
            override fun onResponse(call: Call<User>?, response: Response<User>?){
                Log.i("Login","user successfully logged in")
                val intent = Intent(context,GameActivity::class.java).apply {
                    putExtra("room_name",roomName)
                    putExtra("user_id",user.id)
                }
                startActivity(intent)
            }
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e("Login","Failed to log user")
            }
        })
    }

    fun registerUser(api: Api, username:String, context: Context, roomName: String){
        api.registerUser(UserPost(username),object: Callback<User>{//register user
            override fun onResponse(call: Call<User>?, response: Response<User>) {
                Log.i("Login","user successfully registered")
                response.body()?.also{user->
                    Log.i("Login","${user.name} : ${user.id}")
                    logUser(api,user,context,roomName)
                }?:let{
                    Log.e("Login","Failed to register user : emptyResponse")
                }
            }
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e("Login","Failed to register user : fail request")
            }
        })
    }
}
