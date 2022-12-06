package com.e.notifapps

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.e.notifapps.databinding.ActivityMain2Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private var limit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Firebase.database("https://notif-apps-cd3c5-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("message")

        supportActionBar?.hide()

        takeData()

        binding = ActivityMain2Binding.inflate(layoutInflater)
        binding.button.setOnClickListener(){
            myRef.setValue(0)
        }
        setContentView(binding.root)

    }
    private fun takeData() {
        val database = Firebase.database("https://notif-apps-cd3c5-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("message")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<Int>()!!
                if (value==0 && !limit){
                    limit=true
                    val intent = Intent(applicationContext, MainActivity::class.java)
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
}