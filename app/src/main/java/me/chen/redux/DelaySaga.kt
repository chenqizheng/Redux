package me.chen.redux

import kotlinx.coroutines.delay
import me.chen.redux.core.Saga.INSTANCE.put
import me.chen.redux.core.Saga.INSTANCE.take

suspend fun increment() {
    delay(1000)
    put("INCREMENT_SUCCESS", null);
}

suspend fun rootSaga() {
    take("INCREMENT_ASYNC", ::increment)
}