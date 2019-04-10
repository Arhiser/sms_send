package com.arhiser.sms;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.arhiser.sms.service.SmsServiceConnection;

public class MainActivity extends AppCompatActivity {

    SmsServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection = new SmsServiceConnection(this);

        findViewById(R.id.send).setOnClickListener(v -> {
            EditText phone = findViewById(R.id.phone);
            EditText body = findViewById(R.id.text);

            String phoneStr = phone.getText().toString().trim();
            String bodyStr = body.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneStr) && !TextUtils.isEmpty(bodyStr)) {
                if (connection.isConnected()) {
                    if (connection.hasPermissions()) {
                        sendSms(phoneStr, bodyStr);
                    } else {
                        connection.requestPermissionActivity(this);
                    }
                } else if (connection.getModuleVersion() > 0) {
                    connection.connect(() -> sendSms(phoneStr, bodyStr));
                } else {
                    Toast.makeText(this, "Отсутствует модуль смс", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendSms(String phone, String body) {
        if (connection.sendSms(phone, body)) {
            Toast.makeText(this, "СМС отправлено", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connection.getModuleVersion() > 0
                && !connection.isConnected()) {
            connection.connect(null);
        }
    }
}
