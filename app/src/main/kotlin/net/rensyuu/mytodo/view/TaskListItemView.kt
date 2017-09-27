package net.rensyuu.mytodo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.FrameLayout
import net.rensyuu.mytodo.R
import net.rensyuu.mytodo.bindview
import net.rensyuu.mytodo.model.Task

class TaskListItemView : FrameLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    //以下二つは基本的に使われないらしい
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
//    constructor(context:Context?,attrs:AttributeSet?,defStyleAttr:Int,defStyleRes:Int):super(context,attrs,defStyleAttr,defStyleRes)//API21から

    val taskCheckBox: CheckBox by bindview(R.id.task_check_box)

    init {
        LayoutInflater.from(context).inflate(R.layout.task_list_item, this)
    }

    fun SetTask(task: Task) {
        task.let {
            //taskCheckBox.setText(it.name)//androidStudioに文句を言われた！！
            //GetとSetが用意してある場合Kotlinだとこう書かないとちょっと怒られる
            taskCheckBox.text = it.name
        }
    }

}