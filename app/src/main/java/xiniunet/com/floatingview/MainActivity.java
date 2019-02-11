package xiniunet.com.floatingview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xiniunet.com.floatingview.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doInit(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        btn = findViewById(R.id.btn);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MainActivity.this,FloatingViewActivity.class));
            }
        });
    }


}
