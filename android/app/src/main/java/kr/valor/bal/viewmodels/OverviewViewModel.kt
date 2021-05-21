package kr.valor.bal.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.utilities.randomGenerator
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    workoutRepository: DefaultRepository
): ViewModel() {
    val workouts = workoutRepository.getAllWorkouts()
}