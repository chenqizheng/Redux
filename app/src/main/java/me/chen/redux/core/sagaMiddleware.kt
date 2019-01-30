package me.chen.redux.core

import kotlinx.coroutines.*
import me.chen.redux.rootSaga
import kotlin.reflect.KSuspendFunction0

fun <State> sagaMiddleware(
    state: () -> State,
    action: Action,
    dispatch: Dispatch,
    next: Middleware<State>
): Action {
    Saga.dispatch = dispatch
    GlobalScope.launch(Dispatchers.Main) {
        rootSaga();
    }
    if (Saga.emitter[action.type] != null) {
        GlobalScope.launch(Dispatchers.Main) {
            Saga.emitter[action.type]?.invoke();
        }
    }
    return action
}


class Saga {
    companion object INSTANCE {
        val emitter: HashMap<String, KSuspendFunction0<Unit>> = HashMap();
        lateinit var dispatch: Dispatch
        fun put(type: String, map: HashMap<String, Any>?) {
            dispatch(Action(type))
        }

        fun take(type: String, block: KSuspendFunction0<Unit>) {
            emitter.put(type, block)
        }
    }
}

fun launch(block: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch {
        block
    }
}