package com.example.a20pullupschallenge.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Scene
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.a20pullupschallenge.DayWorkout
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.Sets
import com.example.a20pullupschallenge.databases.MyDatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_workout.*
import org.jetbrains.anko.startActivity

class WorkoutActivity : AppCompatActivity() {

    val Context.planDatabase: MyDatabaseOpenHelper
        get() = MyDatabaseOpenHelper.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val weekCurrent = intent.getIntExtra("week",0)
        val dayCurrent = intent.getIntExtra("day",0)
        val dayWorkoutPlan = planDatabase.getNextWorkout(weekCurrent,dayCurrent)
        var dayWorkoutAchieved = populateDayWorkoutAchieved(dayWorkoutPlan)
        val setsPlanned = dayWorkoutPlan.workoutSets
        val setsAchieved = dayWorkoutAchieved.workoutSets

        // Load layouts as scenes
        val sceneRoot: ViewGroup = findViewById(R.id.fragLayout)
        val welcomeScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_welcome, this)
        val pullupScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_set, this)
        val restScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_rest, this)
        val completeScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_complete, this)

        // Update values of main layout
        weekText.text = getString(R.string.week_x_of_y, weekCurrent.toString(), "8")
        dayText.text = getString(R.string.day_x_of_y, dayCurrent.toString(), "3")


        // Layout manager
        TransitionManager.go(welcomeScene)
        val setOnePullups : TextView = findViewById(R.id.setOnePlan)
        val setTwoPullups : TextView = findViewById(R.id.setTwoPullups)
        val setThreePullups : TextView = findViewById(R.id.setThreePullups)
        val setFourPullups : TextView = findViewById(R.id.setFourPullups)
        val setFivePullups : TextView = findViewById(R.id.setFivePullups)
        val dayWorkoutSets = dayWorkoutPlan.workoutSets
        setOnePullups.text = dayWorkoutSets.setOne.toString()
        setTwoPullups.text = dayWorkoutSets.setTwo.toString()
        setThreePullups.text = dayWorkoutSets.setThree.toString()
        setFourPullups.text = dayWorkoutSets.setFour.toString()
        setFivePullups.text = dayWorkoutSets.setFive.toString()

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

                    val setNumber : TextView = findViewById(R.id.setNumber)
                    val pullupNumber : TextView = findViewById(R.id.pullupNumber)
                    val achievedPullupsNumber : TextView = findViewById(R.id.achievedPullupsNumber)

                    val currentSet = populateTable(setsPlanned, setsAchieved)

                    setNumber.text = currentSet[0].toString()
                    pullupNumber.text = currentSet[1].toString()
                    achievedPullupsNumber.text = currentSet[1].toString()

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

    fun populateDayWorkoutAchieved(dayWorkout:DayWorkout) : DayWorkout {
        dayWorkout.status = "accomp"
        dayWorkout.workoutSets.setOne = 0
        dayWorkout.workoutSets.setTwo = 0
        dayWorkout.workoutSets.setThree = 0
        dayWorkout.workoutSets.setFour = 0
        dayWorkout.workoutSets.setFive = 0

        return dayWorkout
    }

    // populates table of planned and achieved pullups plus returns an Int that says which set it is
    fun populateTable(setsPlanned : Sets, setsAchieved : Sets) : ArrayList<Int> {
        val setOnePlanned : TextView = findViewById(R.id.setOnePlan)
        val setTwoPlanned : TextView = findViewById(R.id.setTwoPlan)
        val setThreePlanned : TextView = findViewById(R.id.setThreePlan)
        val setFourPlanned : TextView = findViewById(R.id.setFourPlan)
        val setFivePlanned : TextView = findViewById(R.id.setFivePlan)

        val setOneAchieved : TextView = findViewById(R.id.setOneAchieved)
        val setTwoAchieved : TextView = findViewById(R.id.setTwoAchieved)
        val setThreeAchieved : TextView = findViewById(R.id.setThreeAchieved)
        val setFourAchieved : TextView = findViewById(R.id.setFourAchieved)
        val setFiveAchieved : TextView = findViewById(R.id.setFiveAchieved)

        setOnePlanned.text = setsPlanned.setOne.toString()
        setOneAchieved.text = setsAchieved.setOne.toString()

        setTwoPlanned.text = setsPlanned.setTwo.toString()
        setTwoAchieved.text = setsAchieved.setTwo.toString()

        setThreePlanned.text = setsPlanned.setThree.toString()
        setThreeAchieved.text = setsAchieved.setThree.toString()

        setFourPlanned.text = setsPlanned.setFour.toString()
        setFourAchieved.text = setsAchieved.setFour.toString()

        setFivePlanned.text = setsPlanned.setFive.toString()
        setFiveAchieved.text = setsAchieved.setFive.toString()

        var currentSet = ArrayList<Int>()

        if (setsAchieved.setFive == 0) {
            currentSet.add(5)
            currentSet.add(setsPlanned.setFive)
            setFiveAchieved.text = "-"
        } else if (setsAchieved.setFour == 0) {
            currentSet.add(4)
            currentSet.add(setsPlanned.setFour)
            setFiveAchieved.text = "-"
            setFourAchieved.text = "-"
        } else if (setsAchieved.setThree == 0) {
            currentSet.add(3)
            currentSet.add(setsPlanned.setThree)
            setFiveAchieved.text = "-"
            setFourAchieved.text = "-"
            setThreeAchieved.text = "-"
        } else if (setsAchieved.setTwo == 0) {
            currentSet.add(2)
            currentSet.add(setsPlanned.setTwo)
            setFiveAchieved.text = "-"
            setFourAchieved.text = "-"
            setThreeAchieved.text = "-"
            setTwoAchieved.text = "-"
        } else if (setsAchieved.setOne == 0) {
            currentSet.add(1)
            currentSet.add(setsPlanned.setOne)
            setFiveAchieved.text = "-"
            setFourAchieved.text = "-"
            setThreeAchieved.text = "-"
            setTwoAchieved.text = "-"
            setOneAchieved.text = "-"
        }

        return currentSet
    }


}
