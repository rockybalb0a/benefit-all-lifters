package kr.valor.bal.data

import kr.valor.bal.data.local.AppDatabaseConverters
import kr.valor.bal.data.local.user.UserPersonalRecording
import org.hamcrest.Matchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class ConvertersTest {

    private lateinit var converters: AppDatabaseConverters

    @Before
    fun `init`() {
        converters = AppDatabaseConverters()
    }

    @Test
    fun `user pr records converts to string returns expected result`() {
        val list = mutableListOf(
            UserPersonalRecording("Back Squat", 100.0, 3),
            UserPersonalRecording("Front Squat", 100.0, 3),
        )
        val converted = converters.userPrRecordingListToColumn(list)
        assertThat(converted, `is`("Back Squat-100.0-3/Front Squat-100.0-3"))

        assertThat(converters.columnToUserPrRecordingList(converted), `is`(list))

    }

    @Test
    fun `user pr records (empty) to string returns expected result`() {
        val list = mutableListOf<UserPersonalRecording>()
        val converted = converters.userPrRecordingListToColumn(list)

        assertThat(converted, `is`(""))
        assertThat(converters.columnToUserPrRecordingList(""), `is`(list))
        assertThat(converters.columnToUserPrRecordingList(converted), `is`(list))
    }

    @Test
    fun `plates converters works as expected`() {
        val list = mutableListOf(25.0, 15.0, 2.5)
        val converted = converters.platesStackToColumn(list)

        assertThat(converted, `is`("25.0 15.0 2.5"))

        assertThat(converters.columnToPlatesStack(converted), `is`(list))
    }

}