package io.javac.example.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.appmsg.AppMsg;

import io.javac.example.R;

/**
 * Created by Pencilso on 2017/7/24.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private AppMsg appMsg;
    private ProgressDialog progressDialog;

    public void appToast(Object message) {
        if (appMsg == null) {
            AppMsg.Style style = new AppMsg.Style(1000, R.color.colorPrimaryDark);
            appMsg = AppMsg.makeText(this, message.toString(), style);
            appMsg.setLayoutGravity(Gravity.CENTER_VERTICAL);
        } else {
            appMsg.setText(message.toString());
        }
        appMsg.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    public void initView() {
    }

    public void initData() {
    }

    public void initEvent() {
    }

    public void registAllView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                viewchild.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void startActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }

    public void setDialog(String str) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(str);
        } else progressDialog.setMessage(str);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
