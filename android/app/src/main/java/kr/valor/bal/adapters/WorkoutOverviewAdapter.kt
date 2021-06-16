package kr.valor.bal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.listeners.OverviewItemListener
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.databinding.OverviewCardviewItemBinding

class WorkoutOverviewAdapter(val clickListener: OverviewItemListener): ListAdapter<WorkoutSchedule, WorkoutOverviewAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(private val binding: OverviewCardviewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(workoutSchedule: WorkoutSchedule, scheduleClickListener: OverviewItemListener) {
            binding.item = workoutSchedule
            binding.clickListener = scheduleClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    OverviewCardviewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
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