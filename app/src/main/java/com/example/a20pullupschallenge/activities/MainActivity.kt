package com.example.a20pullupschallenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.adapters.PlanAdapter
import com.example.a20pullupschallenge.databases.MyDatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.jetbrains.annotations.TestOnly

const val PREFS_FILENAME = "com.example.a20pullupschallenge.prefs"

class MainActivity : AppCompatActivity() {

    val Context.planDatabase: MyDatabaseOpenHelper
        get() = MyDatabaseOpenHelper.getInstance(this)

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
        if(!planDatabase.checkTable()) {
            createWorkoutLayout.visibility = View.VISIBLE
            workoutPlanLayout.visibility = View.GONE
            btnMakePlan.setOnClickListener() {
                startActivity<CreatePlan>()
            }
        } else if (planDatabase.checkTable()) {
            createWorkoutLayout.visibility = View.GONE
            workoutPlanLayout.visibility = View.VISIBLE

            viewPlan()
        }

    }

    fun optionsBtnClicked(view: View) {
        fun focusOnBtns() {
            createNewPlanBtn.visibility = View.VISIBLE
            disclaimerBtn.visibility = View.VISIBLE
            clickLayout.visibility = View.VISIBLE
            titleText.alpha = 0.25f
            tableRow.alpha = 0.25f
            rvList.alpha = 0.25f
            startWorkoutBtn.isEnabled = false
            startWorkoutBtn.isClickable = false
        }

        fun focusOnBckg() {
            createNewPlanBtn.visibility = View.GONE
            disclaimerBtn.visibility = View.GONE
            clickLayout.visibility = View.GONE
            titleText.alpha = 1f
            tableRow.alpha = 1f
            rvList.alpha = 1f
            startWorkoutBtn.isEnabled = true
            startWorkoutBtn.isClickable = true
        }

        if (createNewPlanBtn.visibility != View.VISIBLE) {
            focusOnBtns()
            clickLayout.setOnClickListener() {focusOnBckg() }
        } else if (createNewPlanBtn.visibility == View.VISIBLE) {
            focusOnBckg()
        }


    }

    fun createNewPlanBtnClicked (view: View){
        alert("If you continue, you will delete your current progress. Are you sure you want to proceed?", "Attention") {
            positiveButton("Yes, proceed") {
                planDatabase.dropTable()
                startActivity<CreatePlan>()
            }
            negativeButton("No, go back") {}
        }.show()

    }

    fun disclaimerBtnClicked(view: View) {
        alert("If you continue, you will delete your current progress. Are you sure you want to proceed?", "Disclaimer") {
            title = "test"
            message = "asdiasoidhauhfashdlajsldkj"
            positiveButton("Ok") {}
        }.show()
    }

    fun startWorkoutBtnClicked(view:View) {
        startActivity<WorkoutActivity>()
    }

    private fun viewPlan() {
        val plan = planDatabase.getPlan()
        val adapter = PlanAdapter(this, plan, this)
        val rv : RecyclerView = findViewById(R.id.rvList)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        rv.adapter = adapter
    }
}
