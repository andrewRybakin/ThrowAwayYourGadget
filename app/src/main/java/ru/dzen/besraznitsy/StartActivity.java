package ru.dzen.besraznitsy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class StartActivity extends Activity {

    private static final String LOG_TAG = "StartActivity";
    private EditText eT;
    private ImageButton goButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        eT = (EditText) findViewById(R.id.name_text_edit);
        goButton = (ImageButton) findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartActivity.this, GameActivity.class);
                i.putExtra(GameActivity.EXTRA_NAME, eT.getText());
                startActivity(i);
                finish();
            }
        });
        eT.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    goButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
