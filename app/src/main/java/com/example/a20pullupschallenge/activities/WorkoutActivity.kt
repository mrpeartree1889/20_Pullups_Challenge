package com.example.a20pullupschallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Scene
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.a20pullupschallenge.R
import kotlinx.android.synthetic.main.activity_workout.*
import org.jetbrains.anko.startActivity

class WorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val weekCurrent = intent.getIntExtra("week",0)
        val dayCurrent = intent.getIntExtra("day",0)


        // Load layouts as scenes
        val sceneRoot: ViewGroup = findViewById(R.id.fragLayout)
        val welcomeScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_welcome, this)
        val pullupScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_set, this)
        val restScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_rest, this)
        val completeScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_complete, this)

        TransitionManager.go(welcomeScene)
        // set var with name of active scene
        var activeScene = "welcome"


        // on click for the main button that changes the text displayed in it
        // and the action depending on the active state
        mainBtn.setOnClickListener {
            when (activeScene) {
                "welcome" -> {
                    TransitionManager.go(pullupScene)
                    mainBtn.text = getString(R.string.done)
                    activeScene = "pullup"
                }

                "pullup" -> {
                    TransitionManager.go(restScene)
                    mainBtn.text = getString(R.string.rest_complete)
                    activeScene = "rest"


                    /// Timer for resting
                    val progressBar: ProgressBar = findViewById(R.id.progressBar)
                    val timeText : TextView = findViewById(R.id.timeText)
                    val countDownTimer: CountDownTimer
                    var i = 0

                    progressBar.isIndeterminate = false
                    progressBar.progress = (120- i)
                    progressBar.max = 120

                    countDownTimer = object : CountDownTimer(121000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            timeText.text = (120 - i).toString()
                            progressBar.progress = (120 - i)
                            i++

                        }

                        override fun onFinish() {
                            TransitionManager.go(completeScene)
                            cancelBtn.visibility = View.GONE
                            mainBtn.text = getString(R.string.workout_complete_great)
                            activeScene = "complete"
                        }
                    }
                    countDownTimer.start()
                }

                "rest" -> {
                    TransitionManager.go(completeScene)
                    cancelBtn.visibility = View.GONE
                    mainBtn.text = getString(R.string.workout_complete_great)
                    activeScene = "complete"

                }

                "complete" -> startActivity<MainActivity>()
            }
        }
    }


}
