package kr.valor.bal

import kotlinx.serialization.json.Json
import kr.valor.bal.data.Converter
import kr.valor.bal.data.entities.WorkoutSet
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class SerializationTest {
    @Test
    fun workoutSet_serialization() {
        val set = WorkoutSet(100, 3)
        val setJson = "{\"weight\":100,\"reps\":3}"
        assertThat(Json.encodeToString(WorkoutSet.serializer(), set), `is`(setJson))
    }


    @Test
    fun workoutSet_deserialization() {
        val json = "{   \"weight\":100, \"reps\":3  }"
        val set = WorkoutSet(100, 3)
        assertThat(Json.decodeFromString(WorkoutSet.serializer(), json), `is`(set))
    }

    @Test
    fun workoutSets_converter_test() {
        // GIVEN
        val converter = Converter()
        val sets = listOf(
            WorkoutSet(100,3),
            WorkoutSet(100,3),
            WorkoutSet(100,3)
        )

        // WHEN
        val json = converter.workoutSetsToColumn(sets)

        // THEN
        assertThat(converter.columnToWorkoutSets(json), `is`(sets))
    }
}