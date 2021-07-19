package kr.valor.bal.adapters.schedule

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.UpdateWorkoutSetListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.SetInfoItemBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator

class ScheduleChildViewHolder(private val binding: SetInfoItemBinding): ViewHolder(binding) {

    fun bind(data: WorkoutSet, listener: RecyclerviewItemClickListener<WorkoutSet>, itemPosition: Int) {
        with(binding) {
            item  = data
            index = itemPosition
            clickListener = listener as UpdateWorkoutSetListener
            bindingCreator = WorkoutDetailInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<SetInfoItemBinding>(parent, R.layout.set_info_item)
            return ScheduleChildViewHolder(binding)
        }
    }
}