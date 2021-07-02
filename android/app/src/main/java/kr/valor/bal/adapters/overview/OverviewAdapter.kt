package kr.valor.bal.adapters.overview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.OverviewItemListener
import kr.valor.bal.data.WorkoutSchedule

class OverviewAdapter(val clickListener: OverviewItemListener): ListAdapter<WorkoutSchedule, ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return OverviewViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as OverviewViewHolder).bind(item, clickListener)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutSchedule>() {
            override fun areItemsTheSame(
                oldItem: WorkoutSchedule,
                newItem: WorkoutSchedule
            ): Boolean {
                return oldItem.workoutOverview.overviewId == newItem.workoutOverview.overviewId
            }

            override fun areContentsTheSame(
                oldItem: WorkoutSchedule,
                newItem: WorkoutSchedule
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}