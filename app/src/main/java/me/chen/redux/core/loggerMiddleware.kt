package me.chen.redux.core

import android.util.Log


fun <State> loggerMiddleware(
    state: () -> State,
    action: Action,
    dispatch: Dispatch,
    next: Middleware<State>
): Action {
    Log.d("middleware", "action in <-- ${action.type} , state = ${state.invoke()}")
    val newAction = next(state, action, dispatch)
    Log.d("middleware", "action out --> ${newAction.type} , state = ${state.invoke()} ")
    return newAction
}