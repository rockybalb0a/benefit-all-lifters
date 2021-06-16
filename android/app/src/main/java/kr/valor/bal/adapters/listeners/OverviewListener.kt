package kr.valor.bal.adapters.listeners

import kr.valor.bal.data.entities.WorkoutOverview

class OverviewListener(val clickListener: (WorkoutOverview) -> Unit) {
    fun onClick(item: WorkoutOverview) = clickListener(item)
}