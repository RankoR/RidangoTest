package page.smirnov.ridango.domain.core.interactor


interface IsDebug {

    fun exec(): Boolean
}

internal class IsDebugImpl : IsDebug {

    override fun exec(): Boolean {
        return page.smirnov.ridango.BuildConfig.DEBUG
    }
}
