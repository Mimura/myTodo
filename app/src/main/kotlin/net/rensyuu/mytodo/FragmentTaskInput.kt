package net.rensyuu.mytodo

// 下位のバージョンにも対応させる場合はsupport-v4パッケージを使用します
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText


// Fragmentクラスを継承します
class FragmentTaskInput : Fragment() {

    companion object {

        fun newInstance(target: Fragment, requestInputMode: InputMode): FragmentTaskInput {
            val fragment = FragmentTaskInput()
            fragment.setTargetFragment(target, requestInputMode.num)

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater!!.inflate(R.layout.fragment_task_input, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonOk = view?.findViewById<Button>(R.id.button_ok)
        buttonOk?.setOnClickListener { onClickOk(view) }
        when (targetRequestCode) {
            InputMode.CREATE.num -> {
                buttonOk?.setOnClickListener { onClickOk(view) }
            }
            InputMode.EDIT.num -> {
                buttonOk?.setOnClickListener { onClickEditOk(view) }
            }
        }
        val buttonCancel = view?.findViewById<Button>(R.id.button_cansel)
        buttonCancel?.setOnClickListener { onClickCancel(view) }
    }

    private fun onClickOk(view: View?) {
        listener?.onClickTaskCreateOk(getText(view))
    }

    private fun onClickEditOk(view: View?) {
        listener?.onClickTaskEditOk(getText(view))
    }

    private fun onClickCancel(view: View?) {
        listener?.onClickTaskInputCancel(getText(view))
    }

    private fun getText(view: View?): String {
        val textView = view?.findViewById<EditText>(R.id.edit_text)
        return textView?.text.toString()
    }

    // FragmentがActivityに追加されたら呼ばれるメソッド
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // APILevel23からは引数がActivity->Contextになっているので注意する
        // contextクラスがMyListenerを実装しているかをチェックする
        if (context is Listener) {
            // リスナーをここでセットするようにします
            listener = context
        }

        if (listener != null) {
            return
        }

        //呼び出し元がフラグメントだった場合
        val preFragment = targetFragment
        if (preFragment is Listener) {
            listener = preFragment
        }
    }

    // FragmentがActivityから離れたら呼ばれるメソッド
    override fun onDetach() {
        super.onDetach()
        // 画面からFragmentが離れたあとに処理が呼ばれることを避けるためにNullで初期化しておく
        listener = null
    }

    // 変数を用意する
    private var listener: Listener? = null

    interface Listener {
        fun onClickTaskCreateOk(text: String)
        fun onClickTaskInputCancel(text: String)
        fun onClickTaskEditOk(text: String)
    }

    enum class InputMode(val num: Int) {
        CREATE(0),
        EDIT(1)
    }
}
