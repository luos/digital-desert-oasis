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

import android.content.pm.ApplicationInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class AppListActivity : AppCompatActivity() {
    private val appListAdapter = AppListAdapter()
    private var showAllApps = false
    lateinit var recycler: RecyclerView
    lateinit var loading: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()!!.hide()
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            adapter = appListAdapter
            recycler = this
        }

        appListAdapter.updateAppList()
        val self = this
//        runOnUiThread{
//            Toast.makeText(self.baseContext , "setting data " + appListAdapter.items.size + " " + AppListCache.items.size, Toast.LENGTH_SHORT).show()
//        }


        loading = findViewById(R.id.loading)

        findViewById<View>(R.id.filter).setOnClickListener {
            if (findViewById<RecyclerView>(R.id.recycler).visibility == View.VISIBLE) {
                showAllApps = !showAllApps
                refreshData()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        recycler.visibility = View.INVISIBLE
        loading.visibility = View.VISIBLE

        val context = this

        bgScope.launch {
            mainScope.launch {
                recycler.visibility = View.VISIBLE
                loading.visibility = View.INVISIBLE
            }
        }
    }
}
