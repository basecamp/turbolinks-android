package com.basecamp.turbolinks.delegates

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.basecamp.turbolinks.fragments.TurbolinksFragmentViewModel
import com.basecamp.turbolinks.nav.TurbolinksNavDestination
import com.basecamp.turbolinks.session.TurbolinksSessionModalResult
import com.basecamp.turbolinks.session.TurbolinksSessionViewModel
import com.basecamp.turbolinks.nav.TurbolinksNavigator
import com.basecamp.turbolinks.util.logEvent

class TurbolinksFragmentDelegate(private val navDestination: TurbolinksNavDestination) {
    private val fragment = navDestination.fragment
    private val location = navDestination.location
    private val sessionName = navDestination.sessionNavHostFragment.sessionName

    internal val sessionViewModel = TurbolinksSessionViewModel.get(sessionName, fragment.requireActivity())
    internal val pageViewModel = TurbolinksFragmentViewModel.get(location, fragment)

    internal lateinit var navigator: TurbolinksNavigator

    fun onActivityCreated() {
        navigator = TurbolinksNavigator(navDestination)

        initToolbar()
        logEvent("fragment.onActivityCreated", "location" to location)
    }

    fun onStart() {
        logEvent("fragment.onStart", "location" to location)
    }

    fun onStop() {
        logEvent("fragment.onStop", "location" to location)
    }

    fun onStartAfterDialogCancel() {
        logEvent("fragment.onStartAfterDialogCancel", "location" to location)
    }

    fun onStartAfterModalResult(result: TurbolinksSessionModalResult) {
        logEvent("fragment.onStartAfterModalResult", "location" to result.location, "options" to result.options)
        if (result.shouldNavigate) {
            navigator.navigate(result.location, result.options, result.bundle)
        }
    }

    fun onDialogCancel() {
        logEvent("fragment.onDialogCancel", "location" to location)
        if (!sessionViewModel.modalResultExists) {
            sessionViewModel.sendDialogResult()
        }
    }

    // ----------------------------------------------------------------------------
    // Private
    // ----------------------------------------------------------------------------

    private fun initToolbar() {
        navDestination.toolbarForNavigation()?.let {
            NavigationUI.setupWithNavController(it, fragment.findNavController())
            it.setNavigationOnClickListener { navDestination.navigateUp() }
        }
    }

    private fun logEvent(event: String, vararg params: Pair<String, Any>) {
        val attributes = params.toMutableList().apply {
            add(0, "session" to sessionName)
            add("fragment" to fragment.javaClass.simpleName)
        }
        logEvent(event, attributes)
    }
}