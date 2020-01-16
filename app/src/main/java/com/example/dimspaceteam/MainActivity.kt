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
    fun getTanStops(): Call<List<Avatar>>

    @POST("/api/user/register")
    fun registerUser(@Body newUser: AvatarPost) : Call<Avatar>

    @GET("/api/user/find/{name}")
    fun Avatarname(@Path("name") name: String): Call<Avatar>
}


data class Avatar (
    val id: Int,
    val name: String,
    val avatar: String,
    var score: Int,
    var state: State = State.OVER
)
// class servant lors de la crétion d'un utilisateur
data class AvatarPost(val name: String)


class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btStart.setOnClickListener{displayJoinModal()}

        /*
        //recherche user
        TestPerso()
        // list user score top
        ListTopUser()
        // registre user
        var testname= "test"
        RegistrerUser(testname)
        */
         

    }
    fun displayJoinModal(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Login")
        val dialogLayout = inflater.inflate(R.layout.dialog_join, null)
        val username  = dialogLayout.findViewById<EditText>(R.id.username)
        val room = dialogLayout.findViewById<EditText>(R.id.room)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Connect") {
                dialogInterface, i -> Toast.makeText(applicationContext, "EditText is " + username.text.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        }
        builder.show()
    }

        // Sert pour les connection en GET ou en POST
        val tanUrl = "http://vps769278.ovh.net"

        val retrofit = Retrofit.Builder()
            .baseUrl(tanUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(Testatrix::class.java)


        // obtenir liste de tous les users avec les meilleurs score
        fun ListTopUser(){
            val tanStops = service.getTanStops()
            tanStops.enqueue(object: Callback<List<Avatar>> {
                override fun onResponse(call: Call<List<Avatar>>?, response: Response<List<Avatar>>) {
                    val allTanStop = response.body()
                    allTanStop?.let {
                        // On parcoure la liste
                        for( tanStop in it) {
                            ///////////////////////////
                            // traitement des donnees
                            ///////////////////////////
                            Log.d("TAN","Arret Tan ${tanStop.name}")
                        }
                    }
                }
                override fun onFailure(call: Call<List<Avatar>>?, t: Throwable?) {
                    // SI echec Est ce qu'on l'afficher un textview ?
                }
            })
        }


        // Obtenir information d'un user avec son nom
        fun TestPerso(){
            // variable de test
            var testname= "jojo"


            val tanStops = service.Avatarname("${testname}")
            tanStops.enqueue(object: Callback<Avatar> {

                override fun onResponse(call: Call<Avatar>?, response: Response<Avatar>) {
                    val allTanStop = response.body()
                    allTanStop?.let {
                        ///////////////////////////
                        // traitement des donnees
                        ///////////////////////////
                        Log.d("TAN","Arret Tan ${it.name}")
                    }
                }

                override fun onFailure(call: Call<Avatar>?, t: Throwable?) {
                    // SI l'utilisateur n'exsite pas je n'arrive pas a apeller la fonction RegistreUser()
                }
            })
        }



        // Ajouts d'un utilisateur passage du nom en variable
        fun RegistrerUser( nameuser : String){
            val user = AvatarPost("${nameuser}")
            val tanStops = service.registerUser(user)

            // obtenir info sur un joueur par son nom
            tanStops.enqueue(object: Callback<Avatar> {

                override fun onResponse(call: Call<Avatar>?, response: Response<Avatar>) {
                    val allTanStop = response.body()
                    allTanStop?.let {
                        Log.d("TAN","Arret Tan ${it.name}")
                    }
                }
                override fun onFailure(call: Call<Avatar>?, t: Throwable?) {
                    Log.e("TAN", "Error : $t")
                }
            })
        }



}
