package kr.valor.bal.ui.home.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import kr.valor.bal.R
import kr.valor.bal.databinding.HomeBigWorkoutInfoBinding

class BigMuscleWorkoutView: CardView {

    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    private var binding: HomeBigWorkoutInfoBinding


    init {
        binding = createLayoutBinding()
        binding.root.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
    }

    private fun createLayoutBinding(): HomeBigWorkoutInfoBinding {
        return HomeBigWorkoutInfoBinding.inflate(LayoutInflater.from(context), this, false)
            .also {
                it.root.id = generateViewId()
                addView(it.root)
            }
    }

}