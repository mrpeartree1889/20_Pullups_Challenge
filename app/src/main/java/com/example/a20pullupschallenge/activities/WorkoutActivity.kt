package com.example.a20pullupschallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.fragments.WWelcomeFragment

class WorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()

        val welcomeFragment = WWelcomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.fragLayout, welcomeFragment)
            .replace(R.id.fragLayout, welcomeFragment)
            .addToBackStack(null)
            .commit()


    }

    fun testFun() {

    }


}
