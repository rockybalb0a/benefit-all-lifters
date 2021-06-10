package kr.valor.bal.ui.schedule.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kr.valor.bal.databinding.PlatesLayoutBinding


class PlatesView: ConstraintLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)

    private var binding: PlatesLayoutBinding
    private var container: ConstraintLayout
    private val platesStack: MutableList<Plates> = mutableListOf()
    private var barbellLength = 0
    private var defaultPlateRadius = 0

    init {
        val set = ConstraintSet()
        binding = createLayoutBinding()
        container = binding.root
        set.initPlatesView(container, this)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (barbellLength == 0) {
            barbellLength = this.width
            defaultPlateRadius = this.height
        }
    }

    fun insertPlates(weight: Double) {
        val newPlates = createPlates(weight)
        when (getBarbellStatus(newPlates)) {
            BarbellState.EMPTY -> {newPlates.attachTo(container); platesStack.add(newPlates)}
            BarbellState.LOADED -> {newPlates.attachTo(platesStack.last(), container); platesStack.add(newPlates) }
            BarbellState.FULL -> Toast.makeText(context, "Full", Toast.LENGTH_SHORT).show()
        }
    }

    fun popPlates() {
        try {
            container.removeView(platesStack.removeLast())
        } catch (e: NoSuchElementException) {
            Toast.makeText(context, "No plates!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getPlatesStack() = platesStack.toList()

    private fun createLayoutBinding(): PlatesLayoutBinding {
        return PlatesLayoutBinding
            .inflate(LayoutInflater.from(context), this, false)
            .also {
                it.root.id = generateViewId()
                addView(it.root)
            }
    }

    private fun getBarbellStatus(newPlates: Plates): BarbellState {
        return if (platesStack.isNullOrEmpty()) {
            BarbellState.EMPTY
        } else {
            if (newPlates.isLoadableToBarbell(platesStack, container)) {
                BarbellState.LOADED
            } else {
                BarbellState.FULL
            }
        }
    }

    private fun createPlates(weight: Double): Plates {
        return Plates(context).apply {
            platesInfo = pickPlates(weight).also {
                val platesThickness = (it.thickness.ratio * barbellLength).toInt()
                val platesRadius = it.radius?.let { radius ->
                    (radius.ratio * defaultPlateRadius).toInt()
                } ?: LayoutParams.MATCH_PARENT

                layoutParams = LayoutParams(platesThickness, platesRadius)
            }
            setAppearance()
        }
    }

    private fun pickPlates(weight: Double): PlatesInfo {
        return when(weight) {
            5.toDouble() -> PlatesInfo.PLATES5
            10.toDouble() -> PlatesInfo.PLATES10
            15.toDouble() -> PlatesInfo.PLATES15
            20.toDouble() -> PlatesInfo.PLATES20
            25.toDouble() -> PlatesInfo.PLATES25
            else -> throw NoSuchPlatesException()
        }
    }
}
