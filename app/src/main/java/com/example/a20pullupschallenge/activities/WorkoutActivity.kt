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
import androidx.core.view.updateLayoutParams
import com.example.a20pullupschallenge.R
import kotlinx.android.synthetic.main.activity_workout.*
import org.jetbrains.anko.startActivity

class WorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val sceneRoot: ViewGroup = findViewById(R.id.fragLayout)
        val welcomeScene: Scene =
            Scene.getSceneForLayout(sceneRoot, R.layout.fragment_workout_welcome, this)
        val pullupScene: Scene =
            Scene.getSceneForLayout(sceneRoot, R.layout.fragment_workout_set, this)
        val restScene: Scene =
            Scene.getSceneForLayout(sceneRoot, R.layout.fragment_workout_rest, this)
        val completeScene: Scene =
            Scene.getSceneForLayout(sceneRoot, R.layout.fragment_workout_complete, this)

        TransitionManager.go(welcomeScene)
        var activeScene : String = "welcome"

        mainBtn.setOnClickListener() {
            if (activeScene == "welcome") {
                TransitionManager.go(pullupScene)
                mainBtn.text = "Done"
                activeScene = "pullup"
            } else if (activeScene == "pullup") {
                TransitionManager.go(restScene)
                mainBtn.text = "Complete"
                activeScene = "rest"


                /// TIMER
                val progressBar: ProgressBar = findViewById(R.id.progressBar)
                val timeText : TextView = findViewById(R.id.timeText)
                val countDownTimer: CountDownTimer
                var i = 0

                progressBar.isIndeterminate = false
                progressBar.progress = (120- i)
                progressBar.max = 120
//                progressBar.min = 0

                countDownTimer = object : CountDownTimer(121000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeText.text = (120 - i).toString()
                        progressBar.progress = (120 - i)
                        i++

                    }

                    override fun onFinish() {
                        TransitionManager.go(completeScene)
                        cancelBtn.visibility = View.GONE
                        mainBtn.text = "Great!"
                        activeScene = "complete"
                    }
                }
                countDownTimer.start()
            } else if (activeScene == "rest") {
                TransitionManager.go(completeScene)
                cancelBtn.visibility = View.GONE
                mainBtn.text = "Great!"
                activeScene = "complete"
            } else if (activeScene == "complete") {
                startActivity<MainActivity>()
            }
        }



    }


}
