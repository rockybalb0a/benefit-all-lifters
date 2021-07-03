package kr.valor.bal.adapters

import android.view.LayoutInflater
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kr.valor.bal.databinding.OverviewCardviewItemBinding
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
@SmallTest
class ViewHolderFactoryTest {
    private lateinit var binding: OverviewCardviewItemBinding
    private lateinit var layoutInflater: LayoutInflater

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        layoutInflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    }

}