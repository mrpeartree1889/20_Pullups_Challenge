package com.example.a20pullupschallenge

class Sets (var setOne : Int = 0,
            var setTwo : Int = 0,
            var setThree : Int = 0,
            var setFour : Int = 0,
            var setFive : Int = 0)

class DayWorkout {
    var planId : String = ""
    var startDate : String = ""
    var totNumWeeks : Int = 0
    var week : Int = 0
    var day : Int = 0
    var status : String = "" // either plan for PLANNED, next for NEXT, comp for COMPLETED or accomp for ACCOMPLISHED
    var workoutSets: Sets = Sets()
}