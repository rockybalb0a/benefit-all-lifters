package kr.valor.bal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.R
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.DetailCardviewItemBinding

class WorkoutDetailAdapter: ListAdapter<WorkoutDetailAndSets, WorkoutDetailAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewPool)
    }

    class ViewHolder(private val binding: DetailCardviewItemBinding): RecyclerView.ViewHolder(binding.root) {
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
                return ViewHolder(binding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<WorkoutDetailAndSets>() {
            override fun areItemsTheSame(
                oldItem: WorkoutDetailAndSets,
                newItem: WorkoutDetailAndSets
            ): Boolean {
                return oldItem.workoutDetail.detailId == newItem.workoutDetail.detailId
            }

            override fun areContentsTheSame(
                oldItem: WorkoutDetailAndSets,
                newItem: WorkoutDetailAndSets
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}