package com.example.a20pullupschallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_plan.*
import org.jetbrains.anko.startActivity

class CreatePlan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_plan)

        var numberPicked = 0
        if (createPlanNumberPicker != null) {
            createPlanNumberPicker.minValue = 0
            createPlanNumberPicker.maxValue = 20
            createPlanNumberPicker.wrapSelectorWheel = false
            createPlanNumberPicker.setOnValueChangedListener { _, _, newVal ->
                numberPicked = newVal
            }
        }

        btnCreatePlan.setOnClickListener() {
            startActivity<MainActivity>("initialPullups" to numberPicked)
        }
    }
}
