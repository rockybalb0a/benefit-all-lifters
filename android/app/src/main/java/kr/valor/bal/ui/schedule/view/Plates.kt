package kr.valor.bal.ui.schedule.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import kr.valor.bal.R


class Plates: ShapeableImageView {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle)

    var platesInfo: PlatesInfo = PlatesInfo.PLATES25

    init {
        this.id = generateViewId()
    }

    fun isLoadableToBarbell(platesStack: List<Plates>, container: ConstraintLayout): Boolean {
        var totalPlatesWidth = 0
        platesStack.forEach {
            totalPlatesWidth += it.width
        }
        totalPlatesWidth += (platesInfo.width.ratio * container.width).toInt()
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
//            .setAllCorners(CornerFamily.ROUNDED, 5f)
            .build()
        setBackgroundColor(ContextCompat.getColor(context, platesInfo.color.colorCode))
    }

}

enum class PlatesInfo(val color: PlatesColor, val width: PlatesWidth, val diameter: PlatesDiameter?) {
    PLATES25(PlatesColor.RED, PlatesWidth.P25, null),
    PLATES20(PlatesColor.BLUE ,PlatesWidth.P20, null),
    PLATES15(PlatesColor.YELLOW ,PlatesWidth.P15, null),
    PLATES10(PlatesColor.GREEN, PlatesWidth.P10, null),
    PLATES5(PlatesColor.WHITE, PlatesWidth.P5, PlatesDiameter.P5),
    PLATES2_5(PlatesColor.RED, PlatesWidth.P2_5, PlatesDiameter.P2_5),
    PLATES2(PlatesColor.BLUE, PlatesWidth.P2, PlatesDiameter.P2),
    PLATES1_5(PlatesColor.YELLOW, PlatesWidth.P1_5, PlatesDiameter.P1_5),
    PLATES1(PlatesColor.GREEN, PlatesWidth.P1, PlatesDiameter.P1),
    PLATES0_5(PlatesColor.WHITE, PlatesWidth.P0_5, PlatesDiameter.P0_5)
}

enum class PlatesColor(val colorCode: Int) {
    RED(R.color.plateColor25),
    BLUE(R.color.plateColor20),
    YELLOW(R.color.plateColor15),
    GREEN(R.color.plateColor10),
    WHITE(R.color.plateColor5)
}

enum class PlatesWidth(val ratio: Double) {
    P25(64.0 / 415),
    P20(54.0 / 415),
    P15(42.0 / 415),
    P10(34.0 / 415),
    P5(27.0 / 415),
    P2_5(19.0 / 415),
    P2(19.0 / 415),
    P1_5(18.0 / 415),
    P1(15.0 / 415),
    P0_5(13.0 / 415)
}

enum class PlatesDiameter(val ratio: Double) {
    P5(230.0 / 450),
    P2_5(210.0 / 450),
    P2(190.0 / 450),
    P1_5(175.0 / 450),
    P1(160.0 / 450),
    P0_5(135.0 / 450)
}


class NoSuchPlatesException(msg: String = "No plates"): Exception(msg)