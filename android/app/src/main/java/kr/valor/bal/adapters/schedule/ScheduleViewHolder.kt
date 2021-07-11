package kr.valor.bal.adapters.schedule

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.databinding.ScheduleCardviewItemBinding
import kr.valor.bal.databinding.ScheduleFooterItemBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator

sealed class ScheduleViewHolder(binding: ViewDataBinding): ViewHolder(binding)

class ItemViewHolder private constructor(private val binding: ScheduleCardviewItemBinding): ScheduleViewHolder(binding) {
    override fun <T> bind(
        data: T,
        vararg listeners: RecyclerviewItemClickListener<*>,
        itemPosition: Int?
    ) {
        data as WorkoutDetailAndSets
        bindingData(data, listeners)
    }

    private fun bindingData(
        data: WorkoutDetailAndSets,
        listeners: Array<out RecyclerviewItemClickListener<*>>
    ) {
        with(binding) {
            refresh(View.VISIBLE)
            item = data
            listeners.forEach { clickListener ->
                when (clickListener) {
                    is AddWorkoutSetListener -> addListener = clickListener
                    is RemoveWorkoutSetListener -> removeListener = clickListener
                    is DropWorkoutListener -> dropListener = clickListener
                    is UpdateWorkoutSetListener -> updateListener = clickListener
                }
            }
            setsDetail.removeAllViews()
            if (data.workoutSets.isNotEmpty()) {
                refresh(View.GONE)
            }
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

    override fun <T> bind(data: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int?) {
        with(binding) {
            clickListener = listeners.single() as CompleteWorkoutScheduleListener
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