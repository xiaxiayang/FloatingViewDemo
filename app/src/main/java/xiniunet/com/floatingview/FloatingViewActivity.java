package xiniunet.com.floatingview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import xiniunet.com.floatingview.base.BaseActivity;

/**
 * @author yangxia
 * @since 11/2/19 下午2:48
 */
public class FloatingViewActivity extends BaseActivity {

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
                moveTaskToBack(true);
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
}
