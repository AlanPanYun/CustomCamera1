package alan.example.com.customcamera1.camera;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    Context mContext;
    SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private boolean isPreviewing;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        doStartPreview(mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        doStopCamera();
    }

    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }


    public void doStartPreview(SurfaceHolder holder) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {

            List<Camera.Size> mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

            Camera.Size mPreviewSize = CameraSize.getOptimalSize(
                    mSupportedPreviewSizes,
                    displayMetrics.widthPixels,
                    displayMetrics.heightPixels
            );

            Camera.Parameters mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);

            mParams.setPictureSize(mPreviewSize.width, mPreviewSize.height); // 设置图片尺寸为手机接近匹配尺寸
            mParams.setPreviewSize(mPreviewSize.width, mPreviewSize.height); // 设置预览尺寸为手机接近匹配尺寸

            mCamera.setDisplayOrientation(90);

            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }

            mCamera.setParameters(mParams);

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isPreviewing = true;

        }
    }

    public void doTakePicture(final PictureCallBack pictureCallBack) {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap b = null;
                    if (null != data) {
                        b = BitmapFactory.decodeByteArray(data, 0, data.length);
                        mCamera.stopPreview();
                        isPreviewing = false;
                        pictureCallBack.setPictureUrl(data);
                    }
                }
            });

        }
    }

    public void startPreview() {
        if (mCamera == null) {
            return;
        }
        mCamera.startPreview();
        isPreviewing = true;
    }

}
