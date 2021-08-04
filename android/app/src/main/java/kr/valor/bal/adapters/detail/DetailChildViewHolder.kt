package kr.valor.bal.adapters.detail

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.local.workout.entities.WorkoutSet
import kr.valor.bal.databinding.ItemDetailWorkoutSetGridBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator

class DetailChildViewHolder private constructor (private val binding: ItemDetailWorkoutSetGridBinding): ViewHolder(binding) {

    fun bind(data: WorkoutSet, itemPosition: Int) {
        with(binding) {
            item = data
            index = itemPosition
            bindingCreator = WorkoutDetailInfoBindingParameterCreator
            executePendingBindings()
        }
    }


    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<ItemDetailWorkoutSetGridBinding>(parent, R.layout.item_detail_workout_set_grid)
            return DetailChildViewHolder(binding)
        }
    }

}