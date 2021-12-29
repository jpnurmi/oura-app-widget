package io.github.jpnurmi.oura;

import android.content.Context;
import android.content.Intent;

public class OuraIntent {
    static private String OURA_INTENT = "com.ouraring.oura";

    static Intent getLaunchIntent(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(OURA_INTENT);
    }
}
