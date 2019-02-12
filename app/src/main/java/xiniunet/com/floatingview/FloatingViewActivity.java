package xiniunet.com.floatingview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import xiniunet.com.floatingview.base.BaseActivity;
import xiniunet.com.floatingview.service.FloatingService;

/**
 * @author yangxia
 * @since 11/2/19 下午2:48
 */
public class FloatingViewActivity extends BaseActivity {

    /**
     * 悬浮框权限设置
     */
    private static final  int PERMISSION_REQUEST_CODE = 0;
    private Chronometer chronometer;
    private ImageView ivMinmize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_view);
        doInit(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        chronometer = findViewById(R.id.chronometer);
        ivMinmize = findViewById(R.id.iv_minmize);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        //最小化操作
        ivMinmize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissonCheck();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        //开启定时器
        chronometer.setBase(0L);
        chronometer.start();
    }

    /**
     * 开启悬浮窗权限检查
     */
    public void permissonCheck(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断系统版本
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                Intent intent =new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                this.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            } else {
                createFloatingView();
            }
        } else {
            createFloatingView();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mServiceConnection !=null){
            unbindService(mServiceConnection);
            mServiceConnection = null;
        }
    }

    private ServiceConnection mServiceConnection = null;
    public void createFloatingView(){
        moveTaskToBack(true);
        mServiceConnection  =   new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // 获取服务的操作对象
                FloatingService.FloatingBinder binder = (FloatingService.FloatingBinder) service;
                binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
       Intent  intentService = new Intent(this, FloatingService.class);
       intentService.putExtra("time",chronometer.getBase());
       bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                //授权成功后开启悬浮窗
                createFloatingView();
            }
        }
    }
}
