package pnj.exam.fileinternal

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.Locale

class NoteAdapter(
    private val fileList: MutableList<File>,
    private val deleteListener: (File) -> Unit
) : RecyclerView.Adapter<NoteAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = fileList[position]
        holder.bind(file)
    }

    override fun getItemCount(): Int = fileList.size

    fun getFileAtPosition(position: Int): File = fileList[position]

    fun removeFile(file: File) {
        val position = fileList.indexOf(file)
        if (position != -1) {
            fileList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileName: TextView = itemView.findViewById(R.id.txtFileName)
        private val lastModified: TextView = itemView.findViewById(R.id.txtLastModified)

        fun bind(file: File) {
            fileName.text = file.name
            lastModified.text = formatDate(file.lastModified())

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailNote::class.java).apply {
                    putExtra("FILE_NAME", file.name)
                    putExtra("FILE_PATH", file.absolutePath)
                }
                context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                deleteListener.invoke(file)
                true
            }
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            return sdf.format(calendar.time)
        }
    }
}
