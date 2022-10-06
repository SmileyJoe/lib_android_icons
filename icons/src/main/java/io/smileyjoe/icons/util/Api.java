package io.smileyjoe.icons.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;

import java.util.List;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.db.Database;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.listener.MetaLoaded;

public class Api {

    private static final String URL_ALL = "https://cdn.jsdelivr.net/npm/@mdi/svg@6.2.95/meta.json";
    private static final String URL_ICON = "https://materialdesignicons.com/api/icon/{id}";

    /**
     * Get all the icon meta data, this includes the name and id, not the path itself
     *
     * @param context context
     * @param listener callback
     */
    public static void getAll(Context context, MetaLoaded listener) {
        Ion.with(context)
                .load(URL_ALL)
                .setHandler(null)
                .as(new TypeToken<List<IconData>>() {
                })
                .setCallback((exception, icons) -> {
                    if(icons != null && icons.size() > 0) {
                        Database.getIconData().insertAll(icons);
                    }

                    if (listener != null) {
                        listener.onMetaLoaded();
                    }
                });
    }

    /**
     * Get the icon path
     *
     * @param context context
     * @param id icon id
     * @param listener callback
     */
    public static void getIcon(Context context, String id, final IconLoaded listener) {
        Ion.with(context)
                .load(URL_ICON.replace("{id}", id))
                .setHandler(null)
                .as(new TypeToken<IconData>() {
                })
                .setCallback((exception, icon) -> {
                    if(icon != null && icon.isValid()) {
                        Database.getIconData().insert(icon);

                        if (listener != null) {
                            listener.onIconLoaded(Icon.fromPath(context, icon));
                        }
                    } else {
                        if(listener != null){
                            listener.onIconLoaded(null);
                        }
                    }
                });
    }
}
