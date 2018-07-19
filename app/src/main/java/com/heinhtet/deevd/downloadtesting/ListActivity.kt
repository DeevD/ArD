package com.heinhtet.deevd.downloadtesting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.arialyy.annotations.Download
import com.arialyy.aria.core.download.DownloadTask

/**
 * Created by Hein Htet on 7/19/18.

 */

class ListActivity() : AppCompatActivity() {

    val TAG = "ListActi"
    @BindView(R.id.rv)
    lateinit var rv: RecyclerView

    private lateinit var anyRunnModule: AnyRunnModule

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anyRunnModule = AnyRunnModule(this)
        setContentView(R.layout.rv_layout)
        ButterKnife.bind(this)

        adapter = Adapter()
        var list = anyRunnModule.getAllList()
        adapter.setlist(list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    @Download.onTaskRunning
    fun onTaskRunning(task: DownloadTask) {
        val fileSize = task.fileSize
        //Get the file size after conversion
        val fileSize1 = task.convertFileSize
        //The current percentage of progress
        val percent = task.percent
        Log.i(TAG, "ontask runningpercent $percent fileSize $fileSize convertfileSize $fileSize1")
        adapter.setProgress(task.entity )

    }
}