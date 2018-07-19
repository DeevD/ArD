package com.heinhtet.deevd.downloadtesting

import android.content.Context
import android.os.Environment
import android.util.Log
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.RequestEnum
import com.arialyy.aria.core.download.DownloadTask
import com.arialyy.aria.util.CommonUtil
import java.io.File
import com.arialyy.aria.core.inf.AbsEntity
import com.google.gson.Gson


/**
 * Created by Hein Htet on 7/19/18.
 */
class AnyRunnModule(var mContext: Context) {

    interface task {
        fun running(task: DownloadTask)
    }


    internal var TAG = "AnyRunnModule"
    private var mUrl: String? = null

    init {
        Aria.download(mContext).register()
    }

    @Download.onWait
    fun onWait(task: DownloadTask) {
        Log.d(TAG, "wait ==> " + task.downloadEntity.fileName)
    }

    @Download.onPre
    fun onPre(task: DownloadTask) {
        Log.d(TAG, "onPre")
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask) {
        Log.d(TAG, "onStart")
    }

    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        Log.d(TAG, "running")
    }

    @Download.onTaskResume
    fun taskResume(task: DownloadTask) {
        Log.d(TAG, "resume")
    }

    @Download.onTaskStop
    fun taskStop(task: DownloadTask) {
        Log.d(TAG, "stop")
    }

    @Download.onTaskCancel
    fun taskCancel(task: DownloadTask) {
        Log.d(TAG, "cancel")
    }

    @Download.onTaskFail
    fun taskFail(task: DownloadTask) {
        Log.d(TAG, "fail")
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        Log.i(TAG, "path ==> " + task.downloadEntity.getDownloadPath())
        Log.i(TAG, "md5Code ==> " + CommonUtil.getFileMD5(File(task.getDownloadPath())))
    }


    fun start(url: String,name: String) {
        mUrl = url
        var module = DModule("Task One ", " this is a task one")
        Aria.download(mContext)
                .load(url)
                .setFilePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "$name.mp4")
                .resetState()
                .setExtendField(Gson().toJson(module).toString())
                .start()
    }

    fun getAllList(): List<AbsEntity> {
        return Aria.download(mContext).totalTaskList
    }

    fun stop() {
        Aria.download(mContext).load(mUrl!!).stop()
    }

    fun cancel() {
        Aria.download(mContext).load(mUrl!!).cancel()
    }

    fun unRegister() {
        Aria.download(mContext).unRegister()
    }

    data class DModule(var name: String, var des: String)

}