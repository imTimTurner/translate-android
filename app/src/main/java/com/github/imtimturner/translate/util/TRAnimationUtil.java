package com.github.imtimturner.translate.util;

import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Вспомогательный класс содержащий методы используемые для анимации
 * */
public class TRAnimationUtil {

    public static void defaultFadeIn(View view, boolean animated) {
        if (view.getVisibility() == View.VISIBLE){
            return;
        }

        if (animated) {
            view.startAnimation(AnimationUtils.loadAnimation(
                    view.getContext(), android.R.anim.fade_in));
        } else {
            view.clearAnimation();
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void defaultFadeOut(View view, boolean animated) {
        if (view.getVisibility() == View.GONE){
            return;
        }

        if (animated) {
            view.startAnimation(AnimationUtils.loadAnimation(
                    view.getContext(), android.R.anim.fade_out));
        } else {
            view.clearAnimation();
        }
        view.setVisibility(View.GONE);
    }
}
