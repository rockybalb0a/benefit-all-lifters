package kr.valor.bal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class ViewHolderFactory {

    abstract fun create(parent: ViewGroup): ViewHolder

    protected fun <T : ViewDataBinding> inflate(parent: ViewGroup, layoutId: Int): T =
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)

}