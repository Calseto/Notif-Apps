package com.e.notifapps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.e.notifapps.databinding.ActivityMainBinding
import com.e.notifapps.viewmodels.ViewModel1
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.nio.file.attribute.AclEntry

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID = "channel_id_01"
    private val notificationId = 101
    private var limit=false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Firebase.database("https://notif-apps-cd3c5-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("message")

        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeNotificationChannel()
        takeData()

        binding.button.setOnClickListener{
            myRef.setValue("1")
        }
    }

    private fun takeData() {
        val database = Firebase.database("https://notif-apps-cd3c5-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("message")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()!!
                if (value=="1" && !limit){
                    sendNotif()
                    limit=true
                    val intent = Intent(applicationContext, MainActivity2::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun makeNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val name = "notification title"
            val descriptionText = "description text"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
    private fun sendNotif(){
        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Warning!!")
            .setContentText("ada bahaya disekitar anda")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }

}