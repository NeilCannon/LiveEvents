package org.fuzzyrobot.liveevents

import androidx.lifecycle.*

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

fun <T> LiveEvent<T>.observeEvent(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner) {
        it.getContentIfNotHandled()?.let { newValue ->
            observer(newValue)
        }
    }
}

fun <T> LiveEvent<T>.observeEventForever(observer: (T) -> Unit) {
    observeForever {
        it?.getContentIfNotHandled()?.let { newValue ->
            observer(newValue)
        }
    }
}

fun LiveEvent<Unit>.observeEventForever(observer: () -> Unit) {
    observeForever {
        it?.getContentIfNotHandled()?.let {
            observer()
        }
    }
}


