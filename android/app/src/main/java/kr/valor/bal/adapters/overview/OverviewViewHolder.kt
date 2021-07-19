package kr.valor.bal.adapters.overview

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.databinding.OverviewCardviewItemBinding
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator

class OverviewViewHolder private constructor(
    private  val binding: OverviewCardviewItemBinding
): ViewHolder(binding) {

    fun bind(data: WorkoutSchedule, listeners: RecyclerviewItemClickListener<WorkoutSchedule>) {
        with(binding) {
            item = data
            clickListener = listeners as OverviewItemListener
            bindingCreator = WorkoutSummaryInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<OverviewCardviewItemBinding>(parent, R.layout.overview_cardview_item)
            return OverviewViewHolder(binding)
        }
    }

}