package jp.techacademy.oohara.yoshiyuki.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    Cursor cursor = null;

    Timer mTimer;
    double mTimerSec = 0.0;

    Handler mHandler = new Handler();

    Button mNextButton;
    Button mPrevButton;
    Button mPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPlayButton = (Button) findViewById(R.id.play_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 次の画像を表示、最後なら最初の画像を表示
                if (cursor.moveToNext()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    // URIで取得した画像を表示
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                } else {
                    cursor.moveToFirst();
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    // URIで取得した画像を表示
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 前の画像を表示、最初なら最後の画像を表示
                if (cursor.moveToPrevious()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    // URIで取得した画像を表示
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                } else {
                    cursor.moveToLast();
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    // URIで取得した画像を表示
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageURI(imageUri);
                }
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer == null) {
                    // ボタンのテキストを停止に
                    mPlayButton.setText("停止");

                    // 進む、戻るボタン無効化
                    mNextButton.setEnabled(false);
                    mPrevButton.setEnabled(false);

                    // タイマー開始
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mTimerSec += 2.0;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 次の画像を表示、最後なら最初の画像を表示
                                    if (cursor.moveToNext()) {
                                        // indexからIDを取得し、そのIDから画像のURIを取得する
                                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                        Long id = cursor.getLong(fieldIndex);
                                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                        // URIで取得した画像を表示
                                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                        imageView.setImageURI(imageUri);
                                    } else {
                                        cursor.moveToFirst();
                                        // indexからIDを取得し、そのIDから画像のURIを取得する
                                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                        Long id = cursor.getLong(fieldIndex);
                                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                        // URIで取得した画像を表示
                                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                        imageView.setImageURI(imageUri);
                                    }
                                }
                            });
                        }
                    }, 2000, 2000);
                } else {
                    // ボタンのテキストを再生に
                    mPlayButton.setText("再生");

                    // 進む、戻るボタン有効化
                    mNextButton.setEnabled(true);
                    mPrevButton.setEnabled(true);

                    // タイマー停止
                    mTimerSec = 0.0;

                    mTimer.cancel();
                    mTimer = null;
                }
            }
        });

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();

                    if (mNextButton.isEnabled() == false) {
                        // 進む、戻るボタン有効化
                        mNextButton.setEnabled(true);
                        mPrevButton.setEnabled(true);
                        mPlayButton.setEnabled(true);
                    }
                } else {
                    Toast.makeText(this,"AutoSlideshowAppを使用できません\n許可してください",Toast.LENGTH_SHORT).show();
                    // 許可されていないので許可ダイアログを表示する
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);

                    // 進む、戻るボタン無効化
                    mNextButton.setEnabled(false);
                    mPrevButton.setEnabled(false);
                    mPlayButton.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
        cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            // URIで取得した画像を表示
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }
}
