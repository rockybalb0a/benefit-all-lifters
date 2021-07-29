package kr.valor.bal.adapters.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.CompleteWorkoutScheduleListener
import kr.valor.bal.adapters.ManualTimerSettingListener
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.WorkoutDetailItem

class ScheduleAdapter(private vararg val listeners: RecyclerviewItemClickListener<*>): ListAdapter<WorkoutDetailItem, ScheduleViewHolder>(DIFF_CALLBACK) {

    private val viewPool = RecyclerView.RecycledViewPool()

    private val headerViewListener =
        try {
            listeners.single { it is ManualTimerSettingListener } as ManualTimerSettingListener
        } catch (e: NoSuchElementException) {
            null
        }

    private val footerViewListener =
        listeners.single { it is CompleteWorkoutScheduleListener } as CompleteWorkoutScheduleListener

    private val itemViewListeners =
        listeners.filter { it !is CompleteWorkoutScheduleListener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.create(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.create(parent)
            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.create(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {

        when(holder) {
            is ItemViewHolder -> {
                val item = getItem(position) as WorkoutDetailItem.Item
                holder.bind(item.workoutDetailAndSets, itemViewListeners, viewPool)
            }
            is FooterViewHolder -> {
                holder.bind(footerViewListener)
            }
            is HeaderViewHolder -> {
                val item = getItem(position) as WorkoutDetailItem.Header
                holder.bind(item.workoutOverview, headerViewListener!!)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is WorkoutDetailItem.Header -> ITEM_VIEW_TYPE_HEADER
            is WorkoutDetailItem.Item -> ITEM_VIEW_TYPE_ITEM
            is WorkoutDetailItem.Footer -> ITEM_VIEW_TYPE_FOOTER
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