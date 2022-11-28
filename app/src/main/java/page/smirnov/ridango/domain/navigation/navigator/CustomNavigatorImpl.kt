package page.smirnov.ridango.domain.navigation.navigator

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.Back
import com.github.terrakok.cicerone.BackTo
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Forward
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import java.lang.ref.WeakReference

open class CustomNavigatorImpl : CustomNavigator {

    override var activity: FragmentActivity?
        get() = activityWeakRef?.get()
        set(value) {
            activityWeakRef = WeakReference(value)

            fragmentManager = WeakReference(value?.supportFragmentManager)
            fragmentFactory = WeakReference(fragmentManager?.get()?.fragmentFactory)
        }

    override var containerId: Int? = null

    private var activityWeakRef: WeakReference<FragmentActivity>? = null

    private val localStackCopy = mutableListOf<String>()

    private var fragmentManager: WeakReference<FragmentManager>? = null
        get() {
            return field?.takeIf { it.get()?.isDestroyed == false && it.get()?.isStateSaved == false }
        }

    private var fragmentFactory: WeakReference<FragmentFactory>? = null

    override fun applyCommands(commands: Array<out Command>) {
        fragmentManager?.get()?.executePendingTransactions()

        // copy stack before apply commands
        copyStackToLocal()

        for (command in commands) {
            try {
                applyCommand(command)
            } catch (e: RuntimeException) {
                errorOnApplyCommand(command, e)
            }
        }
    }

    override fun back() {
        if (localStackCopy.isNotEmpty()) {
            fragmentManager
                ?.get()
                ?.popBackStack()
                ?.also {
                    localStackCopy.removeLast()
                }
        } else {
            activityBack()
        }
    }

    override fun showDialog(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    private fun copyStackToLocal() {
        val fragmentManager = this.fragmentManager?.get() ?: return

        localStackCopy.clear()
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.getBackStackEntryAt(i).name?.let(localStackCopy::add)
        }
    }

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    private fun applyCommand(command: Command) {
        when (command) {
            is Forward -> forward(command)
            is Replace -> replace(command)
            is BackTo -> backTo(command)
            is Back -> back()
        }
    }

    private fun forward(command: Forward) {
        when (val screen = command.screen) {
            is ActivityScreen -> {
                checkAndStartActivity(screen)
            }

            is FragmentScreen -> {
                commitNewFragmentScreen(screen, true)
            }
        }
    }

    private fun replace(command: Replace) {
        when (val screen = command.screen) {
            is ActivityScreen -> {
                checkAndStartActivity(screen)
                activity?.finish()
            }

            is FragmentScreen -> {
                if (localStackCopy.isNotEmpty()) {
                    fragmentManager
                        ?.get()
                        ?.popBackStack()
                        ?.also {
                            localStackCopy.removeLast()
                        }

                    commitNewFragmentScreen(screen, true)
                } else {
                    commitNewFragmentScreen(screen, false)
                }
            }
        }
    }

    /**
     * Performs [BackTo] command transition
     */
    private fun backTo(command: BackTo) {
        val screen = command.screen ?: run {
            backToRoot()
            return
        }

        val screenKey = screen.screenKey
        val index = localStackCopy.indexOfFirst { it == screenKey }
        if (index != -1) {
            val forRemove = localStackCopy.subList(index, localStackCopy.size)
            fragmentManager
                ?.get()
                ?.popBackStack(forRemove.first().toString(), 0)
                ?.also {
                    forRemove.clear()
                }
        } else {
            backToUnexisting(screen)
        }
    }

    /**
     * Called when we tried to fragmentBack to some specific screen (via [BackTo] command),
     * but didn't find it.
     *
     * @param screen screen
     */
    private fun backToUnexisting(screen: Screen) {
        backToRoot()
    }

    private fun backToRoot() {
        fragmentManager
            ?.get()
            ?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ?.also { localStackCopy.clear() }
    }

    private fun activityBack() {
        activity?.finish()
    }

    private fun commitNewFragmentScreen(
        screen: FragmentScreen,
        addToBackStack: Boolean
    ) {
        val fragmentFactory = fragmentFactory?.get() ?: return
        val fragmentManager = fragmentManager?.get()?.takeIf { !it.isStateSaved } ?: return
        val containerId = containerId ?: return

        val fragment = screen.createFragment(fragmentFactory)

        fragmentManager
            .beginTransaction()
            .apply {
                setReorderingAllowed(true)
            }
            .let { fragmentTransaction ->

                setupFragmentTransaction(
                    screen,
                    fragmentTransaction,
                    fragmentManager.findFragmentById(containerId),
                    fragment
                )

                when (screen.clearContainer) {
                    true -> fragmentTransaction.replace(containerId, fragment, screen.screenKey)
                    false -> fragmentTransaction.add(containerId, fragment, screen.screenKey)
                }

                if (addToBackStack) {
                    fragmentTransaction.addToBackStack(screen.screenKey)
                    localStackCopy.add(screen.screenKey)
                }

                fragmentTransaction.commitAllowingStateLoss()
            }
    }

    /**
     * Override this method to set up fragment transaction [FragmentTransaction].
     * For example: setCustomAnimations(...), addSharedElement(...) or setReorderingAllowed(...)
     */
    private fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        // Override if necessary
    }

    private fun checkAndStartActivity(screen: ActivityScreen) {
        val activity = activity ?: return

        val activityIntent = screen.createIntent(activity)
        try {
            activity.startActivity(activityIntent, screen.startActivityOptions)
        } catch (e: ActivityNotFoundException) {
            unexistingActivity(screen, activityIntent)
        }
    }

    /**
     * Called when there is no activity to open `screenKey`.
     *
     * @param screen         screen
     * @param activityIntent intent passed to start Activity for the `screenKey`
     */
    private fun unexistingActivity(screen: ActivityScreen, activityIntent: Intent) {
        // Do nothing by default
    }

    private fun errorOnApplyCommand(command: Command, error: RuntimeException) {
        throw error
    }
}
