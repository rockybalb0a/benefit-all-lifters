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

abstract class ScheduleViewHolder(binding: ViewDataBinding): ViewHolder(binding)

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
                    is AddWorkoutSetListener -> addListener = clickListener
                    is RemoveWorkoutSetListener -> removeListener = clickListener
                    is DropWorkoutListener -> dropListener = clickListener
                    is UpdateWorkoutSetListener -> updateListener = clickListener
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

    companion object: ViewHolderFactory {
        override fun create(parent: ViewGroup): ScheduleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ScheduleCardviewItemBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }

    override fun <T> bind(item: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int?) {
        TODO("Not yet implemented")
    }
}

class FooterViewHolder private constructor(
    private val binding: ScheduleFooterItemBinding
): ScheduleViewHolder(binding) {

    fun bind(finishedClickListener: RecyclerviewItemClickListener<*>) {
        binding.clickListener = finishedClickListener as CompleteWorkoutScheduleListener
        binding.executePendingBindings()
    }

    override fun <T> bind(item: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int?) {
        TODO("Not yet implemented")
    }

    companion object: ViewHolderFactory {
        override fun create(parent: ViewGroup): ScheduleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ScheduleFooterItemBinding.inflate(layoutInflater, parent, false)
            return FooterViewHolder(binding)
        }
    }
}

