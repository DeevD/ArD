package com.heinhtet.deevd.downloadtesting

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.arialyy.annotations.Download
import com.arialyy.annotations.DownloadGroup
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadGroupTask
import com.arialyy.aria.core.download.DownloadTask
import com.arialyy.aria.util.ALog
import com.heinhtet.deevd.downloadtesting.R.id.list
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    @Download.onTaskRunning
    fun onTaskRunning(task: DownloadTask) {
        //Get the file size
        val fileSize = task.fileSize
        //Get the file size after conversion
        val fileSize1 = task.convertFileSize
        //The current percentage of progress
        val percent = task.percent
       // result.text = task.currentProgress.toString() + "/" + fileSize1

        Log.i(TAG, "ontask runningpercent $percent fileSize $fileSize convertfileSize $fileSize1")
    }

    @Download.onWait
    fun taskWait(task: DownloadTask) {
        Log.d(TAG, "wait ==> " + task.downloadEntity.fileName)
    }


    @Download.onTaskStart
    fun taskStart(task: DownloadTask) {
        Log.i(TAG, "task start ${task.entity.percent}")
    }

    @Download.onTaskResume
    fun taskResume(task: DownloadTask) {
        Log.i(TAG, "task resumt ${task.entity.percent}")

    }

    @Download.onTaskStop
    fun taskStop(task: DownloadTask) {
        Log.i(TAG, "task stop ${task.entity.percent}")

    }

    @Download.onTaskCancel
    fun taskCancel(task: DownloadTask) {
        Log.i(TAG, "task cancel ${task.entity.percent}")

    }

    @Download.onTaskFail
    fun taskFail(task: DownloadTask) {
        Log.i(TAG, "task fail ${task.entity.percent}")

    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        Log.i(TAG, "task complete ${task.entity.percent}")

    }

    //############################### 任务组 ##############################
    @DownloadGroup.onTaskComplete
    fun groupTaskComplete(task: DownloadGroupTask) {
        Log.i(TAG, "task start ${task.entity.percent}")

    }

    @DownloadGroup.onTaskStart
    fun groupTaskStart(task: DownloadGroupTask) {
    }

    @DownloadGroup.onTaskResume
    fun groupTaskResume(task: DownloadGroupTask) {
    }

    @DownloadGroup.onWait
    fun groupTaskWait(task: DownloadGroupTask) {
        ALog.d(TAG, String.format("group【%s】wait---", task.taskName))
    }

    @DownloadGroup.onTaskStop
    fun groupTaskStop(task: DownloadGroupTask) {
        Log.d(TAG, String.format("group【%s】stop", task.taskName))
    }

    @DownloadGroup.onTaskCancel
    fun groupTaskCancel(task: DownloadGroupTask) {
    }

    @DownloadGroup.onTaskFail
    fun groupTaskFail(task: DownloadGroupTask) {
        Log.d(TAG, String.format("group【%s】fail", task.taskName))
    }

    @DownloadGroup.onTaskComplete
    fun taskComplete(task: DownloadGroupTask) {
    }

    private lateinit var downloadBtn: Button
    private lateinit var result: TextView
    private val TAG = "MainActivity"
    private lateinit var anyRunnModule: AnyRunnModule
    private val DOWNLOAD_URL = "http://hzdown.muzhiwan.com/2017/05/08/nl.noio.kingdom_59104935e56f0.apk"
    private val LINK_1 = "https://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_30mb.mp4"
    private val LINK_2 = "https://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_20mb.mp4"
    private val LINK_3 = "http://www.mahartv.com/downloads/content/15320122327654?user_id=351318"
    private val L = "http://www.mahartv.com/downloads/content/15320121475378?user_id=351318"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anyRunnModule = AnyRunnModule(this)
        setContentView(R.layout.activity_main)

        task_1.setOnClickListener { anyRunnModule.start(DOWNLOAD_URL,"task1.mp4") }
        task_2.setOnClickListener { anyRunnModule.start(L,"2.mp4") }
        task_3.setOnClickListener { anyRunnModule.start(LINK_3,"34.mp4") }
        deleteAll.setOnClickListener { Aria.download(this).removeAllTask(true) }

        list.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListActivity::class.java))
        }
        Log.i("MainActivity ", "downloadListTask" + anyRunnModule.getAllList().size)
    }

    override fun onDestroy() {
        super.onDestroy()
        anyRunnModule.unRegister()
    }

}
