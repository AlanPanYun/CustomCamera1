package alan.example.com.customcamera1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import alan.example.com.customcamera1.camera.CameraCustomActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    public static final int REULT_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btn_camera);
        imageView = (ImageView) findViewById(R.id.iv_camera);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivityForResult(new Intent(this, CameraCustomActivity.class), REULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REULT_CODE && data != null) {
            byte[] cameras = data.getByteArrayExtra("camera");
            Bitmap bitmap = BitmapFactory.decodeByteArray(cameras, 0, cameras.length);
            imageView.setImageBitmap(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
