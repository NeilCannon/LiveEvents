package org.fuzzyrobot.liveevents

import androidx.lifecycle.*
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ObservingKtTest {

    val lifecycleOwner = mock<LifecycleOwner>()
    val event1 = Event(1)
    val observer1 = mock<(Int) -> Unit>()
    val observer2 = mock<(Int) -> Unit>()

    @Test
    fun observeEvent() {

        val event = mock<LiveEvent<Int>> {
            on { observe(any(), any()) } doAnswer {
                assertEquals(lifecycleOwner, it.arguments[0])
                val observer = it.arguments[1] as Observer<Event<Int>>
                observer.onChanged(event1)
                Unit
            }
        }

        event.observeEvent(lifecycleOwner, observer1)
        event.observeEvent(lifecycleOwner, observer2)

        verify(observer1).invoke(1)
        verify(observer2, never()).invoke(1)

    }

    @Test
    fun observeEventForever() {
        val event = mock<LiveEvent<Int>> {
            on { observeForever(any()) } doAnswer {
                val observer = it.arguments[0] as Observer<Event<Int>>
                observer.onChanged(event1)
                Unit
            }
        }

        event.observeEventForever(observer1)
        event.observeEventForever(observer2)

        verify(observer1).invoke(1)
        verify(observer2, never()).invoke(1)

    }

    @Test
    fun observeEventForeverUnit() {
        val unitEvent = Event<Unit>(Unit)

        val event = mock<LiveEvent<Unit>> {
            on { observeForever(any()) } doAnswer {
                val observer = it.arguments[0] as Observer<Event<Unit>>
                observer.onChanged(unitEvent)
                Unit
            }
        }

        val unitObserver1 = mock<() -> Unit>()
        val unitObserver2 = mock<() -> Unit>()

        event.observeEventForever(unitObserver1)
        event.observeEventForever(unitObserver2)

        verify(unitObserver1).invoke()
        verify(unitObserver2, never()).invoke()
    }

}