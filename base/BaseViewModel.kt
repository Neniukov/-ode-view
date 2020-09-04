package com.allocaterite.allocaterite.core.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import com.allocaterite.allocaterite.utils.Logger
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel(), CoroutineScope, LifecycleObserver {

    private val job: Job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        onError(exception)
    }

    val errorLD = MutableLiveData<String>()

    final override val coroutineContext = Dispatchers.Main + job + exceptionHandler

    override fun onCleared() {
        coroutineContext.cancelChildren()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        super.onCleared()
    }

    open fun onError(exception: Throwable) = launch {
        if (exception.message?.trim()?.toLowerCase() != "http 401") {
            errorLD.value = exception.message
        }
        Logger.e("${exception.message}")
    }
}
