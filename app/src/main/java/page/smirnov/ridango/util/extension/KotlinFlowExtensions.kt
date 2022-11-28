package page.smirnov.ridango.util.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import timber.log.Timber

inline fun <T> Flow<T>.onFailureLog(crossinline lazyMessage: (() -> Any) = { "" }): Flow<T> {
    return catch { t ->
        val message = lazyMessage.invoke().toString().takeIf { it.isNotBlank() }

        Timber.e(t, message)

        throw t
    }
}

inline fun <T> Flow<T>.onFailureLogAndIgnore(crossinline lazyMessage: (() -> Any) = { "" }): Flow<T> {
    return catch { t ->
        val message = lazyMessage.invoke().toString().takeIf { it.isNotBlank() }

        Timber.e(t, message)
    }
}

inline fun Flow<Unit>.onFailureLogAndEmitUnit(crossinline lazyMessage: (() -> Any) = { "" }): Flow<Unit> {
    return catch { t ->
        val message = lazyMessage.invoke().toString().takeIf { it.isNotBlank() }

        Timber.e(t, message)
    }.onEmpty { emit(Unit) }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flow<T>.ignoreValue(): Flow<Unit> {
    return this.map { }
}

fun unitFlow(block: suspend FlowCollector<Unit>.() -> Unit): Flow<Unit> {
    return flow {
        block()
        emit(Unit)
    }
}
