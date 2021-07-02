package kr.valor.bal.adapters.overview.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.data.WorkoutDetailAndSets

class DetailAdapter: ListAdapter<WorkoutDetailAndSets, ViewHolder>(
    DIFF_CALLBACK
) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DetailViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as DetailViewHolder).bind(item, viewPool)
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<WorkoutDetailAndSets>() {
            override fun areItemsTheSame(
                oldItem: WorkoutDetailAndSets,
                newItem: WorkoutDetailAndSets
            ): Boolean {
                return oldItem.workoutDetail.detailId == newItem.workoutDetail.detailId
            }

            override fun areContentsTheSame(
                oldItem: WorkoutDetailAndSets,
                newItem: WorkoutDetailAndSets
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}