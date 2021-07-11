package kr.valor.bal.adapters.detail

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.data.entities.WorkoutSet

class WorkoutDetailChildAdapter: ListAdapter<WorkoutSet, ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DetailChildViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as DetailChildViewHolder).bind(data = item, itemPosition = position)
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