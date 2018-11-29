package com.yernarkt.themoviedb.util.transition

import android.transition.AutoTransition
import android.transition.Transition

class FadeOutTransition private constructor()// force callers to call the factory method to instantiate this class
    : AutoTransition() {
    companion object {

        private const val FADE_OUT_DURATION = 250

        fun withAction(finishingAction: Transition.TransitionListener): Transition {
            val transition = AutoTransition()
            transition.duration = FADE_OUT_DURATION.toLong()
            transition.addListener(finishingAction)
            return transition
        }
    }

}
