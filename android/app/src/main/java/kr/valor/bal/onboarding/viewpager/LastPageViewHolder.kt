package kr.valor.bal.onboarding.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kr.valor.bal.data.local.user.UserPersonalRecording
import kr.valor.bal.databinding.ItemOnBoardingUserPrBinding
import kr.valor.bal.databinding.ItemOnBoardingUserPrFooterBinding
import kr.valor.bal.utilities.binding.UserPrBindingParameterCreator

sealed class LastPageViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root)

class ContentViewHolder private constructor(private val binding: ItemOnBoardingUserPrBinding): LastPageViewHolder(binding) {
    fun bind(data: UserPersonalRecording) {
        with(binding) {
            item = data
            bindingCreator = UserPrBindingParameterCreator
            executePendingBindings()
        }
    }
    companion object  {
        fun create(parent: ViewGroup): LastPageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemOnBoardingUserPrBinding.inflate(layoutInflater, parent, false)
            return ContentViewHolder(binding)
        }
    }
}

class FooterViewHolder private constructor(binding: ItemOnBoardingUserPrFooterBinding): LastPageViewHolder(binding) {

    companion object {
        fun create(parent: ViewGroup): LastPageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemOnBoardingUserPrFooterBinding.inflate(layoutInflater, parent, false)
            return FooterViewHolder(binding)
        }
    }
}