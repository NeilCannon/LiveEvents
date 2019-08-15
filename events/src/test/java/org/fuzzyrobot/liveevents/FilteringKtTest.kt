package org.fuzzyrobot.liveevents

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.fuzzyrobot.liveevents.*
import org.junit.Before
import org.junit.Test

class FilteringKtTest {

    val data = MutableLiveData<Int>()
    val allObserver = mock<Observer<Int>> { }
    val firstObserver = mock<Observer<Int>> { }

    @Before
    fun setUp() {
        ArchTaskExecutor.getInstance().setDelegate(FauxMainThreadExecutor)
    }

    @Test
    fun first() {

        data.observeForever(allObserver)

        data.first().observeForever(firstObserver)

        data.value = 1

        verify(allObserver).onChanged(1)
        verify(firstObserver).onChanged(1)

        data.value = 2

        verify(allObserver).onChanged(2)
        verifyNoMoreInteractions(firstObserver)
    }

    @Test
    fun filter() {
        val x = MutableLiveData<Int>()
        val observer = mock<Observer<Int>> {}
        x.filter { it > 10 }.observeForever(observer)
        x.value = 1
        x.value = 20
        verify(observer).onChanged(20)
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun filterEvent() {
        val x = MutableLiveEvent<Int>()
        val observer = mock<(Int) -> Unit> {}
        x.filterEvent { it > 10 }.observeEventForever(observer)
        x.value = Event(1)
        x.value = Event(20)
        verify(observer).invoke(20)
        verifyNoMoreInteractions(observer)
    }
}