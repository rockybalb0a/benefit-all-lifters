package kr.valor.bal.adapters.listeners

import kr.valor.bal.data.WorkoutSchedule

class OverviewItemListener(val clickListener: (WorkoutSchedule) -> Unit) {
    fun onClick(item: WorkoutSchedule) = clickListener(item)
}