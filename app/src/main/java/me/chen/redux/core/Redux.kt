package me.chen.redux.core

open class Action(val type: String)

typealias Dispatch = (Action) -> Unit

typealias Reducer<State> = (State, Action) -> State

typealias Subscription<State> = (State, Dispatch) -> Unit

typealias UnSubscribe = () -> Unit

typealias Middleware<State> = (() -> State, Action, Dispatch) -> Action

typealias MiddlewareChain<State> = (() -> State, Action, Dispatch, Middleware<State>) -> Action

interface IStore<State> {
    fun getState(): State;
    fun dispatch(action: Action)
    fun subscription(subscription: Subscription<State>): UnSubscribe
}

abstract class Store<State>(
    initialState: State,
    val reducers: List<Reducer<State>>,
    val middlewares: List<MiddlewareChain<State>>
) : IStore<State> {

    private var currentState: State = initialState
    private val subscriptions = mutableListOf<Subscription<State>>()

    override fun getState(): State {
        return currentState
    }

    override fun dispatch(action: Action) {
        applyMiddleware(currentState, action) {
            val newState = applyReducers(currentState, action)
            currentState = newState
            subscriptions.forEach { it(currentState, ::dispatch) }
        }

    }

    fun applyReducers(currentState: State, action: Action): State {
        var newState = currentState
        reducers.forEach { newState = it(currentState, action) }
        return newState
    }

    fun applyMiddleware(state: State, action: Action, block: () -> Unit): Action =
        nextMiddlewareChain(0, block)(::getState, action, ::dispatch)

    fun nextMiddlewareChain(index: Int, block: () -> Unit): Middleware<State> {
        if (index == middlewares.size) {
            return { state, action, dispatch ->
                block()
                action
            }
        }
        return { state, action, dispatch ->
            middlewares[index].invoke(
                ::getState,
                action,
                dispatch,
                nextMiddlewareChain(index + 1, block)
            )
        }
    }

    override fun subscription(subscription: Subscription<State>): UnSubscribe {
        if (subscription in subscriptions) {
            return { subscriptions.remove(subscription) }
        }
        subscriptions.add(subscription)
        return { subscriptions.remove(subscription) }
    }
}