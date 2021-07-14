package kr.valor.bal.adapters.schedule

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
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
        binding.item = data as WorkoutDetailAndSets
    }

    fun bind(
        data: WorkoutDetailAndSets,
        listeners: Array<out RecyclerviewItemClickListener<*>>,
        viewPool: RecyclerView.RecycledViewPool
    ) {
        bind(data, *listeners)
        with(binding) {
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

            val childAdapter = ScheduleChildAdapter(*listeners)
            setsDetail.apply {
                adapter = childAdapter
                setRecycledViewPool(viewPool)
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