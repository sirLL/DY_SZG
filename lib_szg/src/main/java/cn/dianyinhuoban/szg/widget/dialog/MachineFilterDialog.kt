package cn.dianyinhuoban.szg.widget.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.income.view.IncomeActivationDetailFragment
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wareroom.lib_base.utils.DateTimeUtils
import java.util.*

class MachineFilterDialog : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(): MachineFilterDialog {
            val args = Bundle()
            val fragment = MachineFilterDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private var tvStartDate: TextView? = null
    private var tvEndDate: TextView? = null
    private var edSN: EditText? = null
    private var onViewClick: OnMachineFilterViewClick? = null
    private var startDate = ""
    private var endDate = ""
    private var inputSN: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = inflater.inflate(R.layout.dy_dialog_machine_filter, container, false)
        contentView.findViewById<ImageView>(R.id.iv_cancel).setOnClickListener {
            dismiss()
        }

        tvStartDate = contentView.findViewById(R.id.tv_start_date)
        tvEndDate = contentView.findViewById(R.id.tv_end_date)
        edSN = contentView.findViewById(R.id.ed_sn)

        tvStartDate?.setOnClickListener {
            onViewClick?.onStartDateClick(this)
        }
        tvEndDate?.setOnClickListener {
            onViewClick?.onEndDateClick(this)
        }
        contentView?.findViewById<Button>(R.id.btn_search)?.setOnClickListener {
            onViewClick?.let {
                it.onSearchClick(
                    edSN?.text?.toString() ?: "",
                    tvStartDate?.text?.toString() ?: "",
                    tvEndDate?.text?.toString() ?: ""
                )
            }
            dismiss()
        }
        edSN?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    tvStartDate?.text = ""
                    tvEndDate?.text = ""
                }
            }
        })
        tvStartDate?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    edSN?.setText("")
                }
            }
        })
        tvEndDate?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    edSN?.setText("")
                }
            }
        })

        tvStartDate?.text = startDate
        tvEndDate?.text = endDate
        edSN?.setText(inputSN)
        return contentView
    }

    fun setInputSN(sn: String): MachineFilterDialog {
        edSN?.setText(sn)
        this.inputSN = sn
        return this
    }

    fun setStartDate(startDate: String): MachineFilterDialog {
        tvStartDate?.text = startDate
        this.startDate = startDate
        return this
    }

    fun setEndDate(endDate: String): MachineFilterDialog {
        tvEndDate?.text = endDate
        this.endDate = endDate
        return this
    }


    fun setOnMachineFilterViewClick(onViewClick: OnMachineFilterViewClick): MachineFilterDialog {
        this.onViewClick = onViewClick
        return this
    }

    interface OnMachineFilterViewClick {
        fun onSearchClick(sn: String, startDate: String, endDate: String)

        fun onStartDateClick(filterDialog: MachineFilterDialog)

        fun onEndDateClick(filterDialog: MachineFilterDialog)
    }

}

