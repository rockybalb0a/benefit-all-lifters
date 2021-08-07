package kr.valor.bal

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.temporal.ChronoUnit

class LocalDateTest {
    @Test
    fun `Are LocalDate instances are same`() {
        val one = LocalDate.now()
        val two = LocalDate.now()

        assertThat(one, `is`(two))
    }

    @Test
    fun `LocalDate instance add and sub`() {
        val startDay = LocalDate.of(2017,2,16)
        val today = LocalDate.of(2021,8,8)

        val dDay = ChronoUnit.DAYS.between(startDay, today) + 1
        assertThat(dDay, `is`(1635))

    }

}