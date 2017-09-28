package net.rensyuu.mytodo

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import net.rensyuu.mytodo.GlobalStatus.ListMode
import net.rensyuu.mytodo.Helper.MyDatabaseOpenHelper.TaskTableParam
import net.rensyuu.mytodo.Helper.TaskParser
import net.rensyuu.mytodo.Helper.database
import net.rensyuu.mytodo.view.TaskListItemView
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

class FragmentTaskList : FragmentTextInput.MyListener, Fragment() {

    companion object {
        fun newInstance(target: Fragment, requestCode: Int): FragmentTaskList {
            val fragment = FragmentTaskList()
            fragment.setTargetFragment(target, requestCode)

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater!!.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList()
        initFloatButton()
    }

    private fun createList() {
        val taskList = view?.findViewById<ListView>(R.id.task_list)
        taskList?.adapter = adapter
        taskList?.isClickable = true
        updateList()

        taskList?.setOnItemClickListener { _, view, i, _ ->
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
            context.database.use {
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
        context.database.use {
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

        adapter.tasks = context.database.readableDatabase.select(TaskTableParam.TABLE_NAME)
                .whereArgs("${TaskTableParam.STATUS} = $statusStr")
                .parseList(TaskParser())
        adapter.notifyDataSetChanged()
    }

    private fun openTextInput() {
        fragment = FragmentTextInput.newInstance(this, 0)
        val transaction = fragmentManager.beginTransaction()
        transaction.run {
            add(R.id.adding_view, fragment)
            commit()
        }
    }

    private fun hideKeyBoard() {
        //キーボード表示を制御するためのオブジェクト
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun removeInputFragment() {
        val transaction = fragmentManager.beginTransaction()
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

    private val adapter: TaskListAdapter by lazy { TaskListAdapter(context) }
    private val fab: FloatingActionButton by lazy { view!!.findViewById<FloatingActionButton>(R.id.fab) }
    private var fragment: Fragment? = null

}

