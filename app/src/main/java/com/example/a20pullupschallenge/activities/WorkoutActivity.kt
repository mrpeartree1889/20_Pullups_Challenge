package com.example.a20pullupschallenge.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.*
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.a20pullupschallenge.DayWorkout
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.databases.MyDatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_workout.*
import org.jetbrains.anko.alert
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
        weekText.text = getString(R.string.week_x_of_y, weekCurrent.toString(), dayWorkoutPlan.totNumWeeks.toString())
        dayText.text = getString(R.string.day_x_of_y, dayCurrent.toString(), "3")

        // Set transition
        var slideFromBottom : Transition = Slide(Gravity.BOTTOM)
        slideFromBottom.duration = 1000
        slideFromBottom.startDelay = 200

        // Layout manager
        TransitionManager.go(welcomeScene, slideFromBottom)
        populateWelcomeScene(setsPlanned)

        // set var with name of active scene
        var activeScene = "welcome"
        // set var with number of current set
        var currentSetNumber = 1



        // on click for the main button that changes the text displayed in it
        // and the action depending on the active state
        mainBtn.setOnClickListener {
            fun startPullupScene() {
                TransitionManager.go(pullupScene, slideFromBottom)
                keepTrackTableLo.visibility = View.VISIBLE
                mainBtn.text = getString(R.string.done)
                activeScene = "pullup"

                val setNumber : TextView = findViewById(R.id.setNumber)
                val pullupNumber : TextView = findViewById(R.id.pullupNumber)
                val achievedPullupsNumber : TextView = findViewById(R.id.achievedPullupsNumber)
                setNumber.text = getString(R.string.set_number, currentSetNumber.toString())
                pullupNumber.text = setsPlanned[currentSetNumber].toString()
                achievedPullupsNumber.text = setsPlanned[currentSetNumber].toString()

                // action on plus and minus button
                val plusBtn = findViewById<TextView>(R.id.buttonPlus)
                val minusBtn = findViewById<TextView>(R.id.buttonMinus)
                plusBtn.setOnClickListener {plusBtnAction(findViewById(R.id.achievedPullupsNumber))}
                minusBtn.setOnClickListener {minusBtnAction(findViewById(R.id.achievedPullupsNumber))}

                populateTable(setsPlanned, setsAchieved, currentSetNumber)
            }

            when (activeScene) {
                /// if we are on welcome or rest scene, take us to pullup
                "welcome" -> {
                    startPullupScene()
                }
                "rest" -> {
                    currentSetNumber += 1
                    startPullupScene()
                }

                /// if we are on pullup scene, take us to rest, or if set is completed, take us to completion
                "pullup" -> {

                    val viewEdit = findViewById<TextView>(R.id.achievedPullupsNumber)
                    setsAchieved[currentSetNumber] = viewEdit.text.toString().toInt()

                    if(currentSetNumber == 5) {
                        TransitionManager.go(completeScene, slideFromBottom)
                        keepTrackTableLo.visibility = View.GONE

                        currentSetNumber = 6
                        populateTable(setsPlanned, setsAchieved, currentSetNumber)

                        val completedPullups = setsAchieved.sum() - setsAchieved[0]
                        val completePullupsText : TextView = findViewById(R.id.textView16)
                        completePullupsText.text = getString(R.string.pullups_achieved, completedPullups.toString())
                        cancelBtn.visibility = View.GONE
                        mainBtn.text = getString(R.string.workout_complete_great)
                        activeScene = "complete"

                    } else {
                        TransitionManager.go(restScene, slideFromBottom)

                        // change text on button, on set number and populate table
                        mainBtn.text = getString(R.string.skip_rest)
                        val setNumber : TextView = findViewById(R.id.setNumber)
                        setNumber.text = getString(R.string.set_number, currentSetNumber.toString())
                        populateTable(setsPlanned, setsAchieved, currentSetNumber)

                        activeScene = "rest"

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
                                TransitionManager.go(pullupScene, slideFromBottom)
                                mainBtn.text = getString(R.string.done)
                                activeScene = "pullup"

                                val setNumber : TextView = findViewById(R.id.setNumber)
                                val pullupNumber : TextView = findViewById(R.id.pullupNumber)
                                val achievedPullupsNumber : TextView = findViewById(R.id.achievedPullupsNumber)


                                setNumber.text = getString(R.string.set_number, currentSetNumber.toString())
                                pullupNumber.text = setsPlanned[currentSetNumber].toString()
                                achievedPullupsNumber.text = setsPlanned[currentSetNumber].toString()

                                populateTable(setsPlanned, setsAchieved, currentSetNumber)

                                currentSetNumber += 1
                            }
                        }
                        countDownTimer.start()
                    }
                }

                "complete" -> {
                    updateDatabase(dayWorkoutPlan, setsAchieved)

                    if ((weekCurrent == 3 && dayCurrent == 3) || (weekCurrent == 6 && dayCurrent == 3)) {
                        startActivity<MidTestActivity>()
                    } else {
                        startActivity<MainActivity>()
                    }

                }
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

    // populates table of planned and achieved pullups plus returns an Int that says which set it is
    private fun populateTable(setsPlanned : ArrayList<Int>, setsAchieved : ArrayList<Int>, currentSetNumber : Int) {
        /// populate values
        val setOnePlanned: TextView = findViewById(R.id.setOnePlanUnique)
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
        setFourPlanned.text = setsPlanned[4].toString()
        setFivePlanned.text = setsPlanned[5].toString()

        if (setsAchieved[1] == 0) {setOneAchieved.text = "-"} else {setOneAchieved.text = setsAchieved[1].toString()}
        if (setsAchieved[2] == 0) {setTwoAchieved.text = "-"} else {setTwoAchieved.text = setsAchieved[2].toString()}
        if (setsAchieved[3] == 0) {setThreeAchieved.text = "-"} else {setThreeAchieved.text = setsAchieved[3].toString()}
        if (setsAchieved[4] == 0) {setFourAchieved.text = "-"} else {setFourAchieved.text = setsAchieved[4].toString()}
        if (setsAchieved[5] == 0) {setFiveAchieved.text = "-"} else {setFiveAchieved.text = setsAchieved[5].toString()}

        /// change background on active set
        val setOneTitle : TextView = findViewById(R.id.setOneTitle)
        val setTwoTitle : TextView = findViewById(R.id.setTwoTitle)
        val setThreeTitle : TextView = findViewById(R.id.setThreeTitle)
        val setFourTitle : TextView = findViewById(R.id.setFourTitle)
        val setFiveTitle : TextView = findViewById(R.id.setFiveTitle)

        if (currentSetNumber == 1) {
            setOneTitle.setBackgroundResource(R.color.colorPrimaryDark)
            setOneTitle.setTextColor(getColor(R.color.colorTextWhite))
        }
        if (currentSetNumber == 2) {
            setTwoTitle.setBackgroundResource(R.color.colorPrimaryDark)
            setTwoTitle.setTextColor(getColor(R.color.colorTextWhite))}
        if (currentSetNumber == 3) {
            setThreeTitle.setBackgroundResource(R.color.colorPrimaryDark)
            setThreeTitle.setTextColor(getColor(R.color.colorTextWhite))
        }
        if (currentSetNumber == 4) {
            setFourTitle.setBackgroundResource(R.color.colorPrimaryDark)
            setFourTitle.setTextColor(getColor(R.color.colorTextWhite))
        }
        if (currentSetNumber == 5) {
            setFiveTitle.setBackgroundResource(R.color.colorPrimaryDark)
            setFiveTitle.setTextColor(getColor(R.color.colorTextWhite))
        }

        if (currentSetNumber == 6) {
            if (setsPlanned[1] <= setsAchieved[1]) {
                setOneAchieved.setBackgroundResource(R.color.colorAppGreen)
            } else if (setsPlanned[4] > setsAchieved[4]){
                setOneAchieved.setBackgroundResource(R.color.colorAppOrange)
            }
            if (setsPlanned[2] <= setsAchieved[2]) {
                setTwoAchieved.setBackgroundResource(R.color.colorAppGreen)
            } else if (setsPlanned[4] > setsAchieved[4]){
                setTwoAchieved.setBackgroundResource(R.color.colorAppOrange)
            }
            if (setsPlanned[3] <= setsAchieved[3]) {
                setThreeAchieved.setBackgroundResource(R.color.colorAppGreen)
            } else if (setsPlanned[4] > setsAchieved[4]){
                setThreeAchieved.setBackgroundResource(R.color.colorAppOrange)
            }
            if (setsPlanned[4] <= setsAchieved[4]) {
                setFourAchieved.setBackgroundResource(R.color.colorAppGreen)
            } else if (setsPlanned[4] > setsAchieved[4]){
                setFourAchieved.setBackgroundResource(R.color.colorAppOrange)
            }
            if (setsPlanned[5] <= setsAchieved[5]) {
                setFiveAchieved.setBackgroundResource(R.color.colorAppGreen)
            } else if (setsPlanned[4] > setsAchieved[4]){
                setFiveAchieved.setBackgroundResource(R.color.colorAppOrange)
            }
        }
    }

    private fun plusBtnAction(achievedPullups : TextView) {
        val value = achievedPullups.text.toString()
        val currentNumber = Integer.parseInt((value))
        var newNumber = currentNumber

        if (currentNumber == -1) {
            newNumber += 2
        } else if (currentNumber != 0 || currentNumber != -1) {
            newNumber += 1
        }

        achievedPullups.text = newNumber.toString()
    }

    private fun minusBtnAction(achievedPullups : TextView) {
        val value = achievedPullups.text.toString()
        val currentNumber = Integer.parseInt((value))
        val newNumber = currentNumber - 1
        achievedPullups.text = newNumber.toString()
    }

    fun cancelBtnClicked(view:View) {
        alert("If you continue, you will delete your current progress. Are you sure you want to proceed?", "Attention") {
            positiveButton("Yes, proceed") { startActivity<MainActivity>() }
            negativeButton("No, continue workout") {}
        }.show()
    }

    fun updateDatabase(dayWorkoutPlan : DayWorkout, setsAchieved : ArrayList<Int>) {
        // change status on planned workout
        planDatabase.changeStatus(dayWorkoutPlan.week, dayWorkoutPlan.day)

        // add accomplished workout
        val dayWorkoutCompleted = dayWorkoutPlan
        dayWorkoutCompleted.status = "accomp"
        dayWorkoutCompleted.workoutSets.setOne = setsAchieved[1]
        dayWorkoutCompleted.workoutSets.setTwo = setsAchieved[2]
        dayWorkoutCompleted.workoutSets.setThree = setsAchieved[3]
        dayWorkoutCompleted.workoutSets.setFour = setsAchieved[4]
        dayWorkoutCompleted.workoutSets.setFive = setsAchieved[5]

        planDatabase.addAccomplishedWorkout(dayWorkoutCompleted)
    }
}
