package ru.skillbranch.skillarticles.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_bottombar.*
import kotlinx.android.synthetic.main.layout_submenu.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.viewmodels.*

class RootActivity : AppCompatActivity() {

    private lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setupToolbar()
        setupBottomBar()
        setupSubmenu()

        val vmFactory = ViewModelFactory("0")
        viewModel = ViewModelProvider(this, vmFactory).get(ArticleViewModel::class.java)
        viewModel.observeState(this) {
            renderUi(it)
        }

        viewModel.observeNotifications(this) {
            renderNotification(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.article_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchMenuItem.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        // overriding maxWidth here is a workaround that helps SearchView to expand full width
        searchView.maxWidth = Int.MAX_VALUE

        val articleState = viewModel.currentState
        if (articleState.isSearch) searchMenuItem.expandActionView() else searchMenuItem.collapseActionView()
        searchView.setQuery(articleState.searchQuery, false)

        // prevent software keyboard from popping up when activity is recreated
        searchView.clearFocus()

        searchMenuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                viewModel.handleSearchMode(true)
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                viewModel.handleSearchMode(false)
                // invalidateOptionsMenu() here is a workaround to avoid search icon being replaced with settings icon
                invalidateOptionsMenu()
                return true
            }
        })

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // clearFocus() here is a workaround to avoid issues with some emulators
                // and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearch(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSubmenu() {
        btn_text_up.setOnClickListener { viewModel.handleUpText() }
        btn_text_down.setOnClickListener { viewModel.handleDownText() }
        switch_mode.setOnClickListener { viewModel.handleNightMode() }
    }

    private fun setupBottomBar() {
        btn_like.setOnClickListener { viewModel.handleLike() }
        btn_bookmark.setOnClickListener { viewModel.handleBookmark() }
        btn_share.setOnClickListener { viewModel.handleShare() }
        btn_settings.setOnClickListener { viewModel.handleToggleMenu() }
    }

    private fun renderUi(data: ArticleState) {
        // bind submenu state
        btn_settings.setChecked(data.isShowMenu)
        if (data.isShowMenu) submenu.open() else submenu.close()

        // bind article person data
        btn_like.setChecked(data.isLike)
        btn_bookmark.setChecked(data.isBookmark)

        // bind submenu views
        switch_mode.setChecked(data.isDarkMode)
        delegate.localNightMode = if (switch_mode.isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        if (data.isBigText) {
            tv_text_content.textSize = 18f
            btn_text_up.setChecked(true)
            btn_text_down.setChecked(false)
        } else {
            tv_text_content.textSize = 14f
            btn_text_up.setChecked(false)
            btn_text_down.setChecked(true)
        }

        // bind content
        tv_text_content.text = if (data.isLoadingContent) "loading" else data.content.first() as String

        // bind toolbar
        toolbar.title = data.title ?: "loading"
        toolbar.subtitle = data.category ?: "loading"
        if (data.categoryIcon != null) toolbar.logo = getDrawable(data.categoryIcon as Int)
    }

    private fun renderNotification(notify: Notify) {
        val snackbar = Snackbar.make(coordinator_container, notify.message, Snackbar.LENGTH_LONG)
            .setAnchorView(bottombar)

        when (notify) {
            is Notify.TextMessage -> { /*nothing*/ }

            is Notify.ActionMessage -> {
                snackbar.setActionTextColor(getColor(R.color.color_accent_dark))
                snackbar.setAction(notify.actionLabel) {
                    notify.actionHandler?.invoke()
                }
            }

            is Notify.ErrorMessage -> {
                with(snackbar) {
                    setBackgroundTint(getColor(R.color.design_default_color_error))
                    setTextColor(getColor(android.R.color.white))
                    setActionTextColor(getColor(android.R.color.white))
                    setAction(notify.errLabel) {
                        notify.errHandler?.invoke()
                    }
                }
            }
        }

        snackbar.show()
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
