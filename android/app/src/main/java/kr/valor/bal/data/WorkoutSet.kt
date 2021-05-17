package kr.valor.bal.data

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSet(
    var weight: Short,
    var reps: Byte
)