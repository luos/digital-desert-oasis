package com.lajosgerecs.desert

import android.content.Context
import android.content.pm.ApplicationInfo

object AppListCache {
    var items: ArrayList<AppItem> = ArrayList();
    var showAllApps = false
    var calculating = false

    fun set(context: Context, newItems: List<ApplicationInfo>) {
        items.clear()
        val comparator = Comparator { a: AppItem, b: AppItem ->
            a.name.toLowerCase().compareTo(b.name.toLowerCase())
        }


        items.addAll(newItems.map {
            val item = AppItem(
                context.packageManager.getApplicationLabel(it.packageName),
                it.packageName
            )
            item
        })
        items.sortWith(comparator)
    }

    fun get(): ArrayList<AppItem> {
        return items;
    }

    fun update(context: Context) {
        val apps = if (showAllApps) {
            context.packageManager.getAllPackages(context)
        } else {
            context.packageManager.getLaunchablePackages(context)
        }
        set(context, apps)
    }

}