package clear_edittext.rxl.com.clearedittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by RXL on 2016/8/22.
 */
public class ClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private boolean hasFoucs;
    private Drawable rightDrawable;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        // 第三个参数必须要有，否则EditText不能输入
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rightDrawable = getCompoundDrawables()[2];
        if (rightDrawable == null) {
            rightDrawable = getResources().getDrawable(R.drawable.delete_icon);
        }

        // setBounds(x,y,width,height); x:组件在容器X轴上的起点 y:组件在容器Y轴上的起点 width:组件的宽度 height:组件的高度
        // 必须为控件设置大小，否则drawable不显示
        // x/y 为EditText右侧的ImageView控件，不是EditText控件
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());

        setClearIconVisible(false); // 默认隐藏清除按钮
        setOnFocusChangeListener(this); // 设置焦点变化监听器
        addTextChangedListener(this); // 设置文字内容变化监听器
    }

    /**
     * 设置删除图片的显示与否
     *
     * @param visible
     */
    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? rightDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 判断点击位置是否需要删除全部，getCompoundPaddingRight()得到的是控件padding + 右drawable的宽度
            // getX() 在此得到的是触摸位置相对于控件左边缘的坐标
            int[] location = new int[]{0, 0};
            getLocationInWindow(location);
            boolean isClear = event.getX() > (getWidth() - getCompoundPaddingRight()) && event.getX() < (getWidth() - getPaddingRight())
                    & event.getY() > (getHeight() / 2 - rightDrawable.getIntrinsicHeight() / 2) &&
                    event.getY() < (getHeight() / 2 + rightDrawable.getIntrinsicHeight() / 2);

            if (isClear) {
                Log.i("MainActivity", "清除");
                getText().clear();
            } else {
                Log.i("MainActivity", "非清除");
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (hasFoucs) {
            setClearIconVisible(getText().length() > 0);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
