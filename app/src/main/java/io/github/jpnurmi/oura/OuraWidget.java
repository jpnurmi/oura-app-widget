package io.github.jpnurmi.oura;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class OuraWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, int appWidgetId) {
        CharSequence token = OuraConfig.loadToken(context);
        OuraService.fetchScore(context, token, new OuraService.OnScore() {
            @Override
            public void onScore(int score) {
                setTextViewText(context, appWidgetId, Integer.toString(score));
            }
            @Override
            public void onError(Object error) {
                setTextViewText(context, appWidgetId, error.toString());
            }
        });
    }

    static private void setTextViewText(Context context, int appWidgetId, String text) {
        Intent intent = OuraIntent.getLaunchIntent(context);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, flags);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.oura_widget);
        views.setTextViewText(R.id.oura_text, text);
        views.setOnClickPendingIntent(R.id.oura_text, pendingIntent);

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetId);
        }
    }
}