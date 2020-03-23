package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class ArticleSubmenuBehavior<V : View>(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attributeSet) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {
        return dependency is Bottombar
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {
        if (dependency is Bottombar) {
            // ArticleSubmenu width is approximately 4 times bigger than BottomBar height
            child.translationX = dependency.translationY * 4
            return true
        } else {
            return super.onDependentViewChanged(parent, child, dependency)
        }
    }
}
