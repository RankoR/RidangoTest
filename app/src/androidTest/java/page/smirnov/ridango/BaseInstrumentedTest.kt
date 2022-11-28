package page.smirnov.ridango

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry

abstract class BaseInstrumentedTest {

    protected val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext


    protected fun runOnUiThread(block: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(block)
    }
}
