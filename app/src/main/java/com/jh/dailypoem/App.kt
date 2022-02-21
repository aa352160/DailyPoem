package com.jh.dailypoem

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

class App: Application() {
    companion object{
        private val activityList = Stack<Activity>()

        fun getCurrentActivity() = if (!activityList.empty()) activityList.peek()!! else null
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityList.add(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                activityList.remove(activity)
            }
        })
    }
}