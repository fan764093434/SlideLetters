package com.fsw.slidelettersmenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author Foin
 * @version 1.0
 * @time 2017/4/21 10:08
 * @desc 自定义字母滑动控件
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SlideLettersMenu extends View {
    /**
     * @desc 字母数组
     */
    private String[] letters;
    /**
     * @desc 画笔
     */
    private Paint paint;
    /**
     * @desc 触摸区域的高宽
     */
    private Rect touchRect;
    /**
     * @desc 触摸区域默认宽度
     */
    private int rectWidth;
    /**
     * @desc 控件的总高度
     */
    private int height;
    /**
     * @desc 当前选择的字母坐标
     */
    private int chooseIndex = -1;
    /**
     * @desc 手机的最小滑动距离，每一个手机的这个值都存在差异
     */
    private int touchSlop;
    /**
     * @desc 滑动是Y轴坐标的记录
     */
    private float slideY;
    /**
     * @desc 是否开始滑动的标记
     */
    private boolean isBeingDrag = false;
    /**
     * @desc 滑动监听
     */
    private OnSlideListener onSlideListener;
    /**
     * @desc 数字的大小
     */
    private int textSize = 23;
    /**
     * @desc 字母的颜色
     */
    private int textColor = Color.BLACK;

    /**
     * @param context
     * @desc 构造函数
     */
    public SlideLettersMenu(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     * @desc 构造函数
     */
    public SlideLettersMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @desc 构造函数
     */
    public SlideLettersMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     * @desc 构造函数
     */
    public SlideLettersMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideLettersMenu);
        Log.e("fsw--", textSize + "-----" + textColor);
        textSize = typedArray.getDimensionPixelSize(R.styleable.SlideLettersMenu_textSize, textSize);
        textColor = typedArray.getColor(R.styleable.SlideLettersMenu_textColor, textColor);
        Log.e("fsw--", textSize + "-----" + textColor);
        typedArray.recycle();
        init(context);
    }

    /**
     * @author Foin
     * @desc 初始化View
     */
    private void init(Context context) {
        //获取数组资源
        letters = context.getResources().getStringArray(R.array.letters);
        //初始化paint
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        //获取手机的最小滑动距离
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //设置背景为透明，不要回遮住布局
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算字母的大小，是要通过和默认的触摸区域的宽度作比较，取较小的数字
        //假如letters的长度只有一个，这时候取字母的大小为高度的letters.length分之一的0.7倍就出现问题
        //所以正确的做法为如下
        height = h;
        rectWidth = (int) Math.min((float) (h / letters.length), textSize * 10 / 7);
        //继而引发触摸区域的计算,这个地方算法有点儿绕,左右的左边计算并不难，难点在于计算计算上下的坐标，这要看你吧这个触摸区域放置在哪儿，是控件的中间还是紧贴着顶部
        touchRect = new Rect(w - rectWidth, (h - rectWidth * letters.length) / 2, w, (h + rectWidth * letters.length) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < letters.length; i++) {
            //当前画的字母的中心点Y轴坐标
            float currentLetterCenterY = rectWidth * (i + 1) + touchRect.top - rectWidth / 2;
            float diff;
            float diffY;
            float diffX;
            //如果当前选择的字母是当前要画的字母则它的便宜量是最大的
            if (chooseIndex == i && i != 0 && i != letters.length - 1) {
                diff = 2.2f;
                diffX = 0f;
                diffY = 0f;
            } else {//当前要话的字母不是当前画的
                float maxPox = Math.abs((slideY - currentLetterCenterY) / touchRect.height() * 6);
                diff = Math.max(1f, 2.2f - maxPox);
                if (!isBeingDrag) {
                    diff = 1f;
                }
                diffX = maxPox * 100;
                diffY = maxPox * 50 * (currentLetterCenterY > slideY ? -1 : 1);
            }
            canvas.save();
            canvas.scale(diff, diff, touchRect.right * 1.20f + diffX, currentLetterCenterY + diffY);
            if (diff == 1f) {
                paint.setAlpha(255);
                paint.setTypeface(Typeface.DEFAULT);
            } else {
                int alpha = (int) (255 * (1 - Math.min(0.9, diff - 1)));
                if (chooseIndex == i) {
                    alpha = 255;
                }
                paint.setAlpha(alpha);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
            }
            canvas.drawText(letters[i], touchRect.left + rectWidth / 2, touchRect.top + i * rectWidth + textSize, paint);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //该判断表示按下的点不在触摸区域
                if (!touchRect.contains((int) event.getX(), (int) event.getY())) {
                    return false;
                }
                isBeingDrag = true;
                y = (int) event.getY();
                slideY = y;
                chooseIndex = judgePoint(y);
                if (onSlideListener != null) {
                    onSlideListener.onStartSlide(chooseIndex);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //此时只要判断滑动的Y轴坐标在触摸区域内就可以了
                if (event.getY() < touchRect.top || event.getY() > touchRect.bottom) {
                    return false;
                }
                float diff = Math.abs(event.getY() - y);
                //此时表示进行了滑动操作
                if (diff > touchSlop && !isBeingDrag) {
                    isBeingDrag = true;
                }
                chooseIndex = judgePoint(event.getY());
                slideY = event.getY();
                if (onSlideListener != null) {
                    onSlideListener.onSliding(chooseIndex);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (onSlideListener != null) {
                    onSlideListener.onStopSlide(letters[chooseIndex]);
                }
                chooseIndex = -1;
                isBeingDrag = false;
                break;
        }
        invalidate();
        return true;
    }

    /**
     * @desc 根据滑动的Y轴坐标计算当前滑到哪儿
     */
    private int judgePoint(float y) {
        return (int) ((y - (height - touchRect.height()) / 2) / touchRect.height() * letters.length);
    }

    /**
     * @desc 滑动菜单的滑动监听
     */
    public interface OnSlideListener {
        /**
         * @desc 开始滑动
         */
        void onStartSlide(int index);

        /**
         * @desc 滑动中
         */
        void onSliding(int index);

        /**
         * @desc 滑动结束监听
         */
        void onStopSlide(String index);
    }

    public OnSlideListener getOnSlideListener() {
        return onSlideListener;
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
