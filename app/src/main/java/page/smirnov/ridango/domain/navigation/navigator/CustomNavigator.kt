package page.smirnov.ridango.domain.navigation.navigator

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.terrakok.cicerone.Navigator

interface CustomNavigator : Navigator {

    var activity: FragmentActivity?
    var containerId: Int?

    val topFragment: Fragment?
        get() = activity?.supportFragmentManager?.fragments?.lastOrNull()

    fun back()

    fun showDialog(dialog: DialogFragment)
}
