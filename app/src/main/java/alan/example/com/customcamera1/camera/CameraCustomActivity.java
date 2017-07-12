package alan.example.com.customcamera1.camera;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import alan.example.com.customcamera1.MainActivity;
import alan.example.com.customcamera1.R;


public class CameraCustomActivity
        extends AppCompatActivity
        implements PictureCallBack, View.OnClickListener {

    CameraSurfaceView mSurfaceView = null;
    private TextView tvTakeCameraAgain;
    private TextView tvCamera;
    private ImageView ivCancel;
    private byte[] datas;

    private boolean mTakePicture = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initViews();
        initData();
        addListeners();
    }

    public void initViews() {
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        tvTakeCameraAgain = (TextView) findViewById(R.id.tv_take_camera_again);
        tvCamera = (TextView) findViewById(R.id.tv_take_camera);
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);
        initViewParams();
        tvTakeCameraAgain.setVisibility(View.GONE);
    }

    public void initData() {
        mSurfaceView.setBackgroundResource(R.drawable.ic_camera_bg);
    }

    public void addListeners() {
        tvTakeCameraAgain.setOnClickListener(this);
        tvCamera.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
    }

    public int getLayoutId() {
        return R.layout.activity_camera_custom;
    }

    private void initViewParams() {
        LayoutParams params = mSurfaceView.getLayoutParams();
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        Point p = new Point(w_screen, h_screen);
        params.width = p.x;
        params.height = p.y;
        mSurfaceView.setLayoutParams(params);
    }

    private void takePicture() {

        mSurfaceView.doTakePicture(this);
    }

    @Override
    public void setPictureUrl(byte[] data) {
        // 返回图片 数据
        datas = data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_camera_again:
                tvTakeCameraAgain.setVisibility(View.GONE);
                mTakePicture = true;
                tvCamera.setText("拍照");
                mSurfaceView.startPreview();
                break;
            case R.id.tv_take_camera:
                if (mTakePicture) {
                    tvCamera.setText("确定");
                    mTakePicture = false;
                    tvTakeCameraAgain.setVisibility(View.VISIBLE);
                    takePicture();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("camera", datas);
                    setResult(MainActivity.REULT_CODE, intent);
                    finish();
                }
                break;
            case R.id.iv_cancel:
                finish();
                break;
        }
    }
}
