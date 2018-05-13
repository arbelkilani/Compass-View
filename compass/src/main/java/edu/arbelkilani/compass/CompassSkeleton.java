package edu.arbelkilani.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

class CompassSkeleton extends RelativeLayout {

    private final static int DEGREES_COLOR = Color.BLACK;
    private final static boolean SHOW_ORIENTATION_LABEL = false;
    private static final int DEFAULT_DEGREES_STEP = 15;
    private final static int DEFAULT_BORDER_COLOR = Color.BLACK;

    private static final String EAST_INDEX = "E";
    private static final String NORTH_INDEX = "N";
    private static final String WEST_INDEX = "W";
    private static final String SOUTH_INDEX = "S";
    private static final int DEFAULT_MINIMIZED_ALPHA = 180;
    private static final int DEFAULT_ORIENTATION_LABELS_COLOR = Color.BLACK;
    private static final boolean DEFAULT_SHOW_BORDER = false;

    private int mWidth;
    private int mCenterX;
    private int mCenterY;

    private int mDegreesColor = DEGREES_COLOR;
    private boolean mShowOrientationLabel = SHOW_ORIENTATION_LABEL;
    private int mDegreesStep = DEFAULT_DEGREES_STEP;
    private int mOrientationLabelsColor = DEFAULT_ORIENTATION_LABELS_COLOR;
    private boolean mShowBorder = DEFAULT_SHOW_BORDER;
    private int mBorderColor = DEFAULT_BORDER_COLOR;

    /**
     * @param context
     */
    public CompassSkeleton(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CompassSkeleton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CompassSkeleton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        Log.d("TAG", "init function");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec < heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        mCenterX = mWidth / 2;
        mCenterY = mWidth / 2;

        drawCompassSkeleton(canvas);
        drawOuterCircle(canvas);

    }

    /**
     * @param canvas
     */
    private void drawOuterCircle(Canvas canvas) {

        int mStrokeWidth = (int) (mWidth * 0.01f);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setColor(mBorderColor);

        float radius = (mWidth / 2) - mStrokeWidth / 2;

        RectF rectF = new RectF();
        rectF.set(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);

        if (mShowBorder)
            canvas.drawArc(rectF, 0, 360, false, paint);
    }

    /**
     * @param canvas
     */
    private void drawCompassSkeleton(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.06f);
        textPaint.setColor(mOrientationLabelsColor);

        Rect rect = new Rect();

        int rPadded = mCenterX - (int) (mWidth * 0.01f);

        for (int i = 0; i <= 360; i = i + mDegreesStep) {

            int rEnd;
            int rText;

            if ((i % 90) == 0) {
                rEnd = mCenterX - (int) (mWidth * 0.08f);
                rText = mCenterX - (int) (mWidth * 0.15f);
                paint.setColor(mDegreesColor);
                paint.setStrokeWidth(mWidth * 0.02f);

                showOrientationLabel(canvas, textPaint, rect, i, rText);

            } else if ((i % 45) == 0) {
                rEnd = mCenterX - (int) (mWidth * 0.06f);
                paint.setColor(mDegreesColor);
                paint.setStrokeWidth(mWidth * 0.02f);

            } else {

                rEnd = mCenterX - (int) (mWidth * 0.04f);
                paint.setColor(mDegreesColor);
                paint.setStrokeWidth(mWidth * 0.015f);
                paint.setAlpha(DEFAULT_MINIMIZED_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));


            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     * @param textPaint
     * @param rect
     * @param i
     * @param rText
     */
    private void showOrientationLabel(Canvas canvas, TextPaint textPaint, Rect rect, int i, int rText) {
        if (mShowOrientationLabel) {
            int textX = (int) (mCenterX + rText * Math.cos(Math.toRadians(i)));
            int textY = (int) (mCenterX - rText * Math.sin(Math.toRadians(i)));

            String direction = EAST_INDEX;
            if (i == 0) {
                direction = EAST_INDEX;
            } else if (i == 90) {
                direction = NORTH_INDEX;
            } else if (i == 180) {
                direction = WEST_INDEX;
            } else if (i == 270) {
                direction = SOUTH_INDEX;
            }

            textPaint.getTextBounds(direction, 0, 1, rect);
            canvas.drawText(direction, textX - rect.width() / 2, textY + rect.height() / 2, textPaint);
        }
    }

    /**
     * @param degreesColor
     */
    public void setDegreesColor(int degreesColor) {
        mDegreesColor = degreesColor;
        invalidate();
    }

    /**
     * @param showOrientationLabel
     */
    public void setShowOrientationLabel(boolean showOrientationLabel) {
        mShowOrientationLabel = showOrientationLabel;
        invalidate();
    }

    /**
     * @param degreesStep
     * @throws Exception
     */
    public void setDegreesStep(int degreesStep) throws Exception {
        if (degreesStep > 360 || degreesStep < 0 || 360 % degreesStep != 0) {
            throw new Exception("Degree step is invalid");
        }
        mDegreesStep = degreesStep;
        invalidate();
    }

    /**
     * @param orientationLabelsColor
     */
    public void setOrientationLabelsColor(int orientationLabelsColor) {
        mOrientationLabelsColor = orientationLabelsColor;
        invalidate();
    }

    /**
     * @param showBorder
     */
    public void setShowBorder(boolean showBorder) {
        mShowBorder = showBorder;
        invalidate();
    }

    /**
     * @param borderColor
     */
    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        invalidate();
    }
}
