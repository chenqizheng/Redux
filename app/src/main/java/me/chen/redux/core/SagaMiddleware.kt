package me.chen.redux.core

fun <State> sagaMiddleware(
    state: () -> State,
    action: Action,
    dispatch: Dispatch,
    next: Middleware<State>
): Action {
    return Action("SAGA")
}