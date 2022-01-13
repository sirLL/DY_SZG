package cn.dianyinhuoban.szg.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.util.ToolUtil
import coil.load

class ImageDialog(context: Context) : Dialog(context, R.style.MessageDialog) {

    private var ivCover: ImageView? = null
    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_dialog_image)
        ivCover = findViewById(R.id.iv_cover)
        ivCover?.load(imagePath)
        findViewById<ImageView>(R.id.iv_cancel)?.setOnClickListener {
            dismiss()
        }

        val layoutParams = window?.attributes
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams?.width = (ToolUtil.getScreenWidth(context) * 0.8f).toInt()
        window?.attributes = layoutParams
        setCanceledOnTouchOutside(true)
    }

    public fun setImagePath(path: String?): ImageDialog {
        this.imagePath = path
        ivCover?.let {
            it.load(path)
        }
        return this
    }
}