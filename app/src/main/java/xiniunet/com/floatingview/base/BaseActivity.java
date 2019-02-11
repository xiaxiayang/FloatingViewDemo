package xiniunet.com.floatingview.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author yangxia
 * @since 11/2/19 下午2:55
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void  doInit(Bundle savedInstanceState){
        initView();
        bindEvent();
        initData();
    }


    protected void  initView(){

    }

    protected void  bindEvent(){

    }

    protected void  initData(){

    }
}
