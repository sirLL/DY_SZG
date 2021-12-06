package cn.dianyinhuoban.szg.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.math.BigDecimal;

import cn.dianyinhuoban.szg.R;
import cn.dianyinhuoban.szg.util.ToolUtil;

public class GiftDialog extends Dialog {

    private TextView tvAmount;
    private String amount;
    private OnViewClickListener onViewClickListener;
    private ConstraintLayout closeContainer;
    private ConstraintLayout openContainer;

    public GiftDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_dialog_gift);
        tvAmount = findViewById(R.id.tv_amount);
        closeContainer = findViewById(R.id.close_container);
        openContainer = findViewById(R.id.open_container);
        if (TextUtils.isEmpty(amount)) {
            tvAmount.setText("--");
        } else {
            tvAmount.setText(numberScale(amount));
        }


        findViewById(R.id.iv_cancel).setOnClickListener(v -> {
            dismiss();
        });
        findViewById(R.id.view_get).setOnClickListener(v -> {
            if (onViewClickListener != null) {
                onViewClickListener.onGetGift(GiftDialog.this);
            }
        });

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = (int) (ToolUtil.getScreenWidth(getContext()) * 0.7f);
        window.setAttributes(layoutParams);
    }

    public GiftDialog showAmount() {
        if (openContainer != null && closeContainer != null) {
            openContainer.setVisibility(View.VISIBLE);
            closeContainer.setVisibility(View.GONE);
        }
        return this;
    }

    public GiftDialog setAmount(String amount) {
        this.amount = amount;
        if (tvAmount != null) {
            if (TextUtils.isEmpty(amount)) {
                tvAmount.setText("--");
            } else {
                tvAmount.setText(numberScale(amount));
            }
        }
        return this;
    }

    private String numberScale(String money) {
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
    }

    public GiftDialog setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
        return this;
    }

    public interface OnViewClickListener {
        void onGetGift(GiftDialog dialog);
    }


}
