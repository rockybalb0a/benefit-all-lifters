package kr.valor.bal.ui.schedule.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import kr.valor.bal.R
import kr.valor.bal.utilities.NO_SUCH_PLATES_MSG


class Plates: ShapeableImageView {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle)

    var platesInfo: PlatesInfo = PlatesInfo.PLATES_DEFAULT

    init {
        this.id = generateViewId()
    }

    fun isLoadableToBarbell(platesStack: List<Plates>, container: ConstraintLayout): Boolean {
        var totalPlatesWidth = 0
        platesStack.forEach {
            totalPlatesWidth += it.width
        }
        totalPlatesWidth += (platesInfo.thickness.ratio * container.width).toInt()
        return totalPlatesWidth < container.width
    }

    fun attachTo(plates: Plates, container: ConstraintLayout) {
        container.addView(this)
        val constraintSet = ConstraintSet()
        constraintSet.applyConstraint(this, container) {
            constraintStartToEndOf(this@Plates, plates)
        }
    }

    fun attachTo(container: ConstraintLayout) {
        container.addView(this)
        val constraintSet = ConstraintSet()
        constraintSet.applyConstraint(this, container) {
            constraintStartToStartOf(this@Plates, container)
        }
    }

    fun setAppearance() {
        this.shapeAppearanceModel = shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, 5f)
            .build()
        setBackgroundColor(ContextCompat.getColor(context, platesInfo.color.colorCode))
    }

}

enum class PlatesInfo(val color: PlatesColor, val thickness: PlatesThickness, val radius: PlatesRadius?) {
    PLATES25(PlatesColor.RED, PlatesThickness.XXL, null),
    PLATES20(PlatesColor.BLUE ,PlatesThickness.XL, null),
    PLATES15(PlatesColor.YELLOW ,PlatesThickness.L, null),
    PLATES10(PlatesColor.GREEN, PlatesThickness.M, null),
    PLATES5(PlatesColor.WHITE, PlatesThickness.S, PlatesRadius.M),
    PLATES_DEFAULT(PlatesColor.RED, PlatesThickness.XXL, null)
}

enum class PlatesColor(val colorCode: Int) {
    RED(R.color.plateColor25),
    BLUE(R.color.plateColor20),
    YELLOW(R.color.plateColor15),
    GREEN(R.color.plateColor10),
    WHITE(R.color.plateColor5)
}

enum class PlatesThickness(val ratio: Double) {
    XXL(64.0 / 415),    // 25
    XL(54.0 / 415),     // 20
    L(42.0 / 415),      // 15
    M(34.0 / 415),      // 10
    S(26.0 / 415),      // 5
    XS(19.0 / 415),     // 2.5, 1.5
    XXS(15.0 / 415)     // 1
}

enum class PlatesRadius(val ratio: Double) {
    M(230.0 / 450),     // 5
    S(210.0 / 450),     // 2.5
    XS(175.0 / 415),    // 1.5
    XXS(160.0 / 450)    // 1.0
}

enum class BarbellState {
    EMPTY,
    LOADED,
    FULL
}

class NoSuchPlatesException(msg: String = "No plates"): Exception(msg)