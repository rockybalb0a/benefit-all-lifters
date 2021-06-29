package kr.valor.bal.adapters.listeners

class ScheduleFinishListener(val clickListener: () -> Unit) {
    fun onclick() = clickListener()
}
