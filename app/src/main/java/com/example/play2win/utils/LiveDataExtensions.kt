@file:Suppress("unused")

package com.example.play2win.utils

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Runnable
import java.util.concurrent.TimeUnit

fun <T> mutableLiveDataWithValue(initial: T?): MutableLiveData<T> =
    MediatorLiveData<T>()
        .apply { value = initial }

fun <T> LiveData<T>.distinct(): LiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    val dispatchedValues = mutableListOf<T?>()
    mutableLiveData.addSource(this) {
        if (!dispatchedValues.contains(it)) {
            mutableLiveData.value = it
            dispatchedValues.add(it)
        }
    }
    return mutableLiveData
}

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    var lastDispatchedValue: T? = null
    mutableLiveData.addSource(this) {
        if (lastDispatchedValue != it) {
            lastDispatchedValue = it
            mutableLiveData.value = it
        }
    }
    return mutableLiveData
}

fun <T> LiveData<T>.postValue(): LiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    mutableLiveData.addSource(this) {
        mutableLiveData.postValue(it)
    }
    return mutableLiveData
}

inline fun <T> LiveData<T>.filter(crossinline predicate: (T?) -> Boolean): LiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    mutableLiveData.addSource(this) {
        if (predicate(it)) {
            mutableLiveData.value = it
        }
    }
    return mutableLiveData
}

fun <A, B> zipLiveData(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <A, B, C> zipLiveData(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>): LiveData<Triple<A, B, C>> {
    return MediatorLiveData<Triple<A, B, C>>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            val localLastC = lastC
            if (localLastA != null && localLastB != null && localLastC != null)
                this.value = Triple(localLastA, localLastB, localLastC)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
        addSource(c) {
            lastC = it
            update()
        }
    }
}

fun <A, B> zipLiveDataAllowNull(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A?, B?>> {
    return MediatorLiveData<Pair<A?, B?>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    mutableLiveData.addSource(this) { mutableLiveData.value = it }
    return mutableLiveData
}

inline fun <T> LiveData<T>.doOnNext(crossinline func: (T?) -> Unit): LiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    mutableLiveData.addSource(this) {
        func(it)
        mutableLiveData.value = it
    }
    return mutableLiveData
}

inline fun <Input, Output> LiveData<Input>.mapNotNull(crossinline func: (Input?) -> Output?): LiveData<Output> {
    val mutableLiveData: MediatorLiveData<Output> = MediatorLiveData()
    mutableLiveData.addSource(this) { input ->
        val result = func(input)
        if (null != result) {
            mutableLiveData.value = result
        }
    }
    return mutableLiveData
}

inline fun <Input, Output> LiveData<Input>.map(crossinline func: (Input?) -> Output?): LiveData<Output> {
    val mutableLiveData: MediatorLiveData<Output> = MediatorLiveData()
    mutableLiveData.addSource(this) { input ->
        val result = func(input)
        mutableLiveData.value = result
    }
    return mutableLiveData
}

fun <T> LiveData<T>.delay(time: Long, timeUnit: TimeUnit): LiveData<T> {
    val handler = Handler()
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    mutableLiveData.addSource(this) {
        handler.postDelayed(
            { mutableLiveData.value = it },
            timeUnit.toMillis(time)
        )
    }
    return mutableLiveData
}

fun <T> LiveData<T>.debounce(time: Long, timeUnit: TimeUnit): LiveData<T> {
    val handler = Handler()
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    var lastData: T? = null
    val updateRunnable = Runnable { mutableLiveData.value = lastData }
    mutableLiveData.addSource(this) {
        lastData = it
        handler.removeCallbacks(updateRunnable)

        handler.postDelayed(
            updateRunnable,
            timeUnit.toMillis(time)
        )
    }
    return mutableLiveData
}