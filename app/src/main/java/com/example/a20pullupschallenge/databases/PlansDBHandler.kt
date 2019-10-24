package com.example.a20pullupschallenge.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.a20pullupschallenge.DayWorkout
import com.example.a20pullupschallenge.Sets
import org.jetbrains.anko.db.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyDatabaseOpenHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PlansDatabase", null, 1) {
    init {
        instance = this
    }

    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: MyDatabaseOpenHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
//        db.createTable("Plan", true,
//            "planId" to TEXT,
//            "startDate" to TEXT,
//            "week" to INTEGER,
//            "day" to INTEGER,
//            "status" to TEXT,
//            "setOne" to INTEGER,
//            "setTwo" to INTEGER,
//            "setThree" to INTEGER,
//            "setFour" to INTEGER,
//            "setFive" to INTEGER)

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
        var dayWorkout : DayWorkout = DayWorkout()

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
        when {

            // BEGINNERS PLANS (0 TO 3 PULLUPS)
            numInitialPullups < 3 -> { // beginners plan
                dayWorkout.planId = "beg_${numInitialPullups}_${currentDate}"
                dayWorkout.startDate = currentDate

                /// TEMPORARY NUMBER DONT FORGET TO CHANGE////////////////////////////////////////////////////////////
                dayWorkout.totNumWeeks = 2
                //////////////////////////////////////////////////////////////////////////////////////////

                dayWorkout.status = "plan"


                when (numInitialPullups) {
                    0 -> {
                        for (week in 1..dayWorkout.totNumWeeks) {
                            for (day in 1..3) {
                                dayWorkout.week = week
                                dayWorkout.day = day
                                when {
                                    week == 1 && day == 1-> {dayWorkout.workoutSets = Sets(-1,-1,-1,-1,-1)}
                                    week == 1 && day == 2-> {dayWorkout.workoutSets = Sets(-1,-1,-1,-1,-1)}
                                    week == 1 && day == 3-> {dayWorkout.workoutSets = Sets(-1,-1,-1,-1,1) }

                                    week == 2 && day == 1-> {dayWorkout.workoutSets = Sets(1,-1,-1,-1,-1)}
                                    week == 2 && day == 2-> {dayWorkout.workoutSets = Sets(1,-1,1,-1,-1)}
                                    week == 2 && day == 3-> {dayWorkout.workoutSets = Sets(1,-1,1,-1,20)}
                                }

                                addWorkoutToDB(dayWorkout)
                            }
                        }
                    }
                    1 -> {
                        for (week in 1..dayWorkout.totNumWeeks) {
                            for (day in 1..3) {
                                dayWorkout.week = week
                                dayWorkout.day = day
                                when {
                                    week == 1 && day == 1-> {dayWorkout.workoutSets = Sets(1,-1,-1,-1,-1)}
                                    week == 1 && day == 2-> {dayWorkout.workoutSets = Sets(1,1,-1,1,-1)}
                                    week == 1 && day == 3-> {dayWorkout.workoutSets = Sets(1,1,-1,-1,2)}

                                    week == 2 && day == 1-> {dayWorkout.workoutSets = Sets(1,1,-1,1,-1)}
                                    week == 2 && day == 2-> {dayWorkout.workoutSets = Sets(1,1,-1,1,-1)}
                                    week == 2 && day == 3-> {dayWorkout.workoutSets = Sets(1,-1,1,-1,20)}
                                }

                                addWorkoutToDB(dayWorkout)
                            }
                        }
                    }
                    2 -> {
                        for (week in 1..dayWorkout.totNumWeeks) {
                            for (day in 1..3) {
                                dayWorkout.week = week
                                dayWorkout.day = day
                                when {
                                    week == 1 && day == 1-> {dayWorkout.workoutSets = Sets(2,1,1,2,-1)}
                                    week == 1 && day == 2-> {dayWorkout.workoutSets = Sets(2,1,1,2,-1)}
                                    week == 1 && day == 3-> {dayWorkout.workoutSets = Sets(2,1,1,1,2)}

                                    week == 2 && day == 1-> {dayWorkout.workoutSets = Sets(2,2,1,2,-1)}
                                    week == 2 && day == 2-> {dayWorkout.workoutSets = Sets(2,2,1,2,-1)}
                                    week == 2 && day == 3-> {dayWorkout.workoutSets = Sets(2,1,1,1,20)}
                                }

                                addWorkoutToDB(dayWorkout)
                            }
                        }
                    }
                }

            }

            // BASIC PLANS (3 TO 5 PULLUPS)
            numInitialPullups in 3..5 -> { // basic plan

            }

            // BASIC PLANS (6 TO 9 PULLUPS)
            numInitialPullups in 6..9 -> { // intermediate plan

            }

            // ADVANCED PLANS (10+ PULLUPS)
            numInitialPullups >= 10 -> { // advanced plan

            }
        }

        db.close()
    }

    fun getPlan() : ArrayList<DayWorkout> {
        val plan = ArrayList<DayWorkout>()
        val db = this.readableDatabase
        db.select("Plan", "planId", "week", "day", "status", "setOne", "setTwo", "setThree", "setFour", "setFive").exec {
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

    fun dropTable() {
        val db = this.writableDatabase
        db.dropTable("Plan", true)
        createTable(db)
    }

    fun createTable(db: SQLiteDatabase) {
        db.createTable("Plan", true,
            "planId" to TEXT,
            "startDate" to TEXT,
            "week" to INTEGER,
            "day" to INTEGER,
            "status" to TEXT,
            "setOne" to INTEGER,
            "setTwo" to INTEGER,
            "setThree" to INTEGER,
            "setFour" to INTEGER,
            "setFive" to INTEGER)
    }

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
}

// Access property for Context
//val Context.database: MyDatabaseOpenHelper
//    get() = MyDatabaseOpenHelper.getInstance(this)

