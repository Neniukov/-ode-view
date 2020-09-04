package com.allocaterite.allocaterite.core.base

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.allocaterite.allocaterite.R
import com.allocaterite.allocaterite.ui.ProgressDialog
import com.allocaterite.allocaterite.utils.extenstions.withBackAnimation
import com.allocaterite.allocaterite.utils.extenstions.withBackStackAnimation
import com.allocaterite.allocaterite.utils.extenstions.withNextAnimation

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutResId: Int
    private var errorDialog: AlertDialog? = null

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.flContainer)
        if (fragment is BaseFragment && fragment.onBackPressed()) return
        super.onBackPressed()
    }

    fun showError(
        layoutId: Int,
        positiveButtonLabel: Int,
        negativeButtonLabel: Int,
        buttonClickListener: (
            activity: BaseActivity,
            dialog: DialogInterface, which: Int
        ) -> Unit
    ) {
        errorDialog = AlertDialog.Builder(this)
            .setView(layoutId)
            .setCancelable(false)
            .setPositiveButton(
                positiveButtonLabel
            ) { dialog, which -> buttonClickListener(this, dialog, which) }
            .setNegativeButton(
                negativeButtonLabel
            ) { dialog, which -> buttonClickListener(this, dialog, which) }
            .create()

        if (errorDialog?.isShowing == true) {
            errorDialog?.dismiss()
            errorDialog = null
        }
        errorDialog?.show()
    }

    fun toBack(
        fragment: Fragment, @IdRes containerId: Int = R.id.flContainer,
        withAnimation: Boolean = true
    ) =
        supportFragmentManager.transaction {
            if (withAnimation) withBackAnimation()
            replace(containerId, fragment)
        }

    fun toNext(
        fragment: Fragment,
        @IdRes
        containerId: Int = R.id.flContainer,
        withAnimation: Boolean = true,
        withBackStack: Boolean = false,
        fragmentId: String? = null,
        bundle: Bundle? = null
    ) = supportFragmentManager.beginTransaction().apply {
        if (withAnimation)
            if (withBackStack) withBackStackAnimation()
            else withNextAnimation()
        bundle?.let { fragment.arguments = bundle }
        if (withBackStack) {
            addToBackStack(null)
            add(containerId, fragment)
        } else
            replace(containerId, fragment)
    }.commit()

    fun showProgress() {
        hideProgress()
        progressDialog = ProgressDialog.newInstance("Loading...")
        try {
            progressDialog?.show(supportFragmentManager, ProgressDialog.TAG)
        } catch (ignored: Exception) {
        }
    }

    fun hideProgress() {
        if (progressDialog != null) {
            try {
                progressDialog?.dismiss()
            } catch (ignored: Exception) {
            } finally {
                progressDialog = null
            }
        }
    }

    companion object {
        const val KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID"
    }

    open fun setColorStatusBar(color: Int = Color.WHITE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
            window.statusBarColor = color
        }
    }

    fun hideKeyboard(): Boolean {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        return false
    }
}