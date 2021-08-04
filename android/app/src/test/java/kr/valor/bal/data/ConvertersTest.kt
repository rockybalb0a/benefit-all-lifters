package kr.valor.bal.data

import kr.valor.bal.data.local.workout.Converters
import org.hamcrest.Matchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class ConvertersTest {

    private lateinit var converters: Converters

    @Before
    fun `init`() {
        converters = Converters()
    }


    @Test
    fun `plates converters works as expected`() {
        val list = mutableListOf(25.0, 15.0, 2.5)
        val converted = converters.platesStackToColumn(list)

        assertThat(converted, `is`("25.0 15.0 2.5"))

        assertThat(converters.columnToPlatesStack(converted), `is`(list))
    }

}