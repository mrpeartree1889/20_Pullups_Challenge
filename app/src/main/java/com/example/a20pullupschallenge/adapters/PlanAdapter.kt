package com.example.a20pullupschallenge.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a20pullupschallenge.DayWorkout
import com.example.a20pullupschallenge.R
import com.example.a20pullupschallenge.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.view.setFive
import kotlinx.android.synthetic.main.activity_main.view.setFour
import kotlinx.android.synthetic.main.activity_main.view.setOne
import kotlinx.android.synthetic.main.activity_main.view.setThree
import kotlinx.android.synthetic.main.activity_main.view.setTwo
import kotlinx.android.synthetic.main.lo_day_workout.view.*
import org.jetbrains.anko.textColorResource

class PlanAdapter(val context: Context, val plan :  ArrayList<DayWorkout>, val activity: MainActivity) : RecyclerView.Adapter<PlanAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return plan.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lo_day_workout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayWorkout : DayWorkout = plan[position]

        holder.week.text = "Week " + dayWorkout.week.toString()
        holder.day.text = dayWorkout.day.toString()
        holder.setOne.text = getNumber(dayWorkout.workoutSets.setOne)
        holder.setTwo.text = getNumber(dayWorkout.workoutSets.setTwo)
        holder.setThree.text = getNumber(dayWorkout.workoutSets.setThree)
        holder.setFour.text = getNumber(dayWorkout.workoutSets.setFour)
        holder.setFive.text = getNumber(dayWorkout.workoutSets.setFive)

        if (dayWorkout.day > 1) {
            holder.weekLo.visibility = View.GONE
        } else {
            holder.weekLo.visibility = View.VISIBLE
        }

        when {
            dayWorkout.status == "accomp" -> holder.tableLo.alpha = 0.4f
            dayWorkout.status == "next" -> {
                holder.tableLo.alpha = 1.0f

                holder.day.setBackgroundResource(R.color.colorPrimary)
                holder.setOne.setBackgroundResource(R.color.colorPrimaryDark)
                holder.setTwo.setBackgroundResource(R.color.colorPrimaryDark)
                holder.setThree.setBackgroundResource(R.color.colorPrimaryDark)
                holder.setFour.setBackgroundResource(R.color.colorPrimaryDark)
                holder.setFive.setBackgroundResource(R.color.colorPrimaryDark)

                holder.day.textColorResource = R.color.colorTextWhite
                holder.setOne.textColorResource = R.color.colorTextWhite
                holder.setTwo.textColorResource = R.color.colorTextWhite
                holder.setThree.textColorResource = R.color.colorTextWhite
                holder.setFour.textColorResource = R.color.colorTextWhite
                holder.setFive.textColorResource = R.color.colorTextWhite
            }
            else -> {
                holder.tableLo.alpha = 1.0f

                holder.day.setBackgroundColor(Color.TRANSPARENT)
                holder.setOne.setBackgroundColor(Color.parseColor("#0D000000"))
                holder.setTwo.setBackgroundColor(Color.parseColor("#0D000000"))
                holder.setThree.setBackgroundColor(Color.parseColor("#0D000000"))
                holder.setFour.setBackgroundColor(Color.parseColor("#0D000000"))
                holder.setFive.setBackgroundColor(Color.parseColor("#0D000000"))

                holder.day.textColorResource = R.color.colorTextPrimary
                holder.setOne.textColorResource = R.color.colorTextPrimary
                holder.setTwo.textColorResource = R.color.colorTextPrimary
                holder.setThree.textColorResource = R.color.colorTextPrimary
                holder.setFour.textColorResource = R.color.colorTextPrimary
                holder.setFive.textColorResource = R.color.colorTextPrimary
            }
        }
    }

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val day = itemView.dayText
        val tableLo = itemView.dayWorkoutLo
        val weekLo = itemView.weekLo
        val setOne = itemView.setOne
        val setTwo = itemView.setTwo
        val setThree = itemView.setThree
        val setFour = itemView.setFour
        val setFive = itemView.setFive
        val week = itemView.weekText
    }

    // fun that returns "max" if number of pullups is 20
    private fun getNumber(num : Int) : String {
        var numberAsString : String = ""
        when (num) {
            20 -> numberAsString = "max"
            in 0..21 -> numberAsString = num.toString()
        }
        return numberAsString
    }
}