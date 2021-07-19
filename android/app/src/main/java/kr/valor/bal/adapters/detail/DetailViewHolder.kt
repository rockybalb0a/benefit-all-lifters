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
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.DetailCardviewItemBinding
import kr.valor.bal.databinding.ScheduleDoneFooterItemBinding
import kr.valor.bal.databinding.ScheduleDoneHeaderItemBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator

sealed class DetailViewHolder(binding: ViewDataBinding): ViewHolder(binding)

class ItemViewHolder private constructor(private val binding: DetailCardviewItemBinding): DetailViewHolder(binding) {

    fun bind(data: WorkoutDetailAndSets, viewPool: RecyclerView.RecycledViewPool) {
        with(binding) {
            item = data
            bindChild(viewPool, data.workoutSets)
            bindingCreator = WorkoutDetailInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    private fun DetailCardviewItemBinding.bindChild(viewPool: RecyclerView.RecycledViewPool, item: List<WorkoutSet>) {

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
                inflate<DetailCardviewItemBinding>(parent, R.layout.detail_cardview_item)
            return ItemViewHolder(binding)
        }
    }
}

class HeaderViewHolder private constructor(private val binding: ScheduleDoneHeaderItemBinding): DetailViewHolder(binding) {

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
                inflate<ScheduleDoneHeaderItemBinding>(parent, R.layout.schedule_done_header_item)
            return HeaderViewHolder(binding)
        }
    }
}


class FooterViewHolder private constructor(private val binding: ScheduleDoneFooterItemBinding): DetailViewHolder(binding) {

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
                inflate<ScheduleDoneFooterItemBinding>(parent, R.layout.schedule_done_footer_item)
            return FooterViewHolder(binding)
        }
    }

}