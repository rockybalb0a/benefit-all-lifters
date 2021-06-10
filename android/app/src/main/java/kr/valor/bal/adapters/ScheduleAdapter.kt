package kr.valor.bal.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.valor.bal.adapters.listeners.ScheduleButtonListener
import kr.valor.bal.adapters.listeners.ScheduleSetListener
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.databinding.ListItemScheduleCardviewBinding


class ScheduleAdapter(
    val addClickListener: ScheduleButtonListener,
    val deleteClickListener: ScheduleButtonListener,
    val closeClickListener: ScheduleButtonListener,
    val setClickListener: ScheduleSetListener
): ListAdapter<WorkoutDetailAndSets, ScheduleAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, addClickListener, deleteClickListener, closeClickListener, setClickListener)
    }

    class ViewHolder private constructor(
        private val binding: ListItemScheduleCardviewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(
            workoutDetail: WorkoutDetailAndSets,
            addClickListener: ScheduleButtonListener,
            deleteClickListener: ScheduleButtonListener,
            closeClickListener: ScheduleButtonListener,
            setClickListener: ScheduleSetListener) {

            with(binding) {
                refresh(View.VISIBLE)
                item = workoutDetail
                addSetListener = addClickListener
                deleteSetListener = deleteClickListener
                closeListener = closeClickListener
                setListener = setClickListener
                setsDetail.removeAllViews()
                if (workoutDetail.workoutSets.isNotEmpty()) {
                    refresh(View.GONE)
                }
                executePendingBindings()
            }
        }


        @SuppressLint("SwitchIntDef")
        private fun ListItemScheduleCardviewBinding.refresh(visibility: Int) {
            emptyAddSetButton.visibility = visibility
            when(emptyAddSetButton.visibility) {
                View.VISIBLE -> {
                    existAddSetButton.visibility = View.GONE
                    existDeleteSetButton.visibility = View.GONE
                }
                View.GONE -> {
                    existDeleteSetButton.visibility = View.VISIBLE
                    existAddSetButton.visibility = View.VISIBLE
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemScheduleCardviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutDetailAndSets>() {
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