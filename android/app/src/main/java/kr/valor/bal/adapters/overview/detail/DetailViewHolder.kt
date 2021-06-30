package kr.valor.bal.adapters.overview.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.R
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.DetailCardviewItemBinding

class DetailViewHolder private constructor(private val binding: DetailCardviewItemBinding): ViewHolder(binding) {

    fun bind(workoutDetail: WorkoutDetailAndSets, viewPool: RecyclerView.RecycledViewPool) {
        with(binding) {
            item = workoutDetail
            bindChild(viewPool, workoutDetail.workoutSets)
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
            addItemDecoration(ItemOffsetDecoration(context, R.dimen.padding_default))
        }
        childAdapter.submitList(item)

    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DetailCardviewItemBinding.inflate(layoutInflater, parent, false)
            return DetailViewHolder(binding)
        }
    }
}