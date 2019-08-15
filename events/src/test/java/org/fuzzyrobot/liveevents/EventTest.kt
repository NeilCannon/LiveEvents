package org.fuzzyrobot.liveevents

import org.fuzzyrobot.liveevents.Event
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class EventTest {

    @Test
    fun `handle only once`() {
        val ev = Event(1)
        assertFalse(ev.hasBeenHandled)
        assertEquals(1, ev.peekContent())
        assertEquals(1, ev.getContentIfNotHandled())

        assertTrue(ev.hasBeenHandled)
        assertEquals(1, ev.peekContent())
        assertEquals(null, ev.getContentIfNotHandled())

    }
}