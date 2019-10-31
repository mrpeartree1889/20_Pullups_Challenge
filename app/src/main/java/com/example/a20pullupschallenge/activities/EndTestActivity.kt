package com.example.a20pullupschallenge.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.databases.MyDatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_end_test.*
import org.jetbrains.anko.startActivity

class EndTestActivity : AppCompatActivity() {

    val Context.planDatabase: MyDatabaseOpenHelper
        get() = MyDatabaseOpenHelper.getInstance(this)

    var currentWeek = 0
    var numberPicked = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_test)

        currentWeek = intent.getIntExtra("week", 0)

        weeksCompleteText.text = getString(R.string.test_main_text, currentWeek.toString())

        if (testNumberPicker != null) {
            testNumberPicker.minValue = 3
            testNumberPicker.maxValue = 20
            testNumberPicker.wrapSelectorWheel = false
            testNumberPicker.setOnValueChangedListener { _, _, newVal ->
                numberPicked = newVal
            }
        }

//        testCompleteBtn.setOnClickListener() {
//            // if managed to do 20 pullups, take to the challenge complete activity
//            if(numberPicked == 20) {
//                startActivity<ChallengeCompleteActivity>()
//            } else {
//                performTestLo.visibility = View.GONE
//                endLo.visibility = View.VISIBLE
//                imageView4.setImageResource(R.drawable.test_arm)
//
//                when(numberPicked) {
//                    // if managed to to between 3 and 14 pullups, repeat 2 last weeks
//                    in 3..14 -> {
//                        testTitle.text = getString(R.string.good_job_test)
//                        completionText.text = getString(R.string.good_job_final_test_text, currentWeek.toString())
//                    }
//
//                    // if managed to do between 15 and 19 pullups, repeat past week
//                    in 15..19 -> {
//                        planDatabase.updatePlan(numberPicked)
//                        testTitle.text = getString(R.string.congratulation)
//                        completionText.text = getString(R.string.congratulation_final_text, currentWeek.toString())
//                    }
//                }
//
//                goToPlan.setOnClickListener {
//                    startActivity<MainActivity>()
//                }
//            }
//        }
    }

    fun startTestBtnClicked(view: View) {
        introLo.visibility = View.GONE
        performTestLo.visibility = View.VISIBLE
    }

    fun testCompleteBtnClicked(view:View) {
        if(numberPicked == 20) {
            startActivity<ChallengeCompleteActivity>()
        } else {

            performTestLo.visibility = View.GONE
            endLo.visibility = View.VISIBLE
            imageView4.setImageResource(R.drawable.test_arm)

            when(numberPicked) {
                // if managed to to between 3 and 14 pullups, repeat 2 last weeks
                in 3..14 -> {
                    planDatabase.restartWeeks(currentWeek, 2)
                    testTitle.text = getString(R.string.good_job_test)
                    completionText.text = getString(R.string.good_job_final_test_text, currentWeek.toString())
                }

                // if managed to do between 15 and 19 pullups, repeat past week
                in 15..19 -> {
                    planDatabase.restartWeeks(currentWeek, 1)
                    testTitle.text = getString(R.string.congratulation)
                    completionText.text = getString(R.string.congratulation_final_text, currentWeek.toString())
                }
            }

            goToPlan.setOnClickListener {
                startActivity<MainActivity>()
            }
        }
    }
}