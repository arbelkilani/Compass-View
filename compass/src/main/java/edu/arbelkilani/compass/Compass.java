package edu.arbelkilani.compass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import static android.content.Context.SENSOR_SERVICE;

public class Compass extends RelativeLayout implements SensorEventListener {

    private final static String TAG = Compass.class.getSimpleName();

    private static final float NEEDLE_PADDING = 0.17f;
    private static final String DEGREE = "\u00b0";
    private static final float DATA_PADDING = 0.35f;
    private static final float TEXT_SIZE_FACTOR = 0.014f;
    private static final int DEFAULT_DEGREES_STEP = 15;
    private static final boolean DEFAULT_SHOW_ORIENTATION_LABEL = false;
    private static final boolean DEFAULT_SHOW_DEGREE_VALUE = false;
    private static final int DEFAULT_ORIENTATION_LABEL_COLOR = Color.BLACK;
    private static final boolean DEFAULT_SHOW_BORDER = false;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private ImageView mNeedleImageView;
    private TextView mDegreeTextView;

    private float mCurrentDegree = 0f;

    private boolean mShowBorder;
    private int mBorderColor;

    private int mDegreesColor;
    private boolean mShowOrientationLabels;
    private int mOrientationLabelsColor;

    private int mDegreeValueColor;
    private boolean mShowDegreeValue;

    private int mDegreesStep;

    private Drawable mNeedle;

    private CompassListener mCompassListener;

    public Compass(Context context) {
        super(context);
        init(context, null);
    }

    public Compass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Compass(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.compass_layout, this, true);

        SensorManager mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Compass, 0, 0);

        if (typedArray != null) {

            mShowBorder = typedArray.getBoolean(R.styleable.Compass_show_border, DEFAULT_SHOW_BORDER);
            mBorderColor = typedArray.getColor(R.styleable.Compass_border_color, DEFAULT_BORDER_COLOR);

            mDegreesColor = typedArray.getColor(R.styleable.Compass_degree_color, Color.BLACK);
            mShowOrientationLabels = typedArray.getBoolean(R.styleable.Compass_show_orientation_labels, DEFAULT_SHOW_ORIENTATION_LABEL);
            mOrientationLabelsColor = typedArray.getColor(R.styleable.Compass_orientation_labels_color, DEFAULT_ORIENTATION_LABEL_COLOR);

            mDegreeValueColor = typedArray.getColor(R.styleable.Compass_degree_value_color, Color.BLACK);
            mShowDegreeValue = typedArray.getBoolean(R.styleable.Compass_show_degree_value, DEFAULT_SHOW_DEGREE_VALUE);

            mDegreesStep = typedArray.getInt(R.styleable.Compass_degrees_step, DEFAULT_DEGREES_STEP);
            mNeedle = typedArray.getDrawable(R.styleable.Compass_needle);
            typedArray.recycle();
        }

        updateLayout();
        updateNeedle();
    }

    private void updateLayout() {

        mDegreeTextView = findViewById(R.id.tv_degree);

        final CompassSkeleton compassSkeleton = findViewById(R.id.compass_skeleton);
        compassSkeleton.setDegreesColor(mDegreesColor);
        compassSkeleton.setShowOrientationLabel(mShowOrientationLabels);
        compassSkeleton.setShowBorder(mShowBorder);
        compassSkeleton.setBorderColor(mBorderColor);

        try {
            compassSkeleton.setDegreesStep(mDegreesStep);
        } catch (Exception e) {
            e.printStackTrace();
        }

        compassSkeleton.setOrientationLabelsColor(mOrientationLabelsColor);

        final View dataLayout = findViewById(R.id.data_layout);
        compassSkeleton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                compassSkeleton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = compassSkeleton.getMeasuredWidth();
                int needlePadding = (int) (width * NEEDLE_PADDING);
                compassSkeleton.setPadding(needlePadding, needlePadding, needlePadding, needlePadding);

                int dataPaddingTop = (int) (width * DATA_PADDING);
                dataLayout.setPadding(0, dataPaddingTop, 0, 0);

                float degreeTextSize = width * TEXT_SIZE_FACTOR;
                mDegreeTextView.setTextSize(degreeTextSize);
            }
        });

        mDegreeTextView.setTextColor(mDegreeValueColor);
        if (mShowDegreeValue) {
            mDegreeTextView.setVisibility(VISIBLE);
        } else {
            mDegreeTextView.setVisibility(GONE);
        }
    }

    private void updateNeedle() {
        if (mNeedle == null) {
            mNeedle = ContextCompat.getDrawable(getContext(), R.drawable.ic_needle);
        }
        mNeedleImageView = findViewById(R.id.iv_needle);
        mNeedleImageView.setImageDrawable(mNeedle);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mCompassListener != null) {
            mCompassListener.onSensorChanged(event);
        }

        float degree = Math.round(event.values[0]);

        RotateAnimation rotateAnimation = new RotateAnimation(mCurrentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        mNeedleImageView.startAnimation(rotateAnimation);

        updateTextDirection(mCurrentDegree);

        mCurrentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (mCompassListener != null) {
            mCompassListener.onAccuracyChanged(sensor, accuracy);
        }
    }

    private void updateTextDirection(double degree) {
        double deg = 360 + degree;
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        String value;
        if (deg > 0 && deg <= 90) {
            value = String.format("%s%s NE", String.valueOf(decimalFormat.format(-degree)), DEGREE);
        } else if (deg > 90 && deg <= 180) {
            value = String.format("%s%s ES", String.valueOf(decimalFormat.format(-degree)), DEGREE);
        } else if (deg > 180 && deg <= 270) {
            value = String.format("%s%s SW", String.valueOf(decimalFormat.format(-degree)), DEGREE);
        } else {
            value = String.format("%s%s WN", String.valueOf(decimalFormat.format(-degree)), DEGREE);
        }
        mDegreeTextView.setText(value);
    }


    public void setListener(CompassListener compassListener) {
        mCompassListener = compassListener;
    }
}

