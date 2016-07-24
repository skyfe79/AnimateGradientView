package kr.pe.burt.android.lib.animategradientview.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.EditText;

import kr.pe.burt.android.lib.animategradientview.AnimateGradientView;
import kr.pe.burt.android.lib.keyboardedittext.KeyboardEditText;

public class EditTextActivity extends AppCompatActivity {

    AnimateGradientView agv = null;
    KeyboardEditText editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        agv = (AnimateGradientView)findViewById(R.id.agv);
        editText = (KeyboardEditText)findViewById(R.id.editText);
        editText.setOnKeyboardListener(new KeyboardEditText.OnKeyboardListener() {
            @Override
            public void onKeyboardShown(EditText editText) {
                agv.startInfiniteRotationAnimation(500);
            }

            @Override
            public void onKeyboardHidden(EditText editText) {
                agv.stopAnimation();
            }
        });
    }
}
