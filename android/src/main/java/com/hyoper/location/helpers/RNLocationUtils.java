package com.hyoper.location.helpers;

import android.location.Location;
import android.os.Build;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import static com.hyoper.location.helpers.RNLocationConstants.Error;
import static com.hyoper.location.helpers.RNLocationConstants.ErrorMessage;

import java.util.function.Consumer;

public class RNLocationUtils {
    public static String name = "RNLocation";
    private static Consumer<ReadableArray> onChange = null;
    private static Consumer<ReadableMap> onError = null;


    public static void setName(String _name) {
        name = _name;
    }

    public static void setEmitter(Consumer<ReadableArray> _onChange, Consumer<ReadableMap> _onError) {
        onChange = _onChange;
        onError = _onError;
    }

    public static void emitChange(ReadableArray body) {
        if (onChange == null) return;

        onChange.accept(body);
    }

    public static void emitError(String code, String message, boolean critical) {
        if (onError == null) return;

        WritableMap map = Arguments.createMap();
        map.putString("code", code);
        map.putString("message", message);
        map.putBoolean("critical", critical);

        onError.accept(map);
    }

    public static void emitError(String code, String message) {
        emitError(code, message, false);
    }

    public static void handleException(Exception exception, @Nullable Promise promise) {
        boolean hasPromise = promise != null;
        String message = (exception.getMessage() != null) ? exception.getMessage() : ErrorMessage.UNKNOWN;

        if (exception instanceof RNLocationException e) {
            if (hasPromise) promise.reject(e.code, message);
            else emitError(e.code, message, e.critical);
        } else {
            if (hasPromise) promise.reject(Error.UNKNOWN, message);
            else emitError(Error.UNKNOWN, message);
        }
    }

    public static void handleException(Exception exception) {
        handleException(exception, null);
    }

    public static WritableMap locationToMap(Location location) {
        WritableMap map = Arguments.createMap();

        map.putDouble("latitude", location.getLatitude());
        map.putDouble("longitude", location.getLongitude());
        map.putDouble("accuracy", location.getAccuracy());
        map.putDouble("altitude", location.getAltitude());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            map.putDouble("altitudeAccuracy", location.getVerticalAccuracyMeters());
        } else {
            map.putDouble("altitudeAccuracy", 0.0);
        }
        map.putDouble("course", location.getBearing());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            map.putDouble("courseAccuracy", location.getBearingAccuracyDegrees());
        } else {
            map.putDouble("courseAccuracy", 0.0);
        }
        map.putDouble("speed", location.getSpeed());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            map.putDouble("speedAccuracy", location.getSpeedAccuracyMetersPerSecond());
        } else {
            map.putDouble("speedAccuracy", 0.0);
        }
        map.putDouble("timestamp", location.getTime());
        map.putBoolean("fromMockProvider", location.isFromMockProvider());

        return map;
    }

    public static void reset() {
        name = "RNLocation";
        onChange = null;
        onError = null;
    }
}
