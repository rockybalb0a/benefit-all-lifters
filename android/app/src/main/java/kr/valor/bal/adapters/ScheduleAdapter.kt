package kr.valor.bal.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.listeners.ScheduleButtonListener
import kr.valor.bal.adapters.listeners.ScheduleFinishListener
import kr.valor.bal.adapters.listeners.ScheduleSetListener
import kr.valor.bal.adapters.listeners.WorkoutDetailItem
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.databinding.ScheduleCardviewItemBinding
import kr.valor.bal.databinding.ScheduleFooterItemBinding


class ScheduleAdapter(
    val addClickListener: ScheduleButtonListener,
    val deleteClickListener: ScheduleButtonListener,
    val closeClickListener: ScheduleButtonListener,
    val setClickListener: ScheduleSetListener,
    val finishedClickListener: ScheduleFinishListener
): ListAdapter<WorkoutDetailItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ItemViewHolder -> {
                val item = getItem(position) as WorkoutDetailItem.WorkoutDetailAndSetsItem
                holder.bind(item.workoutDetailAndSets, addClickListener, deleteClickListener, closeClickListener, setClickListener)
            }
            is FooterViewHolder -> {
                holder.bind(finishedClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is WorkoutDetailItem.Footer -> ITEM_VIEW_TYPE_FOOTER
            is WorkoutDetailItem.WorkoutDetailAndSetsItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ItemViewHolder private constructor(
        private val binding: ScheduleCardviewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            workoutDetail: WorkoutDetailAndSets,
            addClickListener: ScheduleButtonListener,
            deleteClickListener: ScheduleButtonListener,
            closeClickListener: ScheduleButtonListener,
            setClickListener: ScheduleSetListener) {

            with(binding) {
                refresh(View.VISIBLE)
                item = workoutDetail
                addSetListener = addClickListener
                deleteSetListener = deleteClickListener
                closeListener = closeClickListener
                setListener = setClickListener
                setsDetail.removeAllViews()
                if (workoutDetail.workoutSets.isNotEmpty()) {
                    refresh(View.GONE)
                }
                executePendingBindings()
            }
        }


        @SuppressLint("SwitchIntDef")
        private fun ScheduleCardviewItemBinding.refresh(visibility: Int) {
            emptyAddSetButton.visibility = visibility
            when(emptyAddSetButton.visibility) {
                View.VISIBLE -> {
                    existAddSetButton.visibility = View.GONE
                    existDeleteSetButton.visibility = View.GONE
                }
                View.GONE -> {
                    existDeleteSetButton.visibility = View.VISIBLE
                    existAddSetButton.visibility = View.VISIBLE
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ScheduleCardviewItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    class FooterViewHolder private constructor(
        private val binding: ScheduleFooterItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(finishedClickListener: ScheduleFinishListener) {
            binding.clickListener = finishedClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ScheduleFooterItemBinding.inflate(layoutInflater, parent, false)
                return FooterViewHolder(binding)
            }
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