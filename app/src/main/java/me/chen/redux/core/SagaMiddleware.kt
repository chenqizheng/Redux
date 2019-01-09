package me.chen.redux.core

fun <State> sagaMiddleware(
    state: () -> State,
    action: Action,
    dispatch: Dispatch,
    next: Middleware<State>
): Action {
    Channel
    return Action("SAGA")
}