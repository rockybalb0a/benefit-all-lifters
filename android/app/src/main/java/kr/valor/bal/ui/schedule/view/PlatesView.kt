package kr.valor.bal.ui.schedule.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kr.valor.bal.databinding.LayoutPlatesBinding


class PlatesView: ConstraintLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)

    private var binding: LayoutPlatesBinding
    private var container: ConstraintLayout
    private val currentPlatesStack: MutableList<Plates> = mutableListOf()
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

    fun getBarbellStatus(weight: Double): BarbellState {
        val candidatePlates = createPlates(weight)
        return if (currentPlatesStack.isNullOrEmpty()) {
            BarbellState.EMPTY
        } else {
            if (candidatePlates.isLoadableToBarbell(currentPlatesStack, container)) {
                BarbellState.LOADED
            } else {
                BarbellState.FULL
            }
        }
    }


    fun synchronizePlatesInfo(changedPlatesStack: List<Double>) {
        if (currentPlatesStack.isEmpty() && changedPlatesStack.isNotEmpty()) {
            changedPlatesStack.forEach {
                insertPlates(it)
            }
        }
        when (changedPlatesStack.size - currentPlatesStack.size) {
            1 -> insertPlates(changedPlatesStack.last())
            -1 -> popPlates()
        }
    }


    private fun getBarbellStatus(newPlates: Plates): BarbellState {
        return if (currentPlatesStack.isNullOrEmpty()) {
            BarbellState.EMPTY
        } else {
            if (newPlates.isLoadableToBarbell(currentPlatesStack, container)) {
                BarbellState.LOADED
            } else {
                BarbellState.FULL
            }
        }
    }


    private fun insertPlates(weight: Double): BarbellState {
        val newPlates = createPlates(weight)
        val barbellState = getBarbellStatus(newPlates)
        when (barbellState) {
            BarbellState.EMPTY -> {
                newPlates.attachTo(container)
                currentPlatesStack.add(newPlates)
            }
            BarbellState.LOADED -> {
                newPlates.attachTo(currentPlatesStack.last(), container)
                currentPlatesStack.add(newPlates)
            }
            BarbellState.FULL -> { /* Do nothing */ }
        }
        return barbellState
    }

    private fun popPlates(): BarbellState {
        return try {
            container.removeView(currentPlatesStack.removeLast())
            if (currentPlatesStack.size == 0) BarbellState.EMPTY else BarbellState.LOADED
        } catch (e: NoSuchElementException) {
            BarbellState.EMPTY
        }
    }


    private fun createLayoutBinding(): LayoutPlatesBinding {
        return LayoutPlatesBinding
            .inflate(LayoutInflater.from(context), this, false)
            .also {
                it.root.id = generateViewId()
                addView(it.root)
            }
    }



    private fun createPlates(weight: Double): Plates {
        return Plates(context).apply {
            platesInfo = pickPlates(weight).also { platesInfo ->
                val platesWidth = (platesInfo.width.ratio * barbellLength).toInt()
                val platesDiameter = platesInfo.diameter?.let { radius ->
                    (radius.ratio * defaultPlateRadius).toInt()
                } ?: LayoutParams.MATCH_PARENT

                layoutParams = LayoutParams(platesWidth, platesDiameter)
            }
            setAppearance()
        }
    }

    private fun pickPlates(weight: Double): PlatesInfo {
        return when(weight) {
            0.5 -> PlatesInfo.PLATES0_5
            1.0 -> PlatesInfo.PLATES1
            1.5 -> PlatesInfo.PLATES1_5
            2.0 -> PlatesInfo.PLATES2
            2.5 -> PlatesInfo.PLATES2_5
            5.0 -> PlatesInfo.PLATES5
            10.0 -> PlatesInfo.PLATES10
            15.0 -> PlatesInfo.PLATES15
            20.0 -> PlatesInfo.PLATES20
            25.0 -> PlatesInfo.PLATES25
            else -> throw NoSuchPlatesException()
        }
    }
}

enum class BarbellState {
    EMPTY,
    LOADED,
    FULL
}
