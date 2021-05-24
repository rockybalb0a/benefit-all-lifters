package kr.valor.bal

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class LocalDateTest {
    @Test
    fun `Are LocalDate instances are same`() {
        val one = LocalDate.now()
        val two = LocalDate.now()

        assertThat(one, `is`(two))
    }

}