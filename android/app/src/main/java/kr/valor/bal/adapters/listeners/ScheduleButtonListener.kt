package kr.valor.bal.adapters.listeners

import kr.valor.bal.data.WorkoutDetailAndSets

class ScheduleButtonListener (val clickListener: (WorkoutDetailAndSets) -> Unit) {
    fun onClick(item: WorkoutDetailAndSets) = clickListener(item)
}