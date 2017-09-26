package net.rensyuu.mytodo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import net.rensyuu.mytodo.model.Task
import net.rensyuu.mytodo.view.TaskListItemView

class TaskListAdapter(private val context:Context) : BaseAdapter(){
    var tasks: List<Task> = emptyList()

    override fun getCount(): Int {
        return tasks.size
    }

    override fun getItem(position: Int): Any? {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return (convertView as? TaskListItemView ?: TaskListItemView(context)).apply {
            SetTask(tasks[position])
        }
    }
}

