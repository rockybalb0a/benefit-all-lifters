package kr.valor.bal.adapters.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.OverviewItemListener
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.databinding.OverviewCardviewItemBinding

class OverviewViewHolder private constructor(
    private  val binding: OverviewCardviewItemBinding
): ViewHolder(binding) {

    override fun <T> bind(data: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int?) {
        with(binding) {
            item = data as WorkoutSchedule
            clickListener = listeners.single() as OverviewItemListener
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory {
        override fun create(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                OverviewCardviewItemBinding.inflate(layoutInflater, parent, false)
            return OverviewViewHolder(binding)
        }
    }

}