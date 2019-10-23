package com.example.a20pullupschallenge

import android.os.Bundle
import android.widget.NumberPicker
import com.github.paolorotolo.appintro.AppIntro
import kotlinx.android.synthetic.main.slide_two.*

class IntroActivity : AppIntro() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(SampleSlide.newInstance(R.layout.slide_one))
        addSlide(SampleSlide.newInstance(R.layout.slide_two))

        setFadeAnimation() // animation between slides
    }

    override fun onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    override fun onNextPressed() {
        // Do something when users tap on Next button.
    }

    override fun onDonePressed() {
        // Do something when users tap on Done button.
        finish()
    }

    override fun onSlideChanged() {
        // Do something when slide is changed
    }
}