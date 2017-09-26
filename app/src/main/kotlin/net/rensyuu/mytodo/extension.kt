package net.rensyuu.mytodo

import android.support.annotation.IdRes
import android.view.View

fun <T: View> View.bindview (@IdRes id:Int):Lazy<T>{
    return lazy { findViewById<T>(id) }
}

