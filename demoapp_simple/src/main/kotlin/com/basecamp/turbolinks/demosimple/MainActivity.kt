package com.basecamp.turbolinks.demosimple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.basecamp.turbolinks.PathConfiguration.Location
import com.basecamp.turbolinks.TurbolinksActivity
import com.basecamp.turbolinks.TurbolinksActivityDelegate
import com.basecamp.turbolinks.TurbolinksRouter
import com.basecamp.turbolinks.TurbolinksSession

class MainActivity : AppCompatActivity(), TurbolinksActivity {
    private val hostFragmentId = R.id.section_food_nav
    private val session by lazy { TurbolinksSession.getNew(this) }
    private val controller by lazy { findNavController(hostFragmentId) }
    private val view by lazy { layoutInflater.inflate(R.layout.activity_main, null) }
    private val router by lazy { Router(this) }
    private val delegate by lazy { TurbolinksActivityDelegate(this) }
    private val startLocation = Constants.FOOD_URL

    // ----------------------------------------------------------------------------
    // AppCompatActivity
    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(this)
        setContentView(view)
        initControllerGraph()
        initSession()
        verifyServerIpAddress(this)
    }

    // ----------------------------------------------------------------------------
    // TurbolinksActivity interface
    // ----------------------------------------------------------------------------

    override fun onProvideSession(fragment: Fragment): TurbolinksSession {
        return session
    }

    override fun onProvideRouter(): TurbolinksRouter {
        return router
    }

    override fun onProvideCurrentNavHostFragment(): NavHostFragment {
        return supportFragmentManager.findFragmentById(hostFragmentId) as? NavHostFragment
                ?: throw IllegalStateException("No current NavHostFragment found")
    }

    // ----------------------------------------------------------------------------
    // Private
    // ----------------------------------------------------------------------------

    private fun initControllerGraph() {
        // Dynamically set the controller graph and start destination,
        // so we can use a simplified navigation graph.
        delegate.startControllerGraph(
                controller = controller,
                startLocation = startLocation,
                navGraph = R.navigation.nav_graph,
                startDestination = R.id.food_fragment
        )
    }

    private fun initSession() {
        val location = Location(assetFilePath = "json/configuration.json")
        session.pathConfiguration.load(location)
        session.applyWebViewDefaults()
    }
}
