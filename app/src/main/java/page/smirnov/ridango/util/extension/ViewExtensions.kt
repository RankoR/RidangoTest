package page.smirnov.ridango.util.extension

import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

val EditText.textFlow: Flow<String>
    get() {
        return callbackFlow {
            val textWatcher = addTextChangedListener { editable ->
                editable?.toString()?.let(::trySend)
            }

            awaitClose {
                removeTextChangedListener(textWatcher)
            }
        }
    }
