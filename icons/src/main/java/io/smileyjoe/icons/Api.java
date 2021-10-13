package io.smileyjoe.icons;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;

import java.util.List;

class Api {

    private static final String URL_ALL = "https://cdn.jsdelivr.net/npm/@mdi/svg@6.2.95/meta.json";
    private static final String URL_ICON = "https://materialdesignicons.com/api/icon/{id}";

    public static void getAll(Context context, MetaLoaded listener){
        Ion.with(context)
                .load(URL_ALL)
                .setHandler(null)
                .as(new TypeToken<List<IconData>>(){})
                .setCallback((exception, icons) -> {
                    Log.d("IconThings", "Icons: " + icons.size());
                    Database.getIconData().insertAll(icons);

                    listener.onMetaLoaded();
                });
    }

    public static void getIcon(Context context, String id, final IconLoaded listener){
        Ion.with(context)
                .load(URL_ICON.replace("{id}", id))
                .setHandler(null)
                .as(new TypeToken<IconData>(){})
                .setCallback((exception, icon) -> {
                    Database.getIconData().insert(icon);

                    if(listener != null) {
                        listener.onIconLoaded(icon);
                    }
                });
    }
}
