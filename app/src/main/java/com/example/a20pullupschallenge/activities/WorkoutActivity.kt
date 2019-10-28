package com.example.a20pullupschallenge.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Scene
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.a20pullupschallenge.DayWorkout
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.databases.MyDatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_workout.*
import kotlinx.android.synthetic.main.lo_workout_set.*
import org.jetbrains.anko.startActivity

class WorkoutActivity : AppCompatActivity() {

    val Context.planDatabase: MyDatabaseOpenHelper
        get() = MyDatabaseOpenHelper.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        // get intent extra from previous activity saying the week and day
        val weekCurrent = intent.getIntExtra("week",0)
        val dayCurrent = intent.getIntExtra("day",0)
        // get workout from database and create a DayWorkout of the plan
        val dayWorkoutPlan = planDatabase.getNextWorkout(weekCurrent,dayCurrent)
        // create a DayWorkout based on the plan but with placeholder values for accomplished pullups
//        var dayWorkoutAchieved = popWorkAccomp(dayWorkoutPlan)

        // setup two arrayLists for planned workout and achieved workout
        val setsPlanned : ArrayList<Int> = arrayListOf(
            0, // set number
            dayWorkoutPlan.workoutSets.setOne,
            dayWorkoutPlan.workoutSets.setTwo,
            dayWorkoutPlan.workoutSets.setThree,
            dayWorkoutPlan.workoutSets.setFour,
            dayWorkoutPlan.workoutSets.setFive)
        val setsAchieved : ArrayList<Int> = arrayListOf(0,0,0,0,0,0,0)


        // Load layouts as scenes
        val sceneRoot: ViewGroup = findViewById(R.id.fragLayout)
        val welcomeScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_welcome, this)
        val pullupScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_set, this)
        val restScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_rest, this)
        val completeScene: Scene = Scene.getSceneForLayout(sceneRoot, R.layout.lo_workout_complete, this)

        // Update values of main layout
        // TODO change "8" to total number of weeks in workout
        weekText.text = getString(R.string.week_x_of_y, weekCurrent.toString(), "8")
        dayText.text = getString(R.string.day_x_of_y, dayCurrent.toString(), "3")


        // Layout manager
        TransitionManager.go(welcomeScene)
        populateWelcomeScene(setsPlanned)

        // set var with name of active scene
        var activeScene = "welcome"
        // set var with number of current set
        var currentSetNumber = 1

        // on click for the main button that changes the text displayed in it
        // and the action depending on the active state
        mainBtn.setOnClickListener {
            when (activeScene) {
                /// if we are on welcome scene, take us to pullup
                "welcome" -> {
                    TransitionManager.go(pullupScene)
                    mainBtn.text = getString(R.string.done)
                    activeScene = "pullup"

                    val setNumber : TextView = findViewById(R.id.setNumber)
                    val pullupNumber : TextView = findViewById(R.id.pullupNumber)
                    val achievedPullupsNumber : TextView = findViewById(R.id.achievedPullupsNumber)

                    populateTable(setsPlanned, setsAchieved)

                    setNumber.text = getString(R.string.set_number, currentSetNumber.toString())
                    pullupNumber.text = setsPlanned[currentSetNumber].toString()
                    achievedPullupsNumber.text = setsPlanned[currentSetNumber].toString()
                }

                /// if we are on pullup scene, take us to rest, or if set is completed, take us to completion
                "pullup" -> {
                    var viewEdit = findViewById<TextView>(R.id.achievedPullupsNumber)
                    setsAchieved[currentSetNumber] = viewEdit.text.toString().toInt()

                    if(currentSetNumber == 5) {
                        TransitionManager.go(completeScene)
                        cancelBtn.visibility = View.GONE
                        mainBtn.text = getString(R.string.workout_complete_great)
                        activeScene = "complete"

                    } else {
                        TransitionManager.go(restScene)
                        mainBtn.text = getString(R.string.skip_rest)


                        activeScene = "rest"

                        populateTable(setsPlanned, setsAchieved)

                        /// Timer for resting
                        val progressBar: ProgressBar = findViewById(R.id.progressBar)
                        val timeText: TextView = findViewById(R.id.timeText)
                        val countDownTimer: CountDownTimer
                        var i = 0

                        progressBar.isIndeterminate = false
                        progressBar.progress = (120 - i)
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
                }

                /// if we are on rest scene, take us to pullup
                "rest" -> {
                    TransitionManager.go(pullupScene)
                    mainBtn.text = getString(R.string.done)
                    activeScene = "pullup"

                    val setNumber : TextView = findViewById(R.id.setNumber)
                    val pullupNumber : TextView = findViewById(R.id.pullupNumber)
                    val achievedPullupsNumber : TextView = findViewById(R.id.achievedPullupsNumber)


                    setNumber.text = getString(R.string.set_number, currentSetNumber.toString())
                    pullupNumber.text = setsPlanned[currentSetNumber].toString()
                    achievedPullupsNumber.text = setsPlanned[currentSetNumber].toString()

                    currentSetNumber += 1

                }

                "complete" -> startActivity<MainActivity>()
            }
        }
    }

    private fun populateWelcomeScene(setsPlanned : ArrayList<Int>) {
        val setOnePullups : TextView = findViewById(R.id.setOnePlan)
        val setTwoPullups : TextView = findViewById(R.id.setTwoPullups)
        val setThreePullups : TextView = findViewById(R.id.setThreePullups)
        val setFourPullups : TextView = findViewById(R.id.setFourPullups)
        val setFivePullups : TextView = findViewById(R.id.setFivePullups)

        setOnePullups.text = getString(R.string.pullups_plan, setsPlanned[1].toString())
        setTwoPullups.text = getString(R.string.pullups_plan, setsPlanned[2].toString())
        setThreePullups.text = getString(R.string.pullups_plan, setsPlanned[3].toString())
        setFourPullups.text = getString(R.string.pullups_plan, setsPlanned[4].toString())
        setFivePullups.text = getString(R.string.pullups_plan, setsPlanned[5].toString())
    }

    private fun popWorkAccomp(dayWorkoutPlan : DayWorkout) : DayWorkout {
        val workAccomp = DayWorkout()
        workAccomp.week = dayWorkoutPlan.week
        workAccomp.day = dayWorkoutPlan.day
        workAccomp.planId = dayWorkoutPlan.planId
        workAccomp.totNumWeeks = dayWorkoutPlan.totNumWeeks
        workAccomp.status = "accomp"
        workAccomp.workoutSets.setOne = 0
        workAccomp.workoutSets.setTwo = 0
        workAccomp.workoutSets.setThree = 0
        workAccomp.workoutSets.setFour = 0
        workAccomp.workoutSets.setFive = 0

        return workAccomp
    }


    // populates table of planned and achieved pullups plus returns an Int that says which set it is
    private fun populateTable(setsPlanned : ArrayList<Int>, setsAchieved : ArrayList<Int>) {
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

        setOnePlanned.text = setsPlanned[1].toString()
        setTwoPlanned.text = setsPlanned[2].toString()
        setThreePlanned.text = setsPlanned[3].toString()
        setFourPlanned.text = setsPlanned[3].toString()
        setFivePlanned.text = setsPlanned[4].toString()

        if (setsAchieved[1] == 0) {setOneAchieved.text = "-"} else {setOneAchieved.text = setsAchieved[1].toString()}
        if (setsAchieved[2] == 0) {setTwoAchieved.text = "-"} else {setTwoAchieved.text = setsAchieved[2].toString()}
        if (setsAchieved[3] == 0) {setThreeAchieved.text = "-"} else {setThreeAchieved.text = setsAchieved[3].toString()}
        if (setsAchieved[4] == 0) {setFourAchieved.text = "-"} else {setFourAchieved.text = setsAchieved[4].toString()}
        if (setsAchieved[5] == 0) {setFiveAchieved.text = "-"} else {setFiveAchieved.text = setsAchieved[5].toString()}
    }


}
