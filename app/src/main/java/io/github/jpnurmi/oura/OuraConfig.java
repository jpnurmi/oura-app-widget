package io.github.jpnurmi.oura;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import io.github.jpnurmi.oura.databinding.OuraConfigBinding;

public class OuraConfig extends Activity {
    private static final String PREFS_NAME = "io.github.jpnurmi.oura.OuraWidget";
    private static final String PREFS_KEY = "token";

    static String loadToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREFS_KEY, null);
    }

    static void saveToken(Context context, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREFS_KEY, text);
        prefs.apply();
    }

    private int getAppWidgetId() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return AppWidgetManager.INVALID_APPWIDGET_ID;
        }
        return extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        OuraConfigBinding binding = OuraConfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int appWidgetId = getAppWidgetId();
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        } else {
            binding.tokenEdit.setText(loadToken(OuraConfig.this));
        }

        binding.tokenEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Context context = OuraConfig.this;
                saveToken(context, charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = OuraConfig.this;
                OuraWidget.updateAppWidget(context, appWidgetId);

                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}