package me.chen.redux

import me.chen.redux.core.Action

object Clear : Action("Clear")
object Add : Action("Add")
object Sub : Action("Sub")
object INCREMENT_ASYN : Action("INCREMENT_ASYNC")

fun calculate(state: AppState, action: Action): AppState {
    var newState = when (action) {
        is Clear -> state.copy(count = 0)
        is Add -> state.copy(count = state.count + 1)
        is Sub -> state.copy(count = state.count - 1)
        else -> state
    }
    return newState
}