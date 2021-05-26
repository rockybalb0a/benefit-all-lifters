package kr.valor.bal.adapters

import kr.valor.bal.data.WorkoutDetailAndSets

class ScheduleItemListener(val clickListener: (WorkoutDetailAndSets) -> Unit) {
    fun onClick(item: WorkoutDetailAndSets) = clickListener(item)
}