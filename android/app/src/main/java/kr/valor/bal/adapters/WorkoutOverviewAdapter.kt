package kr.valor.bal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.listeners.OverviewListener
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.databinding.OverviewCardviewItemBinding

class WorkoutOverviewAdapter(val clickListener: OverviewListener): ListAdapter<WorkoutOverview, WorkoutOverviewAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(private val binding: OverviewCardviewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(workoutOverview: WorkoutOverview, clickListener: OverviewListener) {
            binding.item = workoutOverview
            binding.clickListener = clickListener
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutOverview>() {
            override fun areItemsTheSame(
                oldItem: WorkoutOverview,
                newItem: WorkoutOverview
            ): Boolean {
                return oldItem.overviewId == newItem.overviewId
            }

            override fun areContentsTheSame(
                oldItem: WorkoutOverview,
                newItem: WorkoutOverview
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}