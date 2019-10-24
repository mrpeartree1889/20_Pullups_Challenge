package com.example.a20pullupschallenge.activities

import android.os.Bundle
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.SampleSlide
import com.github.paolorotolo.appintro.AppIntro

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