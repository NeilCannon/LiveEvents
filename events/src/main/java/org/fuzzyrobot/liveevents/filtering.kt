package org.fuzzyrobot.liveevents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

fun <T> LiveData<T>.first(): LiveData<T> {
    var seen = false
    return Transformations.switchMap(this) { newValue ->
        if (seen) {
            null
        } else {
            seen = true
            MutableLiveData<T>().apply { value = newValue }
        }
    }
}

fun <T> LiveData<T>.filter(block: (T) -> Boolean): LiveData<T> {
    val filteredLiveData = MediatorLiveData<T>()
    filteredLiveData.addSource(this) {
        it?.let {
            if (block.invoke(it)) {
                filteredLiveData.value = it
            }
        }
    }

    return filteredLiveData
}

fun <T> LiveData<Event<T>>.filterEvent(block: (T) -> Boolean): LiveData<Event<T>> {
    val filteredLiveData = MediatorLiveData<Event<T>>()
    filteredLiveData.addSource(this) {
        it?.let {
            if (block.invoke(it.peekContent())) {
                filteredLiveData.value = it
            }
        }
    }

    return filteredLiveData
}
