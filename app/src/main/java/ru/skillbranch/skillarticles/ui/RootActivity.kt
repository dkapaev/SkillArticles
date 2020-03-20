package ru.skillbranch.skillarticles.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_bottombar.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setupToolbar()

        btn_like.setOnClickListener {
            Snackbar.make(coordinator_container, "test", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (toolbar.childCount > 2 && toolbar.getChildAt(2) is ImageView) {
            val toolbarLogo = toolbar.getChildAt(2) as ImageView
            toolbarLogo.scaleType = ImageView.ScaleType.CENTER_CROP
            val layoutParams = toolbarLogo.layoutParams as? Toolbar.LayoutParams
            layoutParams?.apply {
                width = dpToIntPx(40)
                height = dpToIntPx(40)
                marginEnd = dpToIntPx(16)
                toolbarLogo.layoutParams = this
            }
        }
    }
}
