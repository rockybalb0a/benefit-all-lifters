package kr.valor.bal

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class PropertyTest {

    private val list: MutableList<Int> = mutableListOf()

    private val isEmpty: Boolean
        get() = list.size == 0

    @Test
    fun `property backing field`() {

        assertThat(isEmpty, `is`(true))

        list.add(1)

        assertThat(isEmpty, `is`(false))
    }
}