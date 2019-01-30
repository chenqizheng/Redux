package me.chen.redux

import me.chen.redux.core.Store
import me.chen.redux.core.loggerMiddleware
import me.chen.redux.core.sagaMiddleware

data class AppState(val count: Int)

class AppStore : Store<AppState>(
    initialState = AppState(0),
    reducers = listOf(::calculate),
    middlewares = listOf(::loggerMiddleware, ::sagaMiddleware)
) {

    companion object {
        val instance by lazy {
            AppStore()
        }
    }
}