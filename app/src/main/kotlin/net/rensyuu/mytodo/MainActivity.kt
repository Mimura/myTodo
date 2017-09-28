package net.rensyuu.mytodo

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import net.rensyuu.mytodo.Helper.MyDatabaseOpenHelper.TaskTableParam
import net.rensyuu.mytodo.Helper.TaskParser
import net.rensyuu.mytodo.Helper.database
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class MainActivity : AppCompatActivity() {

    private val adapter: TaskListAdapter by lazy { TaskListAdapter(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createList()
        initFloatButton()
    }

    private fun createList() {
        adapter.tasks = database.readableDatabase.select(TaskTableParam.TABLE_NAME).parseList(TaskParser())
        val taskList = findViewById(R.id.task_list) as ListView
        taskList.adapter = adapter
    }

    private fun initFloatButton() {
        val fab: FloatingActionButton = findViewById(R.id.fab) as FloatingActionButton;
        fab.setOnClickListener {

            database.use {
                insert(
                        TaskTableParam.TABLE_NAME,
                        TaskTableParam.ID to 0,
                        TaskTableParam.NAME to "テスト",
                        TaskTableParam.STATUS to 0,
                        TaskTableParam.FOLDER_ID to 1
                )
            }
            adapter.tasks = database.readableDatabase.select(TaskTableParam.TABLE_NAME).parseList(TaskParser())
            adapter.notifyDataSetChanged()
        }
    }

}
