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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

/**
 * @author yangxia
 * @since 21/1/19 下午1:30
 * 语音聊天 窗口最小化处理
 */
public class FloatingService extends Service {

    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View view;
    private TextView tvStatus;
    private Chronometer chronometer;
    /**
     * 更新悬浮框上的文字
     */
    public static final String UPDATE_TEXT_FLAG  = "updateTextFlag";

    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
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
//        layoutParams.x = (int) (ScreenUtil.getDisplayWidth()*0.8);
        layoutParams.y = 200;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView();
    }

//    /**
//     *  eventBus事件处理 在ui线程执行
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventBusFunc(String event) {
//        if (event ==null || event.equals("") || chronometer ==null){
//            return;
//        }
//        if (event.startsWith(UPDATE_TEXT_FLAG)){
//            long  time  = Long.valueOf( event.replace(UPDATE_TEXT_FLAG+"-",""));
//            tvStatus.setVisibility(View.GONE);
//            chronometer.setVisibility(View.VISIBLE);
//            chronometer.setBase(time);
//            chronometer.start();
//        }
//
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        showFloatingWindow(intent);
        return new FloatingBinder();
    }

    public class FloatingBinder extends Binder {
        public FloatingService getService() {
            return FloatingService.this;
        }
    }


    private void showFloatingWindow(Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
//            addView(intent);
        }
    }
    public   void  removeView(){
        if (windowManager !=null && view !=null){
            isStarted = false;
            windowManager.removeView(view);
        }
    }

//    public void addView(Intent intent){
//        view = LayoutInflater.from(this).inflate(R.layout.view_audio_floating,null);
//        tvStatus = view.findViewById(R.id.tv_status);
//        chronometer = view.findViewById(R.id.avchat_audio_time);
//        windowManager.addView(view, layoutParams);
//        if (intent !=null){
//            long time = intent.getLongExtra("time",0L);
//            if (time !=0L){
//                tvStatus.setVisibility(View.GONE);
//                chronometer.setVisibility(View.VISIBLE);
//                chronometer.setBase(time);
//                chronometer.start();
//            }
//        }
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 Intent intent = new Intent();
//                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setClass(AudioFloatingService.this, AVChatActivity.class);
//                startActivity(intent);
////                removeView();
//            }
//        });
//
//        view.setOnTouchListener(new FloatingOnTouchListener());
//    }

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
