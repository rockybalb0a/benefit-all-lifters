package kr.valor.bal.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

    abstract fun <T> bind(item: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int? = null)

}

interface ViewHolderFactory {
     fun create(parent: ViewGroup): ViewHolder
}