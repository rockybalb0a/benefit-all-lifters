package kr.valor.bal.ui.overview

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.DefaultRepository
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    workoutRepository: DefaultRepository
): ViewModel() {
    val workouts = workoutRepository.getAllWorkouts()
}