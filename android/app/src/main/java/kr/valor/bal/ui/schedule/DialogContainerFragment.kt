package kr.valor.bal.ui.schedule

import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.valor.bal.R
import kr.valor.bal.databinding.FragmentScheduleBinding

open class DialogContainerFragment: Fragment(){

    protected inline fun <reified T: ViewDataBinding> Class<T>.initBinding() {
        if (this.isAssignableFrom(FragmentScheduleBinding::class.java)) {
            Toast.makeText(context, "Good", Toast.LENGTH_SHORT).show()
        }

    }

    protected fun showWorkoutSelectionDialog(viewModel: BaseViewModel): AlertDialog {
        val title = resources.getString(R.string.add_new_workout_popup_title)
        val items = resources.getStringArray(R.array.exercise_list)

        return MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_App_Dialog)
            .setTitle(title)
            .setItems(items) { _ , i: Int ->
                viewModel.onDialogItemSelected(items[i])
            }
            .show()
    }

    protected inline fun showEditActionChoiceDialog(crossinline acceptAction: () -> Unit): AlertDialog {
        val titleRes = R.string.edit_action_choice_dialog_title
        val messageRes = R.string.edit_action_choice_dialog_message
        val positiveBtnLabelRes = R.string.edit_action_choice_dialog_positive_action_btn_label
        val negativeBtnLabelRes = R.string.edit_action_choice_dialog_negative_action_btn_label

        return MaterialAlertDialogBuilder(requireContext(), R.style.Theme_App_Dialog)
            .setTitle(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(positiveBtnLabelRes) { _, _ ->
                acceptAction()
            }
            .setNegativeButton(negativeBtnLabelRes) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    protected inline fun showTimerResetActionChoiceDialog(crossinline action: () -> Unit): AlertDialog {
        val dialogTitleRes = R.string.timer_stop_action_choice_dialog_title
        val dialogMessageRes = R.string.timer_stop_action_choice_dialog_message
        val dialogPositiveBtnLabelRes = R.string.timer_stop_action_choice_dialog_positive_action_btn_label
        val dialogNegativeBtnLabelRes = R.string.timer_stop_action_choice_dialog_negative_action_btn_label

        return MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_App_Dialog)
            .setTitle(dialogTitleRes)
            .setMessage(dialogMessageRes)
            .setPositiveButton(dialogPositiveBtnLabelRes) { _, _ ->
                action()
            }
            .setNegativeButton(dialogNegativeBtnLabelRes) { dialogInterface: DialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    protected inline fun showApplyChangeChoiceActionDialog(
        crossinline positiveAction: () -> Unit,
        crossinline negativeAction: () -> Unit
    ): AlertDialog {
        val dialogTitleRes = R.string.save_changes_action_choice_dialog_title
        val dialogMessageRes = R.string.save_changes_action_choice_dialog_message
        val dialogPositiveBtnLabelRes = R.string.save_changes_action_choice_dialog_positive_action_btn_label
        val dialogNegativeBtnLabelRes = R.string.save_changes_action_choice_dialog_negative_action_btn_label

        return MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_App_Dialog)
            .setTitle(dialogTitleRes)
            .setMessage(dialogMessageRes)
            .setPositiveButton(dialogPositiveBtnLabelRes) { _,_ ->
                positiveAction()
            }
            .setNegativeButton(dialogNegativeBtnLabelRes) {_, _ ->
                negativeAction()
            }
            .show()
    }

}