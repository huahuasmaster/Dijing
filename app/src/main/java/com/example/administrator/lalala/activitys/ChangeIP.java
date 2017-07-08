package com.example.administrator.lalala.activitys;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.lalala.R;

public class ChangeIP extends AppCompatActivity {
    private EditText editTextNewip;
    private Button changeipBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ip);
        editTextNewip = (EditText) findViewById(R.id.editText_newip);
        changeipBtn = (Button) findViewById(R.id.changeip_btn);
        editTextNewip.setText(Login.Usip);
        changeipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.changeIP(editTextNewip.getText().toString());
                finish();
            }
        });
    }
}
