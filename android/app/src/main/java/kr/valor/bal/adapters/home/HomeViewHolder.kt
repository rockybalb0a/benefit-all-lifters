package kr.valor.bal.adapters.home

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ShowDetailInfoListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.WorkoutSummaryInfo
import kr.valor.bal.databinding.HomeWorkoutInfoItemBinding
import kr.valor.bal.utilities.binding.WorkoutHeaderInfoBindingParameterCreator

class HomeViewHolder(private val binding: HomeWorkoutInfoItemBinding): ViewHolder(binding) {

    fun bind(data: WorkoutSummaryInfo, listener: RecyclerviewItemClickListener<Unit>) {
        with(binding) {
            item = data
            clickListener = listener as ShowDetailInfoListener
            bindingCreator = WorkoutHeaderInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<HomeWorkoutInfoItemBinding>(parent, R.layout.home_workout_info_item)
            return HomeViewHolder(binding)
        }
    }
}