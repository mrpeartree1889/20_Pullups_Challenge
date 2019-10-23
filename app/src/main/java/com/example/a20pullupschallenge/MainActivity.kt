package com.example.a20pullupschallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

const val PREFS_FILENAME = "com.example.a20pullupschallenge.prefs"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Sets up initial screen slider
        val sp = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        if (!sp.getBoolean("first", false)) {
            val editor = sp.edit()
            editor.putBoolean("first", true)
            editor.apply()
            val intent = Intent(this, IntroActivity::class.java) // Call the AppIntro java class
            startActivity(intent)
        }

        // Checks if new workout is needed
        var initialPullups: Int = Constants.startPullups
        initialPullups = intent.getIntExtra("initialPullups", -1)

        if(initialPullups == -1) {
            createWorkoutLayout.visibility = View.VISIBLE
            workoutPlanLayout.visibility = View.GONE
        } else if (initialPullups >= 0) {
            createWorkoutLayout.visibility = View.GONE
            workoutPlanLayout.visibility = View.VISIBLE

            toast("number of initial pullups was $initialPullups")
        }

        btnMakePlan.setOnClickListener() {
            startActivity<CreatePlan>()
        }

    }
}
