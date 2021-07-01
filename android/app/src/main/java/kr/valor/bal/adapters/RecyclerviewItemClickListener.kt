package kr.valor.bal.adapters

import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutSet

interface RecyclerviewItemClickListener<T> {
    fun onClick(item: T)
}


class OverviewItemListener(val clickListener: (WorkoutSchedule) -> Unit):
    RecyclerviewItemClickListener<WorkoutSchedule> {

    override fun onClick(item: WorkoutSchedule) = clickListener(item)

}


class AddWorkoutSetListener (val clickListener: (WorkoutDetailAndSets) -> Unit):
    RecyclerviewItemClickListener<WorkoutDetailAndSets> {

    override fun onClick(item: WorkoutDetailAndSets) = clickListener(item)

}


class UpdateWorkoutSetListener (val clickListener: (WorkoutSet) -> Unit):
    RecyclerviewItemClickListener<WorkoutSet> {

    override fun onClick(item: WorkoutSet) = clickListener(item)

}


class RemoveWorkoutSetListener (val clickListener: (WorkoutDetailAndSets) -> Unit):
    RecyclerviewItemClickListener<WorkoutDetailAndSets> {

    override fun onClick(item: WorkoutDetailAndSets) = clickListener(item)

}


class DropWorkoutListener (val clickListener: (WorkoutDetailAndSets) -> Unit):
    RecyclerviewItemClickListener<WorkoutDetailAndSets> {

    override fun onClick(item: WorkoutDetailAndSets) = clickListener(item)

}

class CompleteWorkoutScheduleListener (val clickListener: () -> Unit):
    RecyclerviewItemClickListener<Unit> {

    override fun onClick(item: Unit) = clickListener()

}


sealed class WorkoutDetailItem {

    abstract val id: Long

    data class Item(val workoutDetailAndSets: WorkoutDetailAndSets): WorkoutDetailItem() {
        override val id = workoutDetailAndSets.workoutDetail.detailId
    }

    object Footer: WorkoutDetailItem() {
        override val id: Long = Long.MIN_VALUE
    }
}