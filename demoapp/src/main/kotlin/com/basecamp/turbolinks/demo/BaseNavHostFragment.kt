package com.basecamp.turbolinks.demo

import android.app.Activity
import androidx.fragment.app.Fragment
import com.basecamp.turbolinks.BuildConfig
import com.basecamp.turbolinks.config.PathConfiguration
import com.basecamp.turbolinks.nav.TurbolinksNavHostFragment
import kotlin.reflect.KClass

abstract class BaseNavHostFragment : TurbolinksNavHostFragment() {
    override val registeredActivities: List<KClass<out Activity>>
        get() = listOf()

    override val registeredFragments: List<KClass<out Fragment>>
        get() = listOf(
            WebFragment::class,
            WebHomeFragment::class,
            WebModalFragment::class,
            ProfileFragment::class,
            ImageViewerFragment::class
        )

    override val pathConfigurationLocation: PathConfiguration.Location
        get() = PathConfiguration.Location(
            assetFilePath = "json/configuration.json"
        )

    override fun onTurbolinksCreated() {
        super.onTurbolinksCreated()
        turbolinks.setDebugLoggingEnabled(BuildConfig.DEBUG)
    }
}
