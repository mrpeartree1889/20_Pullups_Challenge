package com.example.a20pullupschallenge.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.databases.MyDatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_test.*
import org.jetbrains.anko.startActivity

class MidTestActivity : AppCompatActivity() {

    val Context.planDatabase: MyDatabaseOpenHelper
        get() = MyDatabaseOpenHelper.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val currentWeek = intent.getIntExtra("week", 0)

        weeksCompleteText.text = getString(R.string.test_main_text, currentWeek.toString())

        var numberPicked = 0
        if (testNumberPicker != null) {
            testNumberPicker.minValue = 3
            testNumberPicker.maxValue = 20
            testNumberPicker.wrapSelectorWheel = false
            testNumberPicker.setOnValueChangedListener { _, _, newVal ->
                numberPicked = newVal
            }
        }

        testCompleteBtn.setOnClickListener() {
            performTestLo.visibility = View.GONE
            endLo.visibility = View.VISIBLE
            imageView4.setImageResource(R.drawable.test_arm)

            when(numberPicked) {
                in 2..9 -> {
                    testTitle.text = getString(R.string.good_job_test)
                    completionText.text = getString(R.string.good_job_test_text)
                    planDatabase.restartWeeks(currentWeek, 1)
                }

                in 9..21 -> {
                    planDatabase.updatePlan(numberPicked)
                    testTitle.text = getString(R.string.congratulation)
                    completionText.text = getString(R.string.congratulation_text)
                }
            }

            goToPlan.setOnClickListener {
                startActivity<MainActivity>()
            }
        }
    }

    fun startTestBtnClicked(view: View) {
        introLo.visibility = View.GONE
        performTestLo.visibility = View.VISIBLE
    }
}
