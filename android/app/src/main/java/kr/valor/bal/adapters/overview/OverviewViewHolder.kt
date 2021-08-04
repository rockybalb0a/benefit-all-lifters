package kr.valor.bal.adapters.overview

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.data.local.workout.WorkoutSchedule
import kr.valor.bal.databinding.ItemOverviewWorkoutScheduleBinding
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator

class OverviewViewHolder private constructor(
    private  val binding: ItemOverviewWorkoutScheduleBinding
): ViewHolder(binding) {

    fun bind(data: WorkoutSchedule, listeners: RecyclerviewItemClickListener<WorkoutSchedule>) {
        with(binding) {
            item = data
            clickListener = listeners as OverviewItemListener
            bindingCreator = WorkoutSummaryInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<ItemOverviewWorkoutScheduleBinding>(parent, R.layout.item_overview_workout_schedule)
            return OverviewViewHolder(binding)
        }
    }

}