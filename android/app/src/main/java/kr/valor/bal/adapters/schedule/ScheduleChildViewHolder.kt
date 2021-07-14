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
    override fun <T> bind(
        data: T,
        vararg listeners: RecyclerviewItemClickListener<*>,
        itemPosition: Int?
    ) {
        data as WorkoutSet
        with(binding) {
            item = data
            index = itemPosition!!
            clickListener = listeners.single { it is UpdateWorkoutSetListener } as UpdateWorkoutSetListener
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