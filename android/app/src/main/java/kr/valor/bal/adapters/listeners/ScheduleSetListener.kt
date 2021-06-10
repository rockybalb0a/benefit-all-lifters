package kr.valor.bal.adapters.listeners

import kr.valor.bal.data.entities.WorkoutSet

class ScheduleSetListener(val clickListener: (WorkoutSet) -> Unit) {
    fun onClick(item: WorkoutSet) = clickListener(item)
}