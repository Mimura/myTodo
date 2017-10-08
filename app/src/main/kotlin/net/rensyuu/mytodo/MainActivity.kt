package net.rensyuu.mytodo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_activity_main)
        setTaskListFragment()
    }

    private fun setTaskListFragment() {
        val fragment = FragmentTaskList()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.run {
            add(R.id.content_frame, fragment)
            commit()
        }
    }
}


object GlobalStatus {
    var currentMode = ListMode.TODO

    enum class ListMode(val status: Int) {
        ALL(-1),
        TODO(0),
        DONE(1),
    }
}
