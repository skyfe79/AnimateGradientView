package kr.pe.burt.android.lib.animategradientview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by burt on 2016. 7. 20..
 */
public class AnimateGradientView extends View {

    private float angle = 0.0f;
    private int startColor = Color.parseColor("#00000000");
    private int endColor = Color.parseColor("#FF000000");
    private int middleColor = -1;
    private float gradientAlpha = 1.0f;
    private float startOffset = 0.0f;
    private float endOffset = 1.0f;
    private float middleOffset = 0.5f;
    private float gradientOffset = 0;
    private float gradientScale = 1;
    private long duration = 1000;

    private int [] colors = null;
    private float [] offsets = null;
    private Shader gradient = null;
    private Matrix rotateMatrix = null;
    private Paint gradientPaint = null;

    private Path shapePath = null;
    private Path flowPath  = new Path();

    private AnimationType animationType = AnimationType.Flow;
    private FlowDirection flowDirection = FlowDirection.Left;
    private Boolean autoStartAnimation = true;

    private ObjectAnimator animator = null;

    public enum AnimationType {
        Rotation,
        Flow
    }

    public enum FlowDirection {
        Left,
        Right,
        Top,
        Bottom
    }



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

        angle = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_angle, angle);

        startColor = array.getColor(R.styleable.AnimateGradientViewAttrs_agv_startColor, startColor);
        endColor = array.getColor(R.styleable.AnimateGradientViewAttrs_agv_endColor, endColor);
        middleColor = array.getColor(R.styleable.AnimateGradientViewAttrs_agv_middleColor, middleColor);

        startOffset = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_startOffset, startOffset);
        endOffset = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_endOffset, endOffset);
        middleOffset = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_middleOffset, middleOffset);

        gradientAlpha = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_alpha, gradientAlpha);

        int atype = array.getInt(R.styleable.AnimateGradientViewAttrs_agv_animation, animationType.ordinal());
        if(atype == 0) {
            animationType = AnimationType.Rotation;
        } else {
            animationType = AnimationType.Flow;
        }

        int dir = array.getInt(R.styleable.AnimateGradientViewAttrs_agv_flow_direction, flowDirection.ordinal());
        switch (dir) {
            case 0:
                flowDirection = FlowDirection.Left;
                break;
            case 1:
                flowDirection = FlowDirection.Right;
                break;
            case 2:
                flowDirection = FlowDirection.Top;
                break;
            default:
                flowDirection = FlowDirection.Bottom;
        }

        gradientScale = array.getFloat(R.styleable.AnimateGradientViewAttrs_agv_gradient_scale, gradientScale);
        autoStartAnimation = array.getBoolean(R.styleable.AnimateGradientViewAttrs_agv_autostart, autoStartAnimation);
        duration = array.getInt(R.styleable.AnimateGradientViewAttrs_agv_duration, (int)duration);
        array.recycle();

        gradientPaint = new Paint();
        rotateMatrix = new Matrix();

        flowPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // fix angle for y transition
        if(flowDirection == FlowDirection.Top || flowDirection == FlowDirection.Bottom) {
            angle = 90;
        }

        rotateMatrix.setRotate(angle, getWidth()/2, getHeight()/2);
        gradient.setLocalMatrix(rotateMatrix);
        gradientPaint.setShader(gradient);
        gradientPaint.setAlpha((int)(gradientAlpha * 255));

        canvas.save();

        if(flowDirection == FlowDirection.Left || flowDirection == FlowDirection.Right) {
            canvas.translate(-gradientOffset, 0);
            shapePath.offset(gradientOffset, 0, flowPath);
        } else {
            canvas.translate(0, -gradientOffset);
            shapePath.offset(0, gradientOffset, flowPath);
        }
        canvas.drawPath(flowPath, gradientPaint);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);

        if(middleColor == -1) {
            colors = new int [] { startColor, endColor };
            offsets = new float[] { startOffset, endOffset };
        } else {
            colors = new int [] { startColor, middleColor, endColor };
            offsets = new float[] { startOffset, middleOffset, endOffset };
        }

        gradient = new LinearGradient(0, height/2, width * gradientScale, height/2, colors, offsets, Shader.TileMode.MIRROR);
        shapePath = new Path();
        shapePath.addRect(0, 0, width, height, Path.Direction.CCW);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if(autoStartAnimation) {
            if(animationType == AnimationType.Flow) {
                startTransitionAnimation(duration, 1000, flowDirection);
            } else {
                startInfiniteRotationAnimation(duration);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
       stopAnimation();
        super.onDetachedFromWindow();
    }

    /**
     * Provide get/set methods for Property Animation
     */
    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        postInvalidate();
    }


    public float getGradientAlpha() {
        return gradientAlpha;
    }

    public void setGradientAlpha(float gradientAlpha) {
        this.gradientAlpha = gradientAlpha;
        postInvalidate();
    }

    public float getGradientOffset() {
        return gradientOffset;
    }

    public void setGradientOffset(float gradientOffset) {
        this.gradientOffset = gradientOffset;
        postInvalidate();
    }

    public float getGradientScale() {
        return gradientScale;
    }

    public void setGradientScale(float gradientScale) {
        this.gradientScale = gradientScale;
        onSizeChanged(getWidth(), getHeight(), 0, 0);
        postInvalidate();
    }

    public void startRotateAnimation(long duration) {
        startRotateAnimation(duration, 1);
    }

    public Animator startInfiniteRotationAnimation(long duration) {
        if(isInEditMode())
            return null;

        animator = ObjectAnimator.ofFloat(this, "angle", 0.0f, 360.0f);
        animator.setDuration(duration);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        return animator;
    }

    public Animator startRotateAnimation(long duration, int count) {

        if(isInEditMode())
            return null;

        animator = ObjectAnimator.ofFloat(this, "angle", 0.0f, 360.0f);
        animator.setDuration(duration);
        animator.setRepeatCount(count);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        return animator;
    }

    public Animator startTransitionAnimation(long duration, int length, FlowDirection direction) {

        if(isInEditMode())
            return null;


        float from;
        float to;

        switch (direction) {
            case Left:
            case Top:
                from = 0.0f;
                to = length * 1000.0f;
                break;
            default:
                from = length * 1000.0f;
                to = 0.0f;

        }
        animator = ObjectAnimator.ofFloat(this, "gradientOffset", from, to);
        animator.setDuration(duration * 1000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

        return animator;
    }

    public void stopAnimation() {
        if(animator != null) {
            animator.cancel();
            animator = null;
        }
    }

}
