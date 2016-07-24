package kr.pe.burt.android.lib.animategradientview.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import kr.pe.burt.android.lib.animategradientview.AnimateGradientView;

public class MainActivity extends AppCompatActivity {

    AnimateGradientView agv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agv = (AnimateGradientView)findViewById(R.id.agv);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            Intent intent = new Intent(this, EditTextActivity.class);
            startActivity(intent);
        }
        return super.onTouchEvent(event);
    }
}
