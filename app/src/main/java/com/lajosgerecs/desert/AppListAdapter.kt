// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.lajosgerecs.desert

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lajosgerecs.desert.R
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class AppListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context? = null;

    fun updateAppList() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_app,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = AppListCache.items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val items = AppListCache.items
        val appItem = items[position]

        holder.itemView.setOnClickListener({

            val i = this.context!!.packageManager.getLaunchIntentForPackage(appItem.packageName)
            if (i != null) {
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                this.context!!.startActivity(i!!)
            } else {
                Toast.makeText(
                    this.context,
                    "Can not start application: " + appItem.name,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        holder.itemView.apply {
            findViewById<TextView>(R.id.app_name).text = appItem.name
        }
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)