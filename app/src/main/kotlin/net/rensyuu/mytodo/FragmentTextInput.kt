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
class FragmentTextInput : Fragment() {

    companion object {

        fun newInstance(target: Fragment, requestCode: Int): FragmentTextInput {
            val fragment = FragmentTextInput()
            fragment.setTargetFragment(target, requestCode)

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
        val buttonCancel = view?.findViewById<Button>(R.id.button_cansel)
        buttonCancel?.setOnClickListener { onClickCancel(view) }
    }

    private fun onClickOk(view: View?) {
        myListener?.onClickOk(getText(view))
    }

    private fun onClickCancel(view: View?) {
        myListener?.onClickCancel(getText(view))
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
        if (context is MyListener) {
            // リスナーをここでセットするようにします
            myListener = context
        }

        if (myListener != null) {
            return
        }

        //呼び出し元がフラグメントだった場合
        val preFragment = targetFragment
        if (preFragment is MyListener) {
            myListener = preFragment
        }
    }

    // FragmentがActivityから離れたら呼ばれるメソッド
    override fun onDetach() {
        super.onDetach()
        // 画面からFragmentが離れたあとに処理が呼ばれることを避けるためにNullで初期化しておく
        myListener = null
    }

    // 変数を用意する
    private var myListener: MyListener? = null

    interface MyListener {
        fun onClickOk(text: String)
        fun onClickCancel(text: String)
    }
}
