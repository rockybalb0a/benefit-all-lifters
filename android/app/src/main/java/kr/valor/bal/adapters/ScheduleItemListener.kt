package kr.valor.bal.adapters

import kr.valor.bal.data.entities.WorkoutDetail

class ScheduleItemListener(val clickListener: (WorkoutDetail) -> Unit) {
    fun onClick(workoutDetail: WorkoutDetail) = clickListener(workoutDetail)
}