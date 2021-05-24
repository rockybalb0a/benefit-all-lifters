package kr.valor.bal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.databinding.ListItemScheduleCardviewBinding


class ScheduleAdapter: ListAdapter<WorkoutDetail, ScheduleAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ListItemScheduleCardviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(workoutDetail: WorkoutDetail) {
            binding.item = workoutDetail
            // TODO :  click listener setup
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemScheduleCardviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutDetail>() {
            override fun areItemsTheSame(oldItem: WorkoutDetail, newItem: WorkoutDetail): Boolean {
                return oldItem.detailId == newItem.detailId
            }

            override fun areContentsTheSame(
                oldItem: WorkoutDetail,
                newItem: WorkoutDetail
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}