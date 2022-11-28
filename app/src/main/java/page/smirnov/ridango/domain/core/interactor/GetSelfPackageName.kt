package page.smirnov.ridango.domain.core.interactor

import android.content.Context

interface GetSelfPackageName {

    fun exec(): String
}

internal class GetSelfPackageNameImpl(
    private val context: Context,
) : GetSelfPackageName {

    override fun exec(): String {
        return context.packageName
    }
}
