package com.wareroom.lib_base.widget.bottomnavigation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public abstract class BaseTab extends FrameLayout {
    private boolean canChecked = true;

    ImageView mIconImageView;
    TextView mTitleTextView;
    RoundMessageView mMessagesView;

    @DrawableRes
    int mDefaultDrawable;
    @DrawableRes
    int mCheckedDrawable;
    String mTitle;
    @ColorInt
    int mDefaultTextColor = 0x56000000;
    @ColorInt
    int mCheckedTextColor = 0x56000000;

    public BaseTab(@NonNull Context context) {
        super(context);
    }

    public BaseTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置选中状态
     */
    abstract public void setChecked(boolean checked);

    /**
     * 设置消息数字。注意：数字需要大于0才会显示
     */
    abstract public void setMessageNumber(int number);

    /**
     * 设置是否显示无数字的小圆点。注意：如果消息数字不为0，优先显示带数字的圆
     */
    abstract public void setHasMessage(boolean hasMessage);

    /**
     * 获取标题文字
     */
    abstract public String getTitle();

    /**
     * 已选中的状态下再次点击时触发
     */
    public void onRepeat() {

    }

    public boolean isCanChecked() {
        return canChecked;
    }

    public BaseTab setCanChecked(boolean canChecked) {
        this.canChecked = canChecked;
        return this;
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    public void initialize(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, @ColorInt int color, @ColorInt int checkedColor, @StringRes int title) {
        mDefaultDrawable = drawableRes;
        mCheckedDrawable = checkedDrawableRes;
        mDefaultTextColor = color;
        mCheckedTextColor = checkedColor;
        mTitle = getContext().getString(title);
        mTitleTextView.setText(title);
    }

    public void initialize(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, @ColorInt int color, @ColorInt int checkedColor, String title) {
        mDefaultDrawable = drawableRes;
        mCheckedDrawable = checkedDrawableRes;
        mDefaultTextColor = color;
        mCheckedTextColor = checkedColor;
        mTitle = title;
        mTitleTextView.setText(title);
    }

    public void setTextDefaultColor(@ColorInt int color) {
        mDefaultTextColor = color;
    }

    public void setTextCheckedColor(@ColorInt int color) {
        mCheckedTextColor = color;
    }
}
