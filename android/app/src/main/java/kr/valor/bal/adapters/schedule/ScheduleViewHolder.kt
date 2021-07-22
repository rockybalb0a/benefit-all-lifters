package kr.valor.bal.adapters.schedule

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.databinding.ScheduleCardviewItemBinding
import kr.valor.bal.databinding.ScheduleFooterItemBinding
import kr.valor.bal.databinding.ScheduleHeaderItemBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator

sealed class ScheduleViewHolder(binding: ViewDataBinding): ViewHolder(binding)

class ItemViewHolder private constructor(private val binding: ScheduleCardviewItemBinding): ScheduleViewHolder(binding) {

    fun bind(data: WorkoutDetailAndSets, listeners: List<RecyclerviewItemClickListener<*>>, viewPool: RecyclerView.RecycledViewPool) {
        with(binding) {
            item = data
            if (data.workoutSets.isNotEmpty()) {
                refresh(View.GONE)
            } else {
                refresh(View.VISIBLE)
            }
            listeners.forEach { clickListener ->
                when (clickListener) {
                    is AddWorkoutSetListener -> addListener = clickListener
                    is RemoveWorkoutSetListener -> removeListener = clickListener
                    is DropWorkoutListener -> dropListener = clickListener
                }
            }

            val childListener =
                listeners.single { it is UpdateWorkoutSetListener } as UpdateWorkoutSetListener
            val childAdapter = ScheduleChildAdapter(childListener)
            setsDetail.apply {
                adapter = childAdapter
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }
            childAdapter.submitList(data.workoutSets)

            bindingCreator = WorkoutDetailInfoBindingParameterCreator
            executePendingBindings()

        }
    }

    @SuppressLint("SwitchIntDef")
    private fun ScheduleCardviewItemBinding.refresh(visibility: Int) {
        emptyAddSetButton.visibility = visibility
        when (emptyAddSetButton.visibility) {
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

    companion object : ViewHolderFactory() {
        override fun create(parent: ViewGroup): ScheduleViewHolder {
            val binding =
                inflate<ScheduleCardviewItemBinding>(parent, R.layout.schedule_cardview_item)
            return ItemViewHolder(binding)
        }
    }
}

class FooterViewHolder private constructor(private val binding: ScheduleFooterItemBinding): ScheduleViewHolder(binding) {

    fun bind(listeners: RecyclerviewItemClickListener<Unit>) {
        with(binding) {
            clickListener = listeners as CompleteWorkoutScheduleListener
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ScheduleViewHolder {
            val binding =
                inflate<ScheduleFooterItemBinding>(parent, R.layout.schedule_footer_item)
            return FooterViewHolder(binding)
        }
    }
}

class HeaderViewHolder private constructor(private val binding: ScheduleHeaderItemBinding): ScheduleViewHolder(binding) {

    fun bind(data: WorkoutOverview, listeners: RecyclerviewItemClickListener<Unit>) {
        with(binding) {
            item = data
            clickListener = listeners as ManualTimerSettingListener
            bindingCreator = WorkoutSummaryInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ScheduleViewHolder {
            val binding =
                inflate<ScheduleHeaderItemBinding>(parent, R.layout.schedule_header_item)
            return HeaderViewHolder(binding)
        }
    }
}