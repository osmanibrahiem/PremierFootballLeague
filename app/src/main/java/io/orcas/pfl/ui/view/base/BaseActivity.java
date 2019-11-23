package io.orcas.pfl.ui.view.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;

import io.orcas.pfl.R;
import io.orcas.pfl.helper.NetworkUtils;
import io.orcas.pfl.helper.PermissionHelper;

/**
 * Created by Osman Ibrahiem on 22/11/2019.
 */

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity implements PermissionHelper.PermissionsListener {

    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private T mViewDataBinding;
    private V mViewModel;

    private PermissionHelper permissionHelper;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionHelper = new PermissionHelper(this).setListener(this);
        performDataBinding();
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.executePendingBindings();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public boolean hasPermission(String permission) {
        return hasPermissions(new String[]{permission});
    }

    public boolean hasPermissions(String[] permissions) {
        return permissionHelper.checkPermission(permissions);
    }

    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        permissionHelper.requestPermission(permissions, requestCode);
    }

    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(int requestCode) {
    }

    @Override
    public void onPermissionRejectedManyTimes(@NonNull List<String> rejectedPerms, int requestCode) {
        List<String> permission_name = new ArrayList<>();
        for (String permission : rejectedPerms) {
            permission_name.add(PermissionHelper.getNameFromPermission(permission));
        }
        String res = TextUtils.join(",", permission_name);
        showNeedPermissionAlertDialog(res, rejectedPerms.toArray(new String[]{}), requestCode);
    }

    private void showNeedPermissionAlertDialog(String permission_name, String[] permissions, int requestCode) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_permission_required))
                .setMessage(getString(R.string.message_need_permission, permission_name))
                .setPositiveButton(getString(R.string.btn_ok), (dialogInterface, i) -> requestPermissionsSafely(permissions, requestCode))
                .setNegativeButton(getString(R.string.btn_no), (dialogInterface, i) -> BaseActivity.this.finish())
                .show();
    }

}
