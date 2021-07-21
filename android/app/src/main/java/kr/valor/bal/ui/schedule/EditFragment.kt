package kr.valor.bal.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.R
import kr.valor.bal.databinding.EditFragmentBinding


@AndroidEntryPoint
class EditFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()

    private val navArgs by navArgs<EditFragmentArgs>()

    private lateinit var binding: EditFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return EditFragmentBinding.inflate(inflater, container, false).also {
            binding = it
            binding.initBinding()
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBackButtonPressedCallback()

    }

    private fun EditFragmentBinding.initBinding() {
        viewModel = this@EditFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
    }

    private fun initBackButtonPressedCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showApplyChangeChoiceActionDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun showApplyChangeChoiceActionDialog(): AlertDialog {
        val dialogTitleRes = R.string.save_changes_action_choice_dialog_title
        val dialogMessageRes = R.string.save_changes_action_choice_dialog_message
        val dialogPositiveBtnLabelRes = R.string.save_changes_action_choice_dialog_positive_action_btn_label
        val dialogNegativeBtnLabelRes = R.string.save_changes_action_choice_dialog_negative_action_btn_label

        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(dialogTitleRes)
            .setMessage(dialogMessageRes)
            .setPositiveButton(dialogPositiveBtnLabelRes) { _,_ ->
                findNavController().navigate(
                    EditFragmentDirections.actionEditDestToScheduleDetailDest(
                        navArgs.overviewId
                    )
                )
            }
            .setNegativeButton(dialogNegativeBtnLabelRes) {_, _ ->
                Toast.makeText(context, "못가히히!!", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}