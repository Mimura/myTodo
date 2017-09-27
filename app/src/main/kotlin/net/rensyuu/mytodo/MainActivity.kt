package net.rensyuu.mytodo

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import net.rensyuu.mytodo.Helper.database

class MainActivity : AppCompatActivity() {

    private val adapter: TaskListAdapter by lazy { TaskListAdapter(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createList()
        initFloatButton()
    }

    private fun createList() {
//        val db = DBOpenHelper.getInstance(this)
//        db.readableDatabase.select(DBOpenHelper.tableName)
        val taskList = findViewById(R.id.task_list) as ListView
        taskList.adapter = adapter
    }

    private fun initFloatButton() {
        val fab: FloatingActionButton = findViewById(R.id.fab) as FloatingActionButton;
        fab.setOnClickListener {
            val a = database
            a.use { }
            a.readableDatabase
            adapter.notifyDataSetChanged()
        }
    }

}
