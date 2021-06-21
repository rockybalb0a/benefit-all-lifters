package kr.valor.bal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.SetInfoItemGridBinding

class WorkoutDetailChildAdapter: ListAdapter<WorkoutSet, WorkoutDetailChildAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: SetInfoItemGridBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(workoutSet: WorkoutSet) {
            binding.item = workoutSet
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SetInfoItemGridBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
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