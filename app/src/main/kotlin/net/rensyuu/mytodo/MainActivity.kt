package net.rensyuu.mytodo

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import net.rensyuu.mytodo.GlobalStatus.ListMode
import net.rensyuu.mytodo.Helper.MyDatabaseOpenHelper.TaskTableParam
import net.rensyuu.mytodo.Helper.TaskParser
import net.rensyuu.mytodo.Helper.database
import net.rensyuu.mytodo.view.TaskListItemView
import org.jetbrains.anko.contentView
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

class MainActivity : FragmentTextInput.MyListener, AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createList()
        initFloatButton()
    }

    private fun createList() {
        val taskList = findViewById(R.id.task_list) as ListView
        taskList.adapter = adapter
        taskList.isClickable = true
        updateList()

        taskList.setOnItemClickListener { _, view, i, _ ->
            onTapListItem(view, i)
        }
    }

    private fun onTapListItem(itemView: View, positionI: Int) {
        val item = itemView as TaskListItemView
        val task = item.task
        task?.run {
            val newStatus = when (status) {
                ListMode.TODO.status -> {
                    GlobalStatus.ListMode.DONE.status
                }
                ListMode.DONE.status -> {
                    GlobalStatus.ListMode.TODO.status
                }
                else -> {
                    0
                }
            }
            database.use {
                update(TaskTableParam.TABLE_NAME, TaskTableParam.STATUS to newStatus)
                        .whereArgs("${TaskTableParam.ID} = ${id}")
                        .exec()
            }
        }
        updateList()
    }

    private fun initFloatButton() {
        fab.setOnClickListener {
            openTextInput()
            fab.hide()
            fab.isClickable = false
        }
    }


    private fun addTask(name: String, status: Int, folderId: Int) {
        database.use {
            insert(
                    TaskTableParam.TABLE_NAME,
                    TaskTableParam.NAME to name,
                    TaskTableParam.STATUS to status,
                    TaskTableParam.FOLDER_ID to folderId
            )
        }
    }

    private fun updateList() {
        val allStr = "0 or 1"
        var statusStr = allStr//All
        when (GlobalStatus.currentMode) {
            GlobalStatus.ListMode.TODO -> {
                statusStr = GlobalStatus.ListMode.TODO.status.toString()
            }
            GlobalStatus.ListMode.DONE -> {
                statusStr = GlobalStatus.ListMode.DONE.status.toString()
            }
        }

        adapter.tasks = database.readableDatabase.select(TaskTableParam.TABLE_NAME)
                .whereArgs("${TaskTableParam.STATUS} = $statusStr")
                .parseList(TaskParser())
        adapter.notifyDataSetChanged()
    }

    private fun openTextInput() {
        fragment = FragmentTextInput()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.run {
            add(R.id.adding_view, fragment)
            commit()
        }
    }

    private fun hideKeyBoard() {
        //キーボード表示を制御するためのオブジェクト
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        inputMethodManager.hideSoftInputFromWindow(contentView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private fun removeInputFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.run {
            remove(fragment)
            commit()
        }
        fab.show()
        fab.isClickable = true
    }

    override fun onClickOk(text: String) {
        addTask(text, 0, 0)
        updateList()
        removeInputFragment()
        hideKeyBoard()
    }

    override fun onClickCancel(text: String) {
        removeInputFragment()
        hideKeyBoard()
    }

    private val adapter: TaskListAdapter by lazy { TaskListAdapter(applicationContext) }
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) as FloatingActionButton }
    private var fragment: Fragment? = null

}

object GlobalStatus {
    var currentMode = ListMode.ALL

    enum class ListMode(val status: Int) {
        ALL(-1),
        TODO(0),
        DONE(1),
    }
}
