package kr.valor.bal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.databinding.FragmentHomeBinding
import kr.valor.bal.viewmodels.HomeViewModel


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addWorkoutView.setOnClickListener {
            viewModel.onNewWorkoutClicked()
        }

        binding.restTodayView.setOnClickListener {
            viewModel.onRestTodayClickedEvent()
        }

        viewModel.newWorkoutClickedEvent.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(context, "Let's Go", Toast.LENGTH_SHORT).show()
                viewModel.doneNewWorkoutClicked()
            }
        }

        viewModel.restTodayClickedEvent.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(context, "I'll rest today", Toast.LENGTH_SHORT).show()
                viewModel.doneRestTodayClickedEvent()
            }
        }
    }

}