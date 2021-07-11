package kr.valor.bal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.databinding.ScheduleDetailFragmentBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator

@AndroidEntryPoint
class ScheduleDetailFragment: Fragment() {

    private val viewModel: ScheduleDetailViewModel by viewModels()

    private lateinit var binding: ScheduleDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ScheduleDetailFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initBinding()
    }

    private fun ScheduleDetailFragmentBinding.initBinding() {
        with(this) {
            viewModel = this@ScheduleDetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            headerBindingCreator = WorkoutSummaryInfoBindingParameterCreator
            contentBindingCreator = WorkoutDetailInfoBindingParameterCreator
        }
    }
}