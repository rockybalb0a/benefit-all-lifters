package kr.valor.bal.ui.schedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.R
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.ScheduleDialogBinding
import kr.valor.bal.ui.schedule.view.BarbellState
import kr.valor.bal.ui.schedule.view.PlatesView

@AndroidEntryPoint
class ScheduleSetDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: ScheduleSetViewModel by viewModels()

    private lateinit var platesView: PlatesView

    private lateinit var binding: ScheduleDialogBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.currentWorkoutSet.observe(viewLifecycleOwner) {
            platesView.synchronizePlatesInfo(it.platesStack)
        }

        viewModel.candidatePlatesLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (platesView.getBarbellStatus(it) == BarbellState.FULL) {
                    viewModel.discardChange()
                    Toast.makeText(context, resources.getString(R.string.platesview_full_text), Toast.LENGTH_SHORT).show()
                    return@let
                }
                viewModel.applyChange()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ScheduleDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        platesView = binding.platesView
        return binding.root
    }

}