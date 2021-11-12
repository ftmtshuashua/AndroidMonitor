package com.acap.demo.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/27 16:29
 * </pre>
 */
public class LocationUtils {

    public static void getLocation(Context context){
        LocationManager systemService = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
