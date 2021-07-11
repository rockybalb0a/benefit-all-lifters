package kr.valor.bal.adapters.overview.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.R
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.DetailCardviewItemBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator

class DetailViewHolder private constructor(private val binding: DetailCardviewItemBinding): ViewHolder(binding) {

    fun bind(workoutDetail: WorkoutDetailAndSets, viewPool: RecyclerView.RecycledViewPool) {
        bind(data = workoutDetail)
        with(binding) {
            bindChild(viewPool, workoutDetail.workoutSets)
            bindingCreator = WorkoutDetailInfoBindingParameterCreator
            executePendingBindings()
        }
    }

    override fun <T> bind(data: T, vararg listeners: RecyclerviewItemClickListener<*>, itemPosition: Int?) {
        binding.item = data as WorkoutDetailAndSets
    }

    private fun DetailCardviewItemBinding.bindChild(viewPool: RecyclerView.RecycledViewPool, item: List<WorkoutSet>) {

        val childLayoutManager =
            GridLayoutManager(setsInfoRecyclerview.context, 4, GridLayoutManager.VERTICAL, false)

        val childAdapter = WorkoutDetailChildAdapter()

        setsInfoRecyclerview.apply {
            layoutManager = childLayoutManager
            adapter = childAdapter
            setRecycledViewPool(viewPool)
            addItemDecoration(ItemOffsetDecoration(context, R.dimen.padding_default))
        }
        childAdapter.submitList(item)

    }

    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<DetailCardviewItemBinding>(parent, R.layout.detail_cardview_item)
            return DetailViewHolder(binding)
        }
    }
}