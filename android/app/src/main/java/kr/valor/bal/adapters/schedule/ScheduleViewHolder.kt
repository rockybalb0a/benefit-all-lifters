package kr.valor.bal.adapters.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import kr.valor.bal.adapters.*
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.databinding.ScheduleCardviewItemBinding
import kr.valor.bal.databinding.ScheduleFooterItemBinding

open class ScheduleViewHolder(binding: ViewDataBinding): ViewHolder(binding)

class ItemViewHolder private constructor(
    private val binding: ScheduleCardviewItemBinding
): ScheduleViewHolder(binding) {

    fun bind(
        workoutDetail: WorkoutDetailAndSets,
        listeners: List<RecyclerviewItemClickListener<*>>
    ) {

        with(binding) {
            refresh(View.VISIBLE)
            item = workoutDetail
            listeners.forEach { clickListener ->
                when (clickListener) {
                    is AddWorkoutSetListener -> addSetListener = clickListener
                    is RemoveWorkoutSetListener -> deleteSetListener = clickListener
                    is DropWorkoutListener -> closeListener = clickListener
                    is UpdateWorkoutSetListener -> modifyListener = clickListener
                }
            }
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
        fun from(parent: ViewGroup): ScheduleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ScheduleCardviewItemBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }
}

class FooterViewHolder private constructor(
    private val binding: ScheduleFooterItemBinding
): ScheduleViewHolder(binding) {

    fun bind(finishedClickListener: RecyclerviewItemClickListener<*>) {
        binding.clickListener = finishedClickListener as CompleteWorkoutScheduleListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ScheduleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ScheduleFooterItemBinding.inflate(layoutInflater, parent, false)
            return FooterViewHolder(binding)
        }
    }
}

