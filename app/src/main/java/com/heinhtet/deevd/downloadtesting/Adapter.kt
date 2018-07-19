package com.heinhtet.deevd.downloadtesting

import android.annotation.SuppressLint
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.inf.AbsEntity
import com.arialyy.aria.core.inf.IEntity
import com.arialyy.aria.util.CommonUtil
import org.w3c.dom.Text
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Hein Htet on 7/19/18.
 */
class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {
    val TAG = "Adapter"
    private val mPositions = ConcurrentHashMap<String, Int>()

    private var list = ArrayList<AbsEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_items, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            Log.i(TAG, " Play load nov n  ")

            holder.onBind(list[position])
        } else {
            Log.i(TAG, " Play load contains ")
            holder.onBind(list[position], payloads)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = list[position]
        holder.onBind(item)

    }

    fun setlist(list: List<AbsEntity>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    @Synchronized
    fun setProgress(entity: AbsEntity) {
        var position = indexItem(entity.key)
        list[position] = entity
        notifyItemChanged(position, entity)
    }

    @Synchronized
    private fun indexItem(url: String): Int {
        list.forEachIndexed { index, absEntity ->
            if ((absEntity as DownloadEntity).key == url) {
                return index
            }
        }
        return -1
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.name_tv)
        lateinit var nameTv: TextView
        @BindView(R.id.progress)
        lateinit var progressBar: ProgressBar
        @BindView(R.id.action)
        lateinit var action: Button
        @BindView(R.id.size)
        lateinit var sizeTv: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun onBind(item: AbsEntity) {
            var str = ""
            var json = (item as DownloadEntity).str
            Log.i(TAG, " json ${json.toString()}")
            val size = item.getFileSize()
            val progress = item.getCurrentProgress()
            val current = if (size == 0L) 0 else (progress * 100 / size).toInt()
            sizeTv.text = size.toString()
            progressBar.progress = current
            when (item.getState()) {
                IEntity.STATE_WAIT, IEntity.STATE_OTHER, IEntity.STATE_FAIL -> str = "开始"
                IEntity.STATE_STOP -> {
                    str = "Resume"

                }
                IEntity.STATE_PRE, IEntity.STATE_POST_PRE, IEntity.STATE_RUNNING -> {
                    str = "Downloding"
                }
                IEntity.STATE_COMPLETE -> {
                    str = "Complete"
                }
            }
            nameTv.text = item.convertSpeed
            action.text = str
            action.setOnClickListener {
                clickHandle(item)
            }
            updateSpeed(item)

        }

        private fun clickHandle(entity: AbsEntity){
            when(entity.state){
                IEntity.STATE_RUNNING->{
                    Aria.download(this).load(list[adapterPosition].key).stop()

                }
                IEntity.STATE_STOP->{
                    Aria.download(this).load(list[adapterPosition].key).start()

                }
            }
        }

        fun onBind(item: AbsEntity, payloads: MutableList<Any>) {
            val entity = payloads[0] as AbsEntity
            updateSpeed(entity)
        }


        @SuppressLint("SetTextI18n")
        private fun updateSpeed(entity: AbsEntity) {
            val size = entity.fileSize
            val progress = entity.currentProgress
            val current = if (size == 0L) 0 else (progress * 100 / size).toInt()
            nameTv.text = entity.convertSpeed
            sizeTv.text = covertCurrentSize(progress) + "/" + CommonUtil.formatFileSize(size.toDouble())
            progressBar.progress = current

        }

        private fun covertCurrentSize(currentSize: Long): String {
            return if (currentSize < 0) "0" else CommonUtil.formatFileSize(currentSize.toDouble())
        }


    }
}