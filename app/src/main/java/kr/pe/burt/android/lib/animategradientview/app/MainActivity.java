package kr.pe.burt.android.lib.animategradientview.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        agv.startTransitionXAnimation(1000, 1);
    }
}
