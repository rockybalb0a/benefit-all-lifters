package kr.valor.bal.adapters.detail

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.SetInfoItemGridBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator

class DetailChildViewHolder private constructor (private val binding: SetInfoItemGridBinding): ViewHolder(binding) {

    override fun <T> bind(data: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int?) {
        data as WorkoutSet
        with(binding) {
            item = data
            index = itemPosition!!
            bindingCreator = WorkoutDetailInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<SetInfoItemGridBinding>(parent, R.layout.set_info_item_grid)
            return DetailChildViewHolder(binding)
        }
    }

}