package kr.valor.bal.adapters

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.SetInfoItemGridBinding
import kotlin.math.round

class WorkoutDetailChildAdapter: ListAdapter<WorkoutSet, WorkoutDetailChildAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    class ViewHolder(private val binding: SetInfoItemGridBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(workoutSet: WorkoutSet, position: Int) {
            binding.item = workoutSet
            binding.index = position
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SetInfoItemGridBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutSet>() {
            override fun areItemsTheSame(oldItem: WorkoutSet, newItem: WorkoutSet): Boolean {
                return oldItem.setId == newItem.setId
            }

            override fun areContentsTheSame(oldItem: WorkoutSet, newItem: WorkoutSet): Boolean {
                return oldItem == newItem
            }

        }

    }

}

class ItemOffsetDecoration(context : Context, itemOffsetId : Int) : RecyclerView.ItemDecoration() {
    private val itemOffset = context.resources.getDimensionPixelSize(itemOffsetId)
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(itemOffset, 0, itemOffset, itemOffset)
    }
}