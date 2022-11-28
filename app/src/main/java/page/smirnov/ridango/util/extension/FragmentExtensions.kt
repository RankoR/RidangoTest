package page.smirnov.ridango.util.extension

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    activity?.window?.let { window ->
        view?.let { view ->
            WindowCompat
                .getInsetsController(window, view)
                .hide(WindowInsetsCompat.Type.ime())
        }
    }
}
