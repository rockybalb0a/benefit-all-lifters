package kr.valor.bal.adapters.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.data.UserPersonalRecording

class HomeAdapter(private val clickListener: RecyclerviewItemClickListener<Unit>): ListAdapter<UserPersonalRecording, ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return HomeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        (holder as HomeViewHolder).bind(data, clickListener)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserPersonalRecording>() {
            override fun areItemsTheSame(
                oldItem: UserPersonalRecording,
                newItem: UserPersonalRecording
            ): Boolean {
                return oldItem.workoutName == newItem.workoutName
            }

            override fun areContentsTheSame(
                oldItem: UserPersonalRecording,
                newItem: UserPersonalRecording
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}