package kr.valor.bal.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

    abstract fun <T> bind(data: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int? = null)

}