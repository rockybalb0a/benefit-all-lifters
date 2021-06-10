package kr.valor.bal.ui.schedule.view

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintSet.initPlatesView(view: View, parentView: View) {
    clone(this)
    attachToParent(view, parentView)
}

fun ConstraintSet.attachToParent(view: View, parentView: View) {
    connect(view.id, ConstraintSet.TOP, parentView.id, ConstraintSet.TOP)
    connect(view.id, ConstraintSet.BOTTOM, parentView.id, ConstraintSet.BOTTOM)
    connect(view.id, ConstraintSet.START, parentView.id, ConstraintSet.START)
    connect(view.id, ConstraintSet.END, parentView.id, ConstraintSet.END)
}

fun ConstraintSet.constraintStartToStartOf(from: View, to: View, margin: Int = 0) {
    connect(from.id, ConstraintSet.START, to.id, ConstraintSet.START, margin)
}

fun ConstraintSet.constraintStartToEndOf(from: View, to: View, margin: Int = 1) {
    connect(from.id, ConstraintSet.START, to.id, ConstraintSet.END, margin)
}

fun ConstraintSet.setVerticalConstraint(from: View, to: View, margin: Int = 0) {
    constraintTopToTopOf(from, to, margin)
    constraintBottomToBottomOf(from, to, margin)
}

fun ConstraintSet.constraintTopToTopOf(from: View, to: View, margin: Int = 0) {
    connect(from.id, ConstraintSet.TOP, to.id, ConstraintSet.TOP, margin)
}

fun ConstraintSet.constraintBottomToBottomOf(from: View, to: View, margin: Int = 0) {
    connect(from.id, ConstraintSet.BOTTOM, to.id, ConstraintSet.BOTTOM, margin)
}

fun ConstraintSet.applyConstraint(from: Plates, container: ConstraintLayout, block: ConstraintSet.() -> Unit) {
    clone(container)
    block()
    setVerticalConstraint(from, container, 0)
    applyTo(container)
}