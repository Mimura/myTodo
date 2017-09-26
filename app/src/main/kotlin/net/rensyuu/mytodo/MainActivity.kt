package net.rensyuu.mytodo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import net.rensyuu.mytodo.model.Task

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createList()
    }

    private fun createList() {
        val adapter = TaskListAdapter(applicationContext)
        adapter.tasks = listOf(Task(1, "テストタスクだよ", 0))
        val taskList = findViewById(R.id.task_list) as ListView
        taskList.adapter = adapter
    }
}
