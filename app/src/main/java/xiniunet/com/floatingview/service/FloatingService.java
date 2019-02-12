package xiniunet.com.floatingview.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;

import xiniunet.com.floatingview.FloatingViewActivity;
import xiniunet.com.floatingview.R;

/**
 * @author yangxia
 * @since 11/2/19 下午1:30
 * 窗口最小化处理
 */
public class FloatingService extends Service {

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View view;
    private Chronometer chronometer;

    @Override
    public void onCreate() {
        super.onCreate();
        initWindowManger();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager !=null && view !=null){
            windowManager.removeView(view);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        addView(intent);
        return new FloatingBinder();
    }

    public class FloatingBinder extends Binder {
        public FloatingService getService() {
            return FloatingService.this;
        }
    }

    /**
     * 设置 WindowManger
     */
    public void initWindowManger(){
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 200;
        layoutParams.y = 200;
    }

    /**
     * 添加悬浮窗布局
     * @param intent
     */
    public void addView(Intent intent){
        //6.0 以后需要权限，没有权限不处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            return;
        }
        view = LayoutInflater.from(this).inflate(R.layout.view_floating,null);
        chronometer = view.findViewById(R.id.chronometer);
        windowManager.addView(view, layoutParams);
        if (intent !=null){
            long time = intent.getLongExtra("time",0L);
            chronometer.setBase(time);
            chronometer.start();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(FloatingService.this, FloatingViewActivity.class);
                startActivity(intent);
            }
        });

        view.setOnTouchListener(new FloatingOnTouchListener());
    }

    /**
     * 悬浮窗拖动设置
     */
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        private boolean hasMove;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    hasMove = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    if (movedX > 10 || movedY>10){
                        hasMove = true;
                    }
                    break;
                default:
                    break;
            }
            if (hasMove){
                return true;
            }else {
                return false;
            }

        }
    }

}
