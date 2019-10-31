package com.example.a20pullupschallenge.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.a20pullupschallenge.DayWorkout
import com.example.a20pullupschallenge.Sets
import org.jetbrains.anko.db.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyDatabaseOpenHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PlansDatabase", null, 1) {
    init { instance = this }

    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: MyDatabaseOpenHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("Plan", true)
    }

    fun populatePlan (numInitialPullups: Int) {
        // get today's date
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val currentDate = current.format(formatter)

        // initialize DayWorkout class
        val dayWorkout = DayWorkout()

        // open DB
        val db = this.writableDatabase

        // function to add new line to database
        fun addWorkoutToDB (dayWorkout: DayWorkout) {
            db.insert("Plan",
                "planId" to dayWorkout.planId,
                "startDate" to dayWorkout.startDate,
                "week" to dayWorkout.week,
                "day" to dayWorkout.day,
                "status" to dayWorkout.status,
                "setOne" to dayWorkout.workoutSets.setOne,
                "setTwo" to dayWorkout.workoutSets.setTwo,
                "setThree" to dayWorkout.workoutSets.setThree,
                "setFour" to dayWorkout.workoutSets.setFour,
                "setFive" to dayWorkout.workoutSets.setFive)
        }

        // WORKOUT PLANS
        when (numInitialPullups) {
            // BASIC PLANS (3 TO 5 PULLUPS)
            in 2..6 -> {
                dayWorkout.planId = "bas_${numInitialPullups}_${currentDate}"
                dayWorkout.startDate = currentDate
                dayWorkout.totNumWeeks = 6
                dayWorkout.status = "plan"

                for (week in 1..dayWorkout.totNumWeeks) {
                    for (day in 1..3) {
                        val newDayWorkout = getBasicPlan(dayWorkout, week, day)
                        addWorkoutToDB(newDayWorkout)
                    }
                }
            }

            // INTERMEDIATE PLANS (6 TO 9 PULLUPS)
            in 5..10 -> {
                dayWorkout.planId = "int_${numInitialPullups}_${currentDate}"
                dayWorkout.startDate = currentDate
                dayWorkout.totNumWeeks = 6
                dayWorkout.status = "plan"

                for (week in 1..dayWorkout.totNumWeeks) {
                    for (day in 1..3) {
                        val newDayWorkout = getIntermediatePlan(dayWorkout, week, day)
                        addWorkoutToDB(newDayWorkout)
                    }
                }
            }

            // ADVANCED PLANS (10+ PULLUPS)
            in 9..25 -> {
                dayWorkout.planId = "adv_${numInitialPullups}_${currentDate}"
                dayWorkout.startDate = currentDate
                dayWorkout.totNumWeeks = 6
                dayWorkout.status = "plan"

                for (week in 1..dayWorkout.totNumWeeks) {
                    for (day in 1..3) {
                        val newDayWorkout = getAdvancedPlan(dayWorkout, week, day)
                        addWorkoutToDB(newDayWorkout)
                    }
                }
            }
        }

        db.close()
    }

    private fun getBasicPlan(dayWorkout: DayWorkout, week : Int, day : Int) : DayWorkout {
        if (week == 1 && day == 1) {dayWorkout.status = "next"} else {dayWorkout.status = "plan"}
        dayWorkout.week = week
        dayWorkout.day = day

        when {
            week == 1 && day == 1-> {dayWorkout.workoutSets = Sets(2,1,1,2,3) }
            week == 1 && day == 2-> {dayWorkout.workoutSets = Sets(2,2,1,2,3)}
            week == 1 && day == 3-> {dayWorkout.workoutSets = Sets(2,2,2,2,20) }

            week == 2 && day == 1-> {dayWorkout.workoutSets = Sets(3,2,3,2,3)}
            week == 2 && day == 2-> {dayWorkout.workoutSets = Sets(3,3,2,3,3)}
            week == 2 && day == 3-> {dayWorkout.workoutSets = Sets(3,3,3,2,20)}

            week == 3 && day == 1-> {dayWorkout.workoutSets = Sets(4,3,4,3,4)}
            week == 3 && day == 2-> {dayWorkout.workoutSets = Sets(5,3,4,3, 5)}
            week == 3 && day == 3-> {dayWorkout.workoutSets = Sets(6,5,4,3,20)}

            week == 4 && day == 1-> {dayWorkout.workoutSets = Sets(5,4,5,6,7)}
            week == 4 && day == 2-> {dayWorkout.workoutSets = Sets(7,6,5,6, 7)}
            week == 4 && day == 3-> {dayWorkout.workoutSets = Sets(8,6,5,4,20)}

            week == 5 && day == 1-> {dayWorkout.workoutSets = Sets(4,5,6,6,7)}
            week == 5 && day == 2-> {dayWorkout.workoutSets = Sets(8,7,8, 7, 8)}
            week == 5 && day == 3-> {dayWorkout.workoutSets = Sets(5,6,8,9,10)}

            week == 6 && day == 1-> {dayWorkout.workoutSets = Sets(5,6,7,8,9)}
            week == 6 && day == 2-> {dayWorkout.workoutSets = Sets(8,9,10, 11, 12)}
            week == 6 && day == 3-> {dayWorkout.workoutSets = Sets(13,11,9,7,20)}
        }

        return dayWorkout
    }

    private fun getIntermediatePlan(dayWorkout: DayWorkout, week : Int, day : Int) : DayWorkout {
        if (week == 1 && day == 1) {dayWorkout.status = "next"} else {dayWorkout.status = "plan"}
        dayWorkout.week = week
        dayWorkout.day = day

        when {
            week == 1 && day == 1-> {dayWorkout.workoutSets = Sets(4,3,2,4,5) }
            week == 1 && day == 2-> {dayWorkout.workoutSets = Sets(4,3,3,4,5)}
            week == 1 && day == 3-> {dayWorkout.workoutSets = Sets(4,4,4,3,20) }

            week == 2 && day == 1-> {dayWorkout.workoutSets = Sets(5,4,3,4,5)}
            week == 2 && day == 2-> {dayWorkout.workoutSets = Sets(4,4,3,4,6)}
            week == 2 && day == 3-> {dayWorkout.workoutSets = Sets(4,4,4,4,20)}

            week == 3 && day == 1-> {dayWorkout.workoutSets = Sets(6,5,4,5,6)}
            week == 3 && day == 2-> {dayWorkout.workoutSets = Sets(6,5,6,5, 6)}
            week == 3 && day == 3-> {dayWorkout.workoutSets = Sets(7,6,5,4,20)}

            week == 4 && day == 1-> {dayWorkout.workoutSets = Sets(5,6,7,8,9)}
            week == 4 && day == 2-> {dayWorkout.workoutSets = Sets(7,6,7,8, 9)}
            week == 4 && day == 3-> {dayWorkout.workoutSets = Sets(10,8,7,6,20)}

            week == 5 && day == 1-> {dayWorkout.workoutSets = Sets(5,6,7,8,9)}
            week == 5 && day == 2-> {dayWorkout.workoutSets = Sets(10,8,10, 8, 10)}
            week == 5 && day == 3-> {dayWorkout.workoutSets = Sets(7,8,9,11,12)}

            week == 6 && day == 1-> {dayWorkout.workoutSets = Sets(6,7,8,9,11)}
            week == 6 && day == 2-> {dayWorkout.workoutSets = Sets(10,11,12, 13, 14)}
            week == 6 && day == 3-> {dayWorkout.workoutSets = Sets(14,12,11,9,20)}
        }

        return dayWorkout
    }

    private fun getAdvancedPlan(dayWorkout: DayWorkout, week : Int, day : Int) : DayWorkout {
        if (week == 1 && day == 1) {dayWorkout.status = "next"} else {dayWorkout.status = "plan"}
        dayWorkout.week = week
        dayWorkout.day = day

        when {
            week == 1 && day == 1-> { dayWorkout.workoutSets = Sets(6,6,4,7,9) }
            week == 1 && day == 2-> {dayWorkout.workoutSets = Sets(7,6,6,8,9)}
            week == 1 && day == 3-> {dayWorkout.workoutSets = Sets(8,8,8,6,20) }

            week == 2 && day == 1-> {dayWorkout.workoutSets = Sets(8,7,6,7,8)}
            week == 2 && day == 2-> {dayWorkout.workoutSets = Sets(8,7,7,6,10)}
            week == 2 && day == 3-> {dayWorkout.workoutSets = Sets(8,8,7,7,20)}

            week == 3 && day == 1-> {dayWorkout.workoutSets = Sets(9,7,6,7,9)}
            week == 3 && day == 2-> {dayWorkout.workoutSets = Sets(9,7,8,7, 9)}
            week == 3 && day == 3-> {dayWorkout.workoutSets = Sets(10,8,6,7,20)}

            week == 4 && day == 1-> {dayWorkout.workoutSets = Sets(6,7,8,9,11)}
            week == 4 && day == 2-> {dayWorkout.workoutSets = Sets(7,8,9,10, 11)}
            week == 4 && day == 3-> {dayWorkout.workoutSets = Sets(12,10,8,7,20)}

            week == 5 && day == 1-> {dayWorkout.workoutSets = Sets(6,7,8,9,11)}
            week == 5 && day == 2-> {dayWorkout.workoutSets = Sets(12,10,12, 10, 12)}
            week == 5 && day == 3-> {dayWorkout.workoutSets = Sets(10,11,12,13,15)}

            week == 6 && day == 1-> {dayWorkout.workoutSets = Sets(7,8,9,10,12)}
            week == 6 && day == 2-> {dayWorkout.workoutSets = Sets(11,12,13, 14, 15)}
            week == 6 && day == 3-> {dayWorkout.workoutSets = Sets(15,13,12,10, 20)}
        }

        return dayWorkout
    }


    /// this function returns the full plan based on the answer given on the beginning
    fun getPlan() : ArrayList<DayWorkout> {
        val plan = ArrayList<DayWorkout>()
        val db = this.readableDatabase
        db.select("Plan", "planId", "week", "day", "status", "setOne", "setTwo", "setThree", "setFour", "setFive")
            .whereArgs("(status = {planned}) or (status = {accomplished}) or (status = {next})", "planned" to "plan", "accomplished" to "accomp", "next" to "next")
            .orderBy("week", SqlOrderDirection.ASC)
            .orderBy("day", SqlOrderDirection.ASC)
            .exec {
            if (this.count != 0) {
                while (this.moveToNext()) {
                    val dayWorkout = DayWorkout()
                    dayWorkout.planId = this.getString(this.getColumnIndex("planId"))
                    dayWorkout.week = this.getInt(this.getColumnIndex("week"))
                    dayWorkout.day = this.getInt(this.getColumnIndex("day"))
                    dayWorkout.status = this.getString(this.getColumnIndex("status"))
                    dayWorkout.workoutSets.setOne = this.getInt(this.getColumnIndex("setOne"))
                    dayWorkout.workoutSets.setTwo = this.getInt(this.getColumnIndex("setTwo"))
                    dayWorkout.workoutSets.setThree = this.getInt(this.getColumnIndex("setThree"))
                    dayWorkout.workoutSets.setFour = this.getInt(this.getColumnIndex("setFour"))
                    dayWorkout.workoutSets.setFive = this.getInt(this.getColumnIndex("setFive"))
                    plan.add(dayWorkout)
                }
            }
            this.close()
        }
        db.close()
        return plan
    }

    /// this function returns the week and day number of the next workout
    fun getNextWorkoutWeekAndDay() : ArrayList<Int> {
        val weekAndDay = ArrayList<Int>()
        val db = this.readableDatabase
        db.select("Plan", "week", "day", "status")
            .whereArgs("status = {statusVar}", "statusVar" to "next")
            .exec {
            if (this.count != 0) {
                while (this.moveToNext()) {
                    if (this.getString(this.getColumnIndex("status")) == "next") {
                        weekAndDay.add(this.getInt(this.getColumnIndex("week")))
                        weekAndDay.add(this.getInt(this.getColumnIndex("day")))
                    }
                }
                this.close()
            }
        }
        db.close()
        return weekAndDay
    }

    fun getNextWorkout(week: Int, day: Int) : DayWorkout {
        val dayWorkout = DayWorkout()
        val db = this.readableDatabase
        db.select("Plan", "week", "day", "status", "setOne", "setTwo", "setThree", "setFour", "setFive")
            .whereArgs("(week = {weekNumber}) and (day = {dayNumber})", "weekNumber" to week, "dayNumber" to day)
            .exec {
            if (this.count != 0) {
                this.moveToFirst()
                dayWorkout.week = this.getInt(this.getColumnIndex("week"))
                dayWorkout.day = this.getInt(this.getColumnIndex("day"))
                dayWorkout.status = this.getString(this.getColumnIndex("status"))
                dayWorkout.workoutSets.setOne = this.getInt(this.getColumnIndex("setOne"))
                dayWorkout.workoutSets.setTwo = this.getInt(this.getColumnIndex("setTwo"))
                dayWorkout.workoutSets.setThree = this.getInt(this.getColumnIndex("setThree"))
                dayWorkout.workoutSets.setFour = this.getInt(this.getColumnIndex("setFour"))
                dayWorkout.workoutSets.setFive = this.getInt(this.getColumnIndex("setFive"))

                this.close()
            }
        }
        db.close()
        return dayWorkout
    }

    // this function drops the current table after the user asks to reset progress and start from the beginning
    fun dropTable() {
        val db = this.writableDatabase
        db.dropTable("Plan", true)
        createTable(db)
    }

    // used more than once, dont delete
    private fun createTable(db: SQLiteDatabase) {
        db.createTable("Plan", true,
            "planId" to TEXT,
            "startDate" to TEXT,
            "week" to INTEGER,
            "day" to INTEGER,
            "status" to TEXT, // takes PLAN, NEXT or ACCOMP
            "setOne" to INTEGER,
            "setTwo" to INTEGER,
            "setThree" to INTEGER,
            "setFour" to INTEGER,
            "setFive" to INTEGER)
    }

    // this function checks if there is a table created
    fun checkTable(): Boolean {
        val db = this.readableDatabase
        var week = 0
        db.select("Plan","week").limit(1).exec {
            while (this.moveToNext()) {
                week = this.getInt(this.getColumnIndex("week"))
            }
        }
        return week > 0
    }

    fun changeStatus(week: Int, day: Int) {
        val db = this.writableDatabase
        // update finished workout to show planDone
        db.update("Plan", "status" to "planDone")
            .whereArgs("(week = {weekN}) and (day = {dayN})", "weekN" to week,"dayN" to day)
            .exec()

        // update next workout to show next
        var newDay = day
        var newWeek = week
        if(day == 3) {
            newDay = 1
            newWeek = week + 1
        } else {newDay = day +1}

        db.update("Plan", "status" to "next")
            .whereArgs("(week = {weekN}) and (day = {dayN})", "weekN" to newWeek,"dayN" to newDay)
            .exec()
    }

    fun addAccomplishedWorkout(dayWorkoutComplete : DayWorkout) {
        val db = this.writableDatabase
        db.insert("Plan",
            "planId" to dayWorkoutComplete.planId,
            "startDate" to dayWorkoutComplete.startDate,
            "week" to dayWorkoutComplete.week,
            "day" to dayWorkoutComplete.day,
            "status" to dayWorkoutComplete.status,
            "setOne" to dayWorkoutComplete.workoutSets.setOne,
            "setTwo" to dayWorkoutComplete.workoutSets.setTwo,
            "setThree" to dayWorkoutComplete.workoutSets.setThree,
            "setFour" to dayWorkoutComplete.workoutSets.setFour,
            "setFive" to dayWorkoutComplete.workoutSets.setFive
        )
    }

    fun updatePlan(numIntermediatePullups: Int) {
        // get today's date
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val currentDate = current.format(formatter)

        // initialize DayWorkout class
        val dayWorkout = DayWorkout()

        // open DB
        val db = this.writableDatabase

        // function to update plan
        fun updateWorkout (dayWorkout: DayWorkout) {
            db.update("Plan",
                "startDate" to currentDate,
                    "status" to dayWorkout.status,
                    "setOne" to dayWorkout.workoutSets.setOne,
                    "setTwo" to dayWorkout.workoutSets.setTwo,
                    "setThree" to dayWorkout.workoutSets.setThree,
                    "setFour" to dayWorkout.workoutSets.setFour,
                    "setFive" to dayWorkout.workoutSets.setFive
            ).whereArgs("(week = {weekN}) and (day = {dayN})", "weekN" to dayWorkout.week,"dayN" to dayWorkout.day).exec()
        }

        when (numIntermediatePullups) {
            /// BASIC PLAN
            in 9..11 -> {
                for (week in 4..6) {
                    dayWorkout.planId = "bas_int_${numIntermediatePullups}_${currentDate}"
                    dayWorkout.startDate = currentDate
                    dayWorkout.totNumWeeks = 6
                    dayWorkout.status = "plan"


                    for (day in 1..3) {
                        val newDayWorkout = getBasicPlan(dayWorkout, week, day)
                        if (week == 4 && day == 1) { newDayWorkout.status = "next" }
                        updateWorkout(newDayWorkout)
                    }
                }
            }

            /// INTERMEDIATE PLAN
            in 12..14 -> {
                for (week in 4..6) {
                    dayWorkout.planId = "int_int_${numIntermediatePullups}_${currentDate}"
                    dayWorkout.startDate = currentDate
                    dayWorkout.totNumWeeks = 6
                    dayWorkout.status = "plan"

                    for (day in 1..3) {
                        val newDayWorkout = getIntermediatePlan(dayWorkout, week, day)
                        if (week == 4 && day == 1) {
                            newDayWorkout.status = "next"
                        }
                        updateWorkout(newDayWorkout)
                    }
                }
            }

            /// ADVANCED PLAN
            in 14..21 -> {
                for (week in 4..6) {
                    dayWorkout.planId = "adv_int_${numIntermediatePullups}_${currentDate}"
                    dayWorkout.startDate = currentDate
                    dayWorkout.totNumWeeks = 6
                    dayWorkout.status = "plan"

                    for (day in 1..3) {
                        val newDayWorkout = getAdvancedPlan(dayWorkout, week, day)
                        if (week == 4 && day == 1) {
                            newDayWorkout.status = "next"
                        }
                        updateWorkout(newDayWorkout)
                    }
                }
            }
        }
    }

    fun restartWeeks(completedWeek : Int, repeatWeeksQt : Int) {
        // open DB
        val db = this.writableDatabase

        // fun to delete accomplished
        fun deleteAccompAndUpdatePlan (week: Int) {
            db.delete("Plan",
                "(week = {weekN}) and (status = {statusN})",
                "weekN" to week, "statusN" to "accomp"
            )

            // update plans to repeat to from planDone to plan
            db.update("Plan", "status" to "plan")
                .whereArgs("(week = {weekN}) and (status = {statusN})", "weekN" to week,"statusN" to "planDone")
                .exec()
        }

        for (i in (completedWeek - repeatWeeksQt + 1)..completedWeek) {
            deleteAccompAndUpdatePlan(i)
        }


    }
}

