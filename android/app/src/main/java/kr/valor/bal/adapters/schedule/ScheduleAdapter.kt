package kr.valor.bal.adapters.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.valor.bal.adapters.CompleteWorkoutScheduleListener
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.WorkoutDetailItem

class ScheduleAdapter(
    val listeners: List<RecyclerviewItemClickListener<*>>
): ListAdapter<WorkoutDetailItem, ScheduleViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.create(parent)
            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.create(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {

        val footerViewListener =
            listeners.filterIsInstance<CompleteWorkoutScheduleListener>()[0]

        val itemViewListener =
            listeners.filter { it !is CompleteWorkoutScheduleListener }

        when(holder) {
            is ItemViewHolder -> {
                val item = getItem(position) as WorkoutDetailItem.Item
                holder.bind(item.workoutDetailAndSets, itemViewListener)
            }
            is FooterViewHolder -> {
                holder.bind(footerViewListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is WorkoutDetailItem.Footer -> ITEM_VIEW_TYPE_FOOTER
            is WorkoutDetailItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    companion object {

        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
        private const val ITEM_VIEW_TYPE_FOOTER = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutDetailItem>() {
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