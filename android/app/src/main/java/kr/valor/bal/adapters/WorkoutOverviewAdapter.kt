package kr.valor.bal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.R
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.databinding.ListItemOverviewCardviewBinding
import kr.valor.bal.utilities.elapsedTimeFormatter
import kr.valor.bal.utilities.localDateFormatter
import kotlin.random.Random

class WorkoutOverviewAdapter: ListAdapter<WorkoutOverview, WorkoutOverviewAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ListItemOverviewCardviewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(workoutOverview: WorkoutOverview) {
            binding.item = workoutOverview
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemOverviewCardviewBinding.inflate(layoutInflater, parent, false)
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