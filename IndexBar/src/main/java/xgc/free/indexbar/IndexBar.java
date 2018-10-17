package xgc.free.indexbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexBar extends View {
    private String[] DEFAULT_INDEX_ITEMS = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static final int SHAPE_BG_RECT = 1;
    public static final int SHAPE_BG_ROUND = 2;
    private List<String> indexItems;
    private float itemHeight; //每个index的高度
    private int currentIndex = -1;

    private Paint paint;
    private Paint touchPaint;
    private Paint bgPaint;
    private Paint testPaint;

    private int width;//View的宽度
    private int height;//View的高度

    private TextView overlayTextView;
    private IndexChangedListener indexChangedListener;
    private int bgColor = 0xffffffff;//背景颜色
    private boolean isDrawBg = false;
    private int bgType = SHAPE_BG_RECT;
    private RectF bgRectF;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        indexItems = new ArrayList<>();
        indexItems.addAll(Arrays.asList(DEFAULT_INDEX_ITEMS));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(dpToPx(14));
        paint.setColor(Color.parseColor("#2A70FE"));

        touchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchPaint.setTextSize(dpToPx(20));
        touchPaint.setColor(Color.parseColor("#2A70FE"));

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);

        testPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 文字大小
     *
     * @param textSize(dp)
     */
    public void setTextSize(int textSize) {
        paint.setTextSize(dpToPx(textSize));
    }

    public void setTextColor(int color) {
        paint.setColor(color);
        touchPaint.setColor(color);
    }

    public void setTextColorRes(@ColorRes int colorId) {
        paint.setColor(ContextCompat.getColor(getContext(), colorId));
        touchPaint.setColor(ContextCompat.getColor(getContext(), colorId));
    }

    public void setOverlayTextView(TextView overlay) {
        this.overlayTextView = overlay;
    }

    public void setBgColor(int color) {
        bgColor = color;
        bgPaint.setColor(bgColor);
    }

    public void setBgColorId(@ColorRes int colorId) {
        bgColor = ContextCompat.getColor(getContext(), colorId);
        bgPaint.setColor(bgColor);
    }

    /**
     * 背景形状(矩形和圆角)
     *
     * @param type
     */
    public void setBgType(int type) {
        bgType = type;
    }

    public void setIndexChangedListener(IndexChangedListener listener) {
        this.indexChangedListener = listener;
    }

    /**
     * 更新索引
     *
     * @param indexs
     */
    public void updateIndex(List<String> indexs) {
        currentIndex = -1;
        indexItems = indexs;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawBg && bgColor != 0xffffffff) {
            if(bgRectF == null){
                bgRectF = new RectF();
            }
            bgRectF.left = getPaddingLeft();
            bgRectF.right = width - getPaddingRight();
            bgRectF.top = getPaddingTop();
            bgRectF.bottom = height - getPaddingTop();
            if (bgType == SHAPE_BG_RECT) {
                canvas.drawRect(bgRectF, bgPaint);
            } else {
                canvas.drawRoundRect(bgRectF, width / 2, width / 2, bgPaint);
            }
        }

        String index;
        float x, y;
        Paint tmpPaint;
        Paint.FontMetrics fm;

        for (int i = 0; i < indexItems.size(); i++) {
            index = indexItems.get(i);
            if(i == currentIndex){
                tmpPaint = touchPaint;
            }else {
                tmpPaint = paint;
            }

            fm = tmpPaint.getFontMetrics();
            x = (width - tmpPaint.measureText(index)) / 2;
            //计算位置，确保文字绘制在每个矩形中间
            y = itemHeight * i + itemHeight / 2 + (-fm.top - fm.bottom) / 2;
            //test(canvas, i);
            canvas.drawText(index, x, y, tmpPaint);
        }
    }

    private void test(Canvas canvas, int index) {
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.right = width;
        rectF.top = itemHeight * index;
        rectF.bottom = rectF.top + itemHeight;
        testPaint.setColor(Color.argb(255 * (index + 1) / indexItems.size(), 255, 0, 0));
        canvas.drawRect(rectF, testPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = Math.max(h, oldh);
        itemHeight = height * 1.0f / indexItems.size();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int indexSize = indexItems.size();
                int touchedIndex = (int) (y / itemHeight);
                if (touchedIndex < 0) {
                    touchedIndex = 0;
                } else if (touchedIndex >= indexSize) {
                    touchedIndex = indexSize - 1;
                }
                if (touchedIndex >= 0 && touchedIndex < indexSize) {
                    if (touchedIndex != currentIndex) {
                        currentIndex = touchedIndex;
                        if (overlayTextView != null) {
                            overlayTextView.setVisibility(VISIBLE);
                            overlayTextView.setText(indexItems.get(touchedIndex));
                        }
                        if (indexChangedListener != null) {
                            indexChangedListener.onIndexChanged(indexItems.get(touchedIndex), touchedIndex);
                        }
                        isDrawBg = true;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentIndex = -1;
                if (overlayTextView != null) {
                    overlayTextView.setVisibility(GONE);
                }
                isDrawBg = false;
                invalidate();
                break;
        }
        return true;
    }


    public interface IndexChangedListener {
        void onIndexChanged(String index, int position);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
