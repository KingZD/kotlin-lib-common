package com.zed.view.status;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zed.image.GlideUtils;
import com.zed.view.R;

/**
 * Created by zed on 2018/4/9.
 */

public class StatusView extends FrameLayout implements StatusLinster {
    ImageView longImg;
    TextView lodingTxt;

    @Override
    public void changeStatus(Status status) {
        switch (status) {
            case EMPTY:
                break;
            case EXCEPTER:
                break;
            case NETERROR:
                break;
        }
    }

    @Override
    public void changeStatus(Status status, int imgId) {
        changeStatus(status);
        longImg.setImageResource(imgId);
    }

    @Override
    public void changeStatus(Status status, int imgId, int stringId) {
        changeStatus(status);
        longImg.setImageResource(imgId);
        lodingTxt.setText(stringId);
    }

    @Override
    public void changeStatus(Status status, int imgId, String txt) {
        changeStatus(status);
        longImg.setImageResource(imgId);
        lodingTxt.setText(txt);
    }

    enum Status {
        EMPTY, NETERROR, EXCEPTER
    }

    public StatusView(@NonNull Context context) {
        super(context);
        init();
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.view_status, this);
        longImg = findViewById(R.id.ivLodingIvon);
        lodingTxt = findViewById(R.id.tvLodingTxt);
        //默认的加载动画
        GlideUtils.Builder.with(getContext()).load(R.drawable.bp).into(longImg);
    }
}
