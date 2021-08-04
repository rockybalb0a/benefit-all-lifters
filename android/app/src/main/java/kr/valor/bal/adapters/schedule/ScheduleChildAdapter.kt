package kr.valor.bal.adapters.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.data.local.workout.entities.WorkoutSet

class ScheduleChildAdapter(private val listener: RecyclerviewItemClickListener<WorkoutSet>): ListAdapter<WorkoutSet, ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ScheduleChildViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ScheduleChildViewHolder).bind(item, listener, position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutSet>() {
            override fun areItemsTheSame(oldItem: WorkoutSet, newItem: WorkoutSet): Boolean {
                return oldItem.setId == newItem.setId
            }

            override fun areContentsTheSame(oldItem: WorkoutSet, newItem: WorkoutSet): Boolean {
                return oldItem == newItem
            }
        }
    }
}