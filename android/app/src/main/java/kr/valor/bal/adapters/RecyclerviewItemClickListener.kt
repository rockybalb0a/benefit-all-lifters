package kr.valor.bal.adapters

import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutOverview
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

class EditWorkoutScheduleListener (val clickListener: () -> Unit):
    RecyclerviewItemClickListener<Unit>{

    override fun onClick(item: Unit)  = clickListener()

}

class ManualTimerSettingListener (val clickListener: () -> Unit): RecyclerviewItemClickListener<Unit> {

    override fun onClick(item: Unit)  = clickListener()

}


sealed class WorkoutDetailItem {

    abstract val id: Long

    data class Header(val workoutOverview: WorkoutOverview): WorkoutDetailItem() {
        override val id: Long = Long.MAX_VALUE
    }

    data class Item(val workoutDetailAndSets: WorkoutDetailAndSets): WorkoutDetailItem() {
        override val id = workoutDetailAndSets.workoutDetail.detailId
    }

    object Footer: WorkoutDetailItem() {
        override val id: Long = Long.MIN_VALUE
    }

    companion object {

        fun convertToRequireHeaderAdapterList(schedule: WorkoutSchedule): List<WorkoutDetailItem> {
            val headerItem = Header(schedule.workoutOverview)
            val list = generateBaseList(schedule)
            list.add(0, headerItem)

            return list.toList()
        }

        fun convertToNoHeaderAdapterList(schedule: WorkoutSchedule): List<WorkoutDetailItem> =
            generateBaseList(schedule).toList()

        private fun generateBaseList(schedule: WorkoutSchedule): MutableList<WorkoutDetailItem> {
            val contents = schedule.workoutDetails.map { Item(it) }
            val footerItem = Footer
            val list = mutableListOf<WorkoutDetailItem>()
            list.addAll(contents)
            list.add(footerItem)

            return list
        }
    }

}