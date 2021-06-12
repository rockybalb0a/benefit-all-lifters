package kr.valor.bal.ui.schedule.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.R
import kr.valor.bal.databinding.FragmentScheduleDialogBinding
import kr.valor.bal.ui.schedule.ScheduleFragmentDirections
import kr.valor.bal.ui.schedule.view.PlatesView

@AndroidEntryPoint
class ScheduleSetDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: ScheduleSetViewModel by viewModels()

    private lateinit var platesView: PlatesView

    private lateinit var binding: FragmentScheduleDialogBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: ScheduleSetDialogFragmentArgs by navArgs()
        setupClickListener()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        platesView = binding.platesView
        return binding.root
    }


    private fun setupClickListener() {
        with(binding) {
            redPlatesButton.setOnClickListener {
                platesView.insertPlates(25.0)
            }
            bluePlatesButton.setOnClickListener {
                platesView.insertPlates(20.0)
            }
            yellowPlatesButton.setOnClickListener {
                platesView.insertPlates(15.0)
            }
            greenPlatesButton.setOnClickListener {
                platesView.insertPlates(10.0)
            }
            whitePlatesButton.setOnClickListener {
                platesView.insertPlates(5.0)
            }
            popPlatesButton.setOnClickListener {
                platesView.popPlates()
            }


//            topCloseButton.setOnClickListener {
//                dismiss()
//            }
        }
    }
}