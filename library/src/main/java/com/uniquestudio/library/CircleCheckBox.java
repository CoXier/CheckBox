package com.uniquestudio.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by CoXier on 2016/7/26.
 */
public class CircleCheckBox extends MaterialCheckBox {
    private static final String TAG = "CircleCheckBox";
    private Paint mTickPaint, mTickBackgroundPaint, mCircleBorderPaint, mInnerCircleBackgroundPaint;
    private Path mArcPath;
    private Path mLeftPath, mRightPath;
    private PathMeasure mLeftMeasure, mRightMeasure;
    private int mTickWidth, mBorderWidth;
    private int mTickColor, mBorderColor, mBackgroundColor;
    private int mWidth, mHeight, mRadius;
    private int mDuration;
    private boolean mChecked;
    private Point mCenterPoint;
    private RectF mRectF;
    private Point mLeftPoint, mMiddlePoint, mRightPoint, mStopPoint;

    private ValueAnimator mRightAnimator;
    private ValueAnimator mLeftAnimator;
    private ValueAnimator mCircleAnimator;

    private OnCheckedChangeListener mListener;

    private final static int UNSET_FLAG = 1;

    public void setListener(OnCheckedChangeListener listener) {
        this.mListener = listener;
    }

    public CircleCheckBox(Context context) {
        super(context, null);
    }

    public CircleCheckBox(Context context, AttributeSet attrs) {
        super(context,attrs);
        getAttrs(context, attrs);
        init();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleCheckBox, 0, 0);
        try {
            mChecked = a.getBoolean(R.styleable.CircleCheckBox_checked, false);
            mDuration = a.getInteger(R.styleable.CircleCheckBox_animation_duration, 2000);
            mTickColor =
                    a.getColor(R.styleable.CircleCheckBox_tick_color, Color.WHITE);
            mTickWidth = a.getDimensionPixelSize(R.styleable.CircleCheckBox_tick_width, UNSET_FLAG);
            mBorderWidth =
                    a.getDimensionPixelOffset(R.styleable.CircleCheckBox_border_width, UNSET_FLAG);
            mBorderColor =
                    a.getColor(R.styleable.CircleCheckBox_border_color, Color.parseColor("#4AC65A"));
            mBackgroundColor = a.getColor(R.styleable.CircleCheckBox_background_color, Color.parseColor("#32bc43"));
        } finally {
            a.recycle();
        }

    }

    private void init() {
        mTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCircleBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // paint of the tick
        mTickPaint.setColor(mTickColor);
        mTickPaint.setStyle(Paint.Style.STROKE);
        mTickPaint.setStrokeCap(Paint.Cap.ROUND);

        mTickBackgroundPaint.setColor(mBackgroundColor);
        mTickBackgroundPaint.setStyle(Paint.Style.STROKE);
        mTickBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);


        // paint of the border
        mCircleBorderPaint.setColor(mTickColor);
        mCircleBorderPaint.setStyle(Paint.Style.STROKE);
        mCircleBorderPaint.setStrokeCap(Paint.Cap.ROUND);

        // paint of the background
        mInnerCircleBackgroundPaint.setColor(mBackgroundColor);
        mInnerCircleBackgroundPaint.setStyle(Paint.Style.FILL);

        mCenterPoint = new Point();
        // three point of the tick
        mLeftPoint = new Point();
        mMiddlePoint = new Point();
        mRightPoint = new Point();
        mStopPoint = new Point();

        // build the path of tick
        mLeftPath = new Path();
        mRightPath = new Path();

        mLeftMeasure = new PathMeasure();
        mRightMeasure = new PathMeasure();
        mArcPath = new Path();
        mRectF = new RectF();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
                if (mListener != null) {
                    mListener.onCheckedChanged(isChecked());
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        mWidth = width - paddingLeft - paddingRight;
        mHeight = height - paddingTop - paddingBottom;
        int diameter = Math.min(mWidth, mHeight);
        mRadius = Math.min(mWidth, mHeight) / 2 - mBorderWidth;

        mCenterPoint.x = paddingLeft + mRadius + mBorderWidth;
        mCenterPoint.y = paddingTop + mRadius + mBorderWidth;

        mRectF.set(paddingLeft + mBorderWidth, paddingTop + mBorderWidth, mWidth - mBorderWidth, mHeight - mBorderWidth);

        if (mTickWidth == UNSET_FLAG) {
            mTickWidth = mWidth / 10;
        }

        if (mBorderWidth == UNSET_FLAG) {
            mBorderWidth = mWidth / 12;
        }

        mCircleBorderPaint.setStrokeWidth(mBorderWidth);
        mTickPaint.setStrokeWidth(mTickWidth);
        mTickBackgroundPaint.setStrokeWidth(mTickWidth);

        mLeftPoint.x = (int) (paddingLeft + (diameter * 0.2428));
        mLeftPoint.y = (int) (paddingTop + diameter * 0.4712);

        mMiddlePoint.x = (int) (paddingLeft + (diameter * 0.4571));
        mMiddlePoint.y = (int) (paddingTop + diameter * 0.6642);

        mStopPoint.x = (int) (paddingLeft + diameter * 0.4581);
        mStopPoint.y = (int) (paddingTop + diameter * 0.6652);

        mRightPoint.x = (int) (paddingLeft + (diameter * 0.7642));
        mRightPoint.y = (int) (paddingTop + diameter * 0.3285);

        mLeftPath.moveTo(mLeftPoint.x, mLeftPoint.y);
        mLeftPath.lineTo(mMiddlePoint.x, mMiddlePoint.y);
        mLeftMeasure.setPath(mLeftPath, false);
        mLeftPath.reset();

        mRightPath.moveTo(mStopPoint.x, mStopPoint.y);
        mRightPath.lineTo(mRightPoint.x, mRightPoint.y);
        mRightMeasure.setPath(mRightPath, false);
        mRightPath.reset();

        if (isChecked()) {
            mLeftMeasure.getSegment(0, mLeftMeasure.getLength(), mLeftPath, true);
            mRightMeasure.getSegment(0, mRightMeasure.getLength(), mRightPath, true);
            mArcPath.reset();
            mArcPath.addArc(mRectF, 0, 0);
        }else{
            mArcPath.reset();
            mArcPath.addCircle(mCenterPoint.x,mCenterPoint.y,mRadius, Path.Direction.CCW);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw inner circle
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mInnerCircleBackgroundPaint);
        // draw ring
        mCircleBorderPaint.setColor(mBorderColor);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCircleBorderPaint);
        mCircleBorderPaint.setColor(mTickColor);
        canvas.drawPath(mArcPath, mCircleBorderPaint);
        // draw tick
        canvas.drawLine(mLeftPoint.x, mLeftPoint.y, mMiddlePoint.x, mMiddlePoint.y, mTickBackgroundPaint);
        canvas.drawPath(mLeftPath, mTickPaint);
        canvas.drawLine(mStopPoint.x, mStopPoint.y, mRightPoint.x, mRightPoint.y, mTickBackgroundPaint);
        canvas.drawPath(mRightPath, mTickPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLeftAnimator != null) mLeftAnimator.cancel();
        if (mRightAnimator != null) mRightAnimator.cancel();
        if (mCircleAnimator != null) mCircleAnimator.cancel();
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        reset();
        if (checked) {
            startCheckedAnimation();
        } else {
            startUnCheckedAnimation();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    private void reset() {
        mLeftPath.reset();
        mRightPath.reset();
    }

    private void startUnCheckedAnimation() {
        // tick animation
        mLeftMeasure.getSegment(0, mLeftMeasure.getLength(), mLeftPath, true);
        mRightAnimator = ValueAnimator.ofFloat(1f, 0f);
        mRightAnimator.setDuration((long) (mDuration * 0.16));
        mRightAnimator.setInterpolator(new LinearInterpolator());
        mRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mRightPath.reset();
                mRightPath.lineTo(0, 0);
                mRightMeasure.getSegment(0, value * mRightMeasure.getLength(), mRightPath, true);
                postInvalidate();
            }
        });
        mRightAnimator.start();

        mLeftAnimator = ValueAnimator.ofFloat(1f, 0f);
        mLeftAnimator.setStartDelay((long) (mDuration * 0.14));
        mLeftAnimator.setDuration((long) (mDuration * 0.10));
        mLeftAnimator.setInterpolator(new LinearInterpolator());
        mLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mLeftPath.reset();
                mLeftPath.lineTo(0, 0);
                mLeftMeasure.getSegment(0, value * mLeftMeasure.getLength(), mLeftPath, true);
                postInvalidate();
            }
        });
        mLeftAnimator.start();

        // circle animation
        mCircleBorderPaint.setColor(mBorderColor);
        mCircleAnimator = ValueAnimator.ofFloat(0f, 1f);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mArcPath.reset();
                mArcPath.addArc(mRectF, -159, 360 * value);
                postInvalidate();
            }
        });
        mCircleAnimator.setDuration(mDuration / 3);
        mCircleAnimator.setStartDelay((long) (mDuration * 0.23));
        mCircleAnimator.start();
    }

    private void startCheckedAnimation() {
        // circle animation
        mCircleAnimator = ValueAnimator.ofFloat(1f, 0f);
        mCircleAnimator.setInterpolator(new DecelerateInterpolator());
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mArcPath.reset();
                mArcPath.addArc(mRectF, -159, 360 * value);
                postInvalidate();
            }
        });
        mCircleAnimator.setDuration(mDuration / 4);
        mCircleAnimator.start();

        // tick animation
        mTickPaint.setColor(mTickColor);
        mTickPaint.setStrokeWidth(mTickWidth);
        mTickBackgroundPaint.setColor(mBackgroundColor);

        mLeftAnimator = ValueAnimator.ofFloat(0f, 1f);
        mLeftAnimator.setStartDelay((long) (mDuration * 0.21));
        mLeftAnimator.setDuration(mDuration / 7);
        mLeftAnimator.setInterpolator(new LinearInterpolator());
        mLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mLeftPath.reset();
                mLeftPath.lineTo(0, 0);
                mLeftMeasure.getSegment(0, value * mLeftMeasure.getLength(), mLeftPath, true);
                postInvalidate();
            }
        });
        mLeftAnimator.start();

        mRightAnimator = ValueAnimator.ofFloat(0f, 1f);
        mRightAnimator.setStartDelay((long) (mDuration * 0.33));
        mRightAnimator.setDuration(mDuration / 5);
        mRightAnimator.setInterpolator(new LinearInterpolator());
        mRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mRightPath.reset();
                mRightPath.lineTo(0, 0);
                mRightMeasure.getSegment(0, value * mRightMeasure.getLength(), mRightPath, true);
                postInvalidate();
            }
        });
        mRightAnimator.start();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }

}
