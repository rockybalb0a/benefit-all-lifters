package kr.valor.bal.adapters.listeners

import kr.valor.bal.data.WorkoutDetailAndSets

class ScheduleButtonListener (val clickListener: (WorkoutDetailAndSets) -> Unit) {
    fun onClick(item: WorkoutDetailAndSets) = clickListener(item)
}

sealed class WorkoutDetailItem {

    abstract val id: Long

    data class WorkoutDetailAndSetsItem(val workoutDetailAndSets: WorkoutDetailAndSets): WorkoutDetailItem() {
        override val id = workoutDetailAndSets.workoutDetail.detailId
    }

    object Footer: WorkoutDetailItem() {
        override val id: Long = Long.MIN_VALUE
    }
}