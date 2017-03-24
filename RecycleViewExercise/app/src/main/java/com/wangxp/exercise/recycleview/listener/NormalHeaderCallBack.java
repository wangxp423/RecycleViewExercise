package com.wangxp.exercise.recycleview.listener;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.BaseRefreshHeader;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wangxp.exercise.recycleview.R;


/**
 * @类描述：常规xRecyclerView CallBack
 * @创建人：Wangxiaopan
 * @创建时间：2017/3/16 0016 11:04
 * @修改人：
 * @修改时间：2017/3/16 0016 11:04
 * @修改备注：
 */

public class NormalHeaderCallBack implements XRecyclerView.HeaderAndFooterViewCallback{
    private String mTipText;
    StateListDrawable mPullingDrawable;
    public NormalHeaderCallBack(Context context){
        StateListDrawable drawable = mPullingDrawable = new StateListDrawable();
        for (int i = DRAWABLE_PULL.length - 1; i >= 0; i--) {
            final int resid = DRAWABLE_PULL[i];
            drawable.addState(new int[]{i}, context.getResources().getDrawable(resid));
        }
    }

    private final int[] DRAWABLE_PULL = {
            R.drawable.refresh_pulling_bird_logo_01, R.drawable.refresh_pulling_bird_logo_02, R.drawable.refresh_pulling_bird_logo_03,
            R.drawable.refresh_pulling_bird_logo_04, R.drawable.refresh_pulling_bird_logo_05, R.drawable.refresh_pulling_bird_logo_06,
            R.drawable.refresh_pulling_bird_logo_07, R.drawable.refresh_pulling_bird_logo_08, R.drawable.refresh_pulling_bird_logo_09,
            R.drawable.refresh_pulling_bird_logo_10, R.drawable.refresh_pulling_bird_logo_11, R.drawable.refresh_pulling_bird_logo_12,
            R.drawable.refresh_pulling_bird_logo_13, R.drawable.refresh_pulling_bird_logo_14, R.drawable.refresh_pulling_bird_logo_15,
            R.drawable.refresh_pulling_bird_logo_16, R.drawable.refresh_pulling_bird_logo_17, R.drawable.refresh_pulling_bird_logo_18,
            R.drawable.refresh_pulling_bird_logo_19, R.drawable.refresh_pulling_bird_logo_20, R.drawable.refresh_pulling_bird_logo_21,
            R.drawable.refresh_pulling_bird_logo_22, R.drawable.refresh_pulling_bird_logo_23, R.drawable.refresh_pulling_bird_logo_24,
            R.drawable.refresh_pulling_bird_logo_25, R.drawable.refresh_pulling_bird_logo_26, R.drawable.refresh_pulling_bird_logo_27,
    };
    @Override
    public void onHeightChanged(ArrowRefreshHeader header, int height, int state) {
////        Log.d("Test","onHeightChanged.visibileHeight = "+header.getVisibleHeight() + "  height = " + height + "   state = " + state);
//        ImageView pulling = (ImageView )header.getHeaderView().findViewById(R.id.listview_header_arrow);
//        final int keep = 120 / 2;
//        final int minHeight = 120 + keep;
//        int index = 0;
//        if (height <= keep) {
////            Log.d("Test","height <= keep = " + index);
//        } else {
//            index = (height - keep) * DRAWABLE_PULL.length / (minHeight - keep);
//            if (index >= DRAWABLE_PULL.length) index = DRAWABLE_PULL.length - 1;
//            Log.d("Test","else = " + index);
//        }
//        mPullingDrawable.setState(new int[]{index});
//        pulling.invalidate();
    }

    @Override
    public void onStateChanged(final ArrowRefreshHeader header, int newState, int oldState) {
//        Log.d("Test","onStateChanged.view="  + header.getHeaderView()  +  "  newState = " + newState + "   oldState = " + oldState);
        TextView tipView = null;
        ImageView imageView = null;
        if (null == tipView) tipView = (TextView) header.getHeaderView().findViewById(R.id.listview_tip_tv);
        if (null == imageView) imageView = (ImageView) header.getHeaderView().findViewById(R.id.listview_header_arrow);
        switch (newState){
            case BaseRefreshHeader.PULL_STATE_NONE:
                header.smoothScrollTo(0);
                break;
            case BaseRefreshHeader.PULL_STATE_NORMAL:

                break;
            case BaseRefreshHeader.PULL_STATE_PULLING:
                tipView.setBackgroundColor(0);
                tipView.setText("");
//                imageView.setImageDrawable(mPullingDrawable);
                imageView.setImageDrawable(header.getHeaderView().getContext().getResources().getDrawable(R.drawable.refresh_loading_01));
                break;
            case BaseRefreshHeader.PULL_STATE_ENABLE:
                setAnimation(imageView);
                break;
            case BaseRefreshHeader.PULL_STATE_LOADING:
                setAnimation(imageView);
                header.smoothScrollTo(120);
                break;
            case BaseRefreshHeader.PULL_STATE_FINISH:
                if (oldState == BaseRefreshHeader.PULL_STATE_LOADING){
                    setTipAnim(tipView,imageView);
                    header.smoothScrollTo(70);
                    header.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            header.setState(BaseRefreshHeader.PULL_STATE_NONE);
                        }
                    },1500);
                } else {
                    header.setState(BaseRefreshHeader.PULL_STATE_NONE);
                }
                break;
            default:
                break;
        }
    }

    private void setAnimation(ImageView imageView){
        Animation mAnimation = imageView.getAnimation();
        if(mAnimation!=null){
            if (mAnimation.hasStarted()) return;
        }
        AnimationDrawable drawable1 = (AnimationDrawable) imageView.getContext().getResources().getDrawable(R.drawable.pull_loading_anim);
        imageView.setImageDrawable(drawable1);
        drawable1.start();
    }

    public String getmTipText() {
        return mTipText;
    }

    public void setmTipText(String mTipText) {
        this.mTipText = mTipText;
    }

    private void setTipAnim(final TextView tipView, final ImageView imageView) {
        tipView.setText(getmTipText());
        ObjectAnimator scalex = ObjectAnimator.ofFloat(tipView, "scaleX", 0f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(tipView, "alpha", 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                imageView.setImageDrawable(null);
                tipView.setBackgroundColor(tipView.getContext().getResources().getColor(com.jcodecraeer.xrecyclerview.R.color.tip_tv_bg));
                tipView.getBackground().setAlpha(200);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
//                animSet.play(alpha);
        animSet.play(scalex).with(alpha);
//        animSet.setInterpolator(new AccelerateDecelerateInterpolator());//先加速后减速
        animSet.setInterpolator(new AnticipateOvershootInterpolator());//回弹
        animSet.setDuration(300);
        animSet.start();
    }
}
