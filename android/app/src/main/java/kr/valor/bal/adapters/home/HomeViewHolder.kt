package kr.valor.bal.adapters.home

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ShowDetailInfoListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.local.UserPersonalRecording
import kr.valor.bal.databinding.ItemHomeUserPrBinding
import kr.valor.bal.utilities.binding.UserPrBindingParameterCreator

class HomeViewHolder(private val binding: ItemHomeUserPrBinding): ViewHolder(binding) {

    fun bind(data: UserPersonalRecording, listener: RecyclerviewItemClickListener<Unit>) {
        with(binding) {
            item = data
            clickListener = listener as ShowDetailInfoListener
            bindingCreator = UserPrBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<ItemHomeUserPrBinding>(parent, R.layout.item_home_user_pr)
            return HomeViewHolder(binding)
        }
    }
}