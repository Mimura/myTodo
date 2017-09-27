package net.rensyuu.mytodo

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import net.rensyuu.mytodo.model.Task

class MainActivity : AppCompatActivity() {

    private val adapter: TaskListAdapter by lazy { TaskListAdapter(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createList()
        initFloatButton()
    }

    private fun createList() {
        adapter.tasks = mutableListOf(Task(1, "テストタスクだよ", 0))
        val taskList = findViewById(R.id.task_list) as ListView
        taskList.adapter = adapter
    }

    private fun initFloatButton() {
        val fab: FloatingActionButton = findViewById(R.id.fab) as FloatingActionButton;
        fab.setOnClickListener {
            adapter.tasks.add(Task(2, "テストタスクだよ", 0))
            adapter.notifyDataSetChanged()
        }
    }

}
