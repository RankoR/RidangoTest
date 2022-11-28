package page.smirnov.ridango.presentation.core_ui

import androidx.annotation.StringRes

sealed class ScreenState {
    object Content : ScreenState()
    object Loading : ScreenState()

    data class Error(
        @StringRes
        val errorResId: Int? = null,
        val errorText: String? = null,
    ) : ScreenState()
}
