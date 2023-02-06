package io.smileyjoe.icons.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.db.Database;
import io.smileyjoe.icons.listener.IconLoaded;
import io.smileyjoe.icons.listener.MetaLoaded;

public class Api {

    private static final String VERSION_NUMBER = "7.1.96";
    private static final String URL_ALL = "https://cdn.jsdelivr.net/npm/@mdi/svg@{version}/meta.json".replace("{version}", VERSION_NUMBER);
    private static final String URL_ICON = "https://cdn.jsdelivr.net/npm/@mdi/svg@{version}/svg/{name}.svg".replace("{version}", VERSION_NUMBER);

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
     * @param icon icon data object
     * @param listener callback
     */
    public static void getIcon(Context context, IconData icon, final IconLoaded listener) {
        Ion.with(context)
                .load(URL_ICON.replace("{name}", icon.getName()))
                .setHandler(null)
                .asString()
                .setCallback((exception, svgString) -> {

                    // There is no error and we have a response //
                    if(exception == null && !TextUtils.isEmpty(svgString)) {
                        String path = getPathFromSvg(svgString);
                        icon.setPath(path);
                    }

                    if (icon != null && icon.isValid()) {
                        Database.getIconData().insert(icon);

                        if (listener != null) {
                            listener.onIconLoaded(Icon.fromPath(context, icon));
                        }
                    } else {
                        if (listener != null) {
                            listener.onIconLoaded(null);
                        }
                    }
                });
    }

    /**
     * Get the path from the svg response
     *
     * @param svgString svg from cdn
     * @return path from svg
     */
    private static String getPathFromSvg(String svgString){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(svgString));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("path")) {
                    return xpp.getAttributeValue(null, "d");
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException | NullPointerException e) {
        }

        return null;
    }
}
