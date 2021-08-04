package kr.valor.bal.adapters.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.WorkoutDetailItem

class DetailAdapter(private val listener: RecyclerviewItemClickListener<Unit>?): ListAdapter<WorkoutDetailItem, ViewHolder>(DIFF_CALLBACK) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.create(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.create(parent)
            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.create(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder) {
            is HeaderViewHolder -> {
                val item = getItem(position) as WorkoutDetailItem.Header
                holder.bind(item.workoutOverview)
            }
            is ItemViewHolder -> {
                val item = getItem(position) as WorkoutDetailItem.Item
                holder.bind(item.workoutDetailAndSets, viewPool = viewPool)
            }
            is FooterViewHolder -> {
                holder.bind(listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is WorkoutDetailItem.Footer -> ITEM_VIEW_TYPE_FOOTER
            is WorkoutDetailItem.Item -> ITEM_VIEW_TYPE_ITEM
            is WorkoutDetailItem.Header -> ITEM_VIEW_TYPE_HEADER
        }
    }

    companion object {

        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
        private const val ITEM_VIEW_TYPE_FOOTER = 2

        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<WorkoutDetailItem>() {
            override fun areItemsTheSame(
                oldItem: WorkoutDetailItem,
                newItem: WorkoutDetailItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: WorkoutDetailItem,
                newItem: WorkoutDetailItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}