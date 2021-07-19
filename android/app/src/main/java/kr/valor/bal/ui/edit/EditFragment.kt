package kr.valor.bal.ui.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.databinding.EditFragmentBinding


@AndroidEntryPoint
class EditFragment : Fragment() {

    private val viewModel: EditViewModel by viewModels()

    private lateinit var toolbar: ActionBar

    private lateinit var binding: EditFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        toolbar = (activity as AppCompatActivity).supportActionBar!!
        
        return EditFragmentBinding.inflate(inflater, container, false).also {
            binding = it
            binding.initBinding()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.workoutSchedule.observe(viewLifecycleOwner) {
            Toast.makeText(context, "ID : ${it.workoutOverview.overviewId}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun EditFragmentBinding.initBinding() {
        viewModel = this@EditFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
    }
}