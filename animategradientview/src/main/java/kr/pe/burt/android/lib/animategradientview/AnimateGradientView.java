package kr.pe.burt.android.lib.animategradientview;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by burt on 2016. 7. 20..
 */
public class AnimateGradientView extends View {

    private float startX = 0f;
    private float startY = 0f;
    private float widthRatio = 1.0f;
    private float heightRatio = 1.0f;
    private float rotate = 0.0f;
    private int startColor = Color.parseColor("#00000000");
    private int endColor = Color.parseColor("#FF000000");
    private int middleColor = -1;
    private float gradientAlpha = 1.0f;
    private float startOffset = 0.0f;
    private float endOffset = 1.0f;
    private float middleOffset = 0.5f;

    int [] colors = null;
    float [] offsets = null;
    Shader gradient = null;
    Matrix rotateMatrix = null;
    Paint gradientPaint = null;

    public AnimateGradientView(Context context) {
        this(context, null);
    }

    public AnimateGradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimateGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimateGradientView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs == null)
            return;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AnimateGradientViewAttrs);

        startX = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_x, startX);
        startY = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_y, startY);
        widthRatio = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_width, widthRatio);
        heightRatio= array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_height, heightRatio);
        rotate = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_rotate, rotate);

        startColor = array.getColor(R.styleable.AnimateGradientViewAttrs_agv_startColor, startColor);
        endColor = array.getColor(R.styleable.AnimateGradientViewAttrs_agv_endColor, endColor);
        middleColor = array.getColor(R.styleable.AnimateGradientViewAttrs_agv_middleColor, middleColor);

        startOffset = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_startOffset, startOffset);
        endOffset = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_endOffset, endOffset);
        middleOffset = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_middleOffset, middleOffset);

        gradientAlpha = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_alpha, gradientAlpha);

        array.recycle();

        if(middleColor == -1) {
            colors = new int [] { startColor, endColor };
            offsets = new float[] { startOffset, endOffset };
        } else {
            colors = new int [] { startColor, middleColor, endColor };
            offsets = new float[] { startOffset, middleOffset, endOffset };
        }

        gradientPaint = new Paint();
        rotateMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = startX * getWidth();
        float top = startY * getHeight();

        float right = left + widthRatio * getWidth();
        float bottom = top + heightRatio * getHeight();

        if(gradient == null) {
            gradient = new LinearGradient(
                    left, top,
                    right, bottom,
                    colors,
                    offsets,
                    Shader.TileMode.MIRROR);
        }
        rotateMatrix.setRotate(rotate, getWidth()/2, getHeight()/2);
        gradient.setLocalMatrix(rotateMatrix);
        gradientPaint.setShader(gradient);
        gradientPaint.setAlpha((int)(gradientAlpha * 255));
        canvas.drawRect(left, top, right, bottom, gradientPaint);
    }

    /**
     * Provide get/set methods for Property Animation
     */
    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
        gradient = null;
        postInvalidate();
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
        gradient = null;
        postInvalidate();
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
        gradient = null;
        postInvalidate();
    }

    public float getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(float widthRatio) {
        this.widthRatio = widthRatio;
        gradient = null;
        postInvalidate();
    }

    public float getHeightRatio() {
        return heightRatio;
    }

    public void setHeightRatio(float heightRatio) {
        this.heightRatio = heightRatio;
        gradient = null;
        postInvalidate();
    }

    public float getGradientAlpha() {
        return gradientAlpha;
    }

    public void setGradientAlpha(float gradientAlpha) {
        this.gradientAlpha = gradientAlpha;
        postInvalidate();
    }


    public void startRotateAnimation(long duration) {
        startRotateAnimation(duration, 1);
    }

    public void startInfiniteRotationAnimation(long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "rotate", 0.0f, 360.0f);
        animator.setDuration(duration);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void startRotateAnimation(long duration, int count) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "rotate", 0.0f, 360.0f);
        animator.setDuration(duration);
        animator.setRepeatCount(count);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    // 잘 안되고 있음
    // @link { http://stackoverflow.com/questions/32224797/how-to-animate-gradient }
    // 위 링크를 참고해 보자
    public void startTransitionXAnimation(long duration, int count) {

    }
}
