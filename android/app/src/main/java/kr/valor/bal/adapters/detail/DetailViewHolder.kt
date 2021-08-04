package kr.valor.bal.adapters.detail

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.R
import kr.valor.bal.adapters.EditWorkoutScheduleListener
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.local.WorkoutDetailAndSets
import kr.valor.bal.data.local.entities.WorkoutOverview
import kr.valor.bal.data.local.entities.WorkoutSet
import kr.valor.bal.databinding.ItemDetailWorkoutDetailWithSetsBinding
import kr.valor.bal.databinding.ItemDoneFooterBinding
import kr.valor.bal.databinding.ItemDoneHeaderBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator

sealed class DetailViewHolder(binding: ViewDataBinding): ViewHolder(binding)

class ItemViewHolder private constructor(private val binding: ItemDetailWorkoutDetailWithSetsBinding): DetailViewHolder(binding) {

    fun bind(data: WorkoutDetailAndSets, viewPool: RecyclerView.RecycledViewPool) {
        with(binding) {
            item = data
            bindChild(viewPool, data.workoutSets)
            bindingCreator = WorkoutDetailInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    private fun ItemDetailWorkoutDetailWithSetsBinding.bindChild(viewPool: RecyclerView.RecycledViewPool, item: List<WorkoutSet>) {

        val childLayoutManager =
            GridLayoutManager(setsInfoRecyclerview.context, 4, GridLayoutManager.VERTICAL, false)

        val childAdapter = WorkoutDetailChildAdapter()

        setsInfoRecyclerview.apply {
            layoutManager = childLayoutManager
            adapter = childAdapter
            setRecycledViewPool(viewPool)
            setHasFixedSize(true)
            if (itemDecorationCount == 0) {
                addItemDecoration(ItemOffsetDecoration(context, R.dimen.padding_small))
            }
        }
        childAdapter.submitList(item)

    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<ItemDetailWorkoutDetailWithSetsBinding>(parent, R.layout.item_detail_workout_detail_with_sets)
            return ItemViewHolder(binding)
        }
    }
}

class HeaderViewHolder private constructor(private val binding: ItemDoneHeaderBinding): DetailViewHolder(binding) {

    fun bind(data: WorkoutOverview) {
        with(binding) {
            item = data
            bindingCreator = WorkoutSummaryInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<ItemDoneHeaderBinding>(parent, R.layout.item_done_header)
            return HeaderViewHolder(binding)
        }
    }
}


class FooterViewHolder private constructor(private val binding: ItemDoneFooterBinding): DetailViewHolder(binding) {

    fun bind(listener: RecyclerviewItemClickListener<Unit>?) {
        with(binding) {
            listener?.let {
                clickListener = listener as EditWorkoutScheduleListener
            }
            executePendingBindings()
        }
    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<ItemDoneFooterBinding>(parent, R.layout.item_done_footer)
            return FooterViewHolder(binding)
        }
    }

}