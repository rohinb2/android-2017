package org.hackillinois.android.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.glxn.qrgen.android.QRCode;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.service.EventNotifierJob;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class Utils {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = null;
		if (connectivityManager != null) {
			activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		}
		return (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());
	}

	public static void setFullScreen(Window window) {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(window.getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
	}

	public static Bitmap getQRCodeBitmap(Context context, int id, String identifier) {
		String qrFormattedString = String.format(Locale.US, "hackillinois://qrcode/user?id=%d&identifier=%s", id, identifier);
		return QRCode.from(qrFormattedString)
				.withSize(1024, 1024)
				.withColor(ContextCompat.getColor(context, R.color.darkPurple), Color.TRANSPARENT)
				.bitmap();
	}

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static void toggleEventStarred(Context context, ImageView star, EventResponse.Event event) {
		boolean starred = !Settings.get().getIsEventStarred(event.getId());
		setEventStarred(context, star, event, starred);

		if (starred) {
			TimeUnit timeUnit = TimeUnit.MINUTES;
			long duration = 15;
			EventNotifierJob.scheduleReminder(event, timeUnit.toMillis(duration));
			String infoMessage = context.getString(
					R.string.notified_before_event_starts,
					duration,
					timeUnit.toString().toLowerCase(),
					event.getName()
			);
			Toast.makeText(context, infoMessage, Toast.LENGTH_SHORT).show();
		} else {
			EventNotifierJob.cancelReminder(event);
		}
	}

	public static void updateEventStarred(Context context, ImageView star, EventResponse.Event event) {
		setEventStarred(context, star, event, Settings.get().getIsEventStarred(event.getId()));
	}

	public static void setEventStarred(Context context, ImageView star, EventResponse.Event event, boolean starred) {
		GoogleMaterial.Icon icon;
		if (starred) {
			icon = GoogleMaterial.Icon.gmd_star;
			Settings.get().saveEventIsStarred(event.getId());
		} else {
			icon = GoogleMaterial.Icon.gmd_star_border;
			Settings.get().saveEventIsNotStarred(event.getId());
		}

		Timber.i("Setting event %s id=%d to be starred=%b", event.getName(), event.getId(), starred);

		star.setImageDrawable(
				new IconicsDrawable(context)
						.icon(icon)
						.colorRes(R.color.bluePurple)
						.actionBar()
		);
	}

	public static void goToMapApp(Context context) {
		Settings settings = Settings.get();
		String json = settings.getLocations();

		LocationResponse locations = Settings.get().getGson().fromJson(json, LocationResponse.class);

		// Just get the first location's location to open up maps app.
		double longitude = locations.getLocations()[0].getLongitude();
		double latitude = locations.getLocations()[0].getLatitude();

		if (longitude == 0) {
			longitude = 40.1138; // Default location for ECEB
		}

		if (latitude == 0) {
			latitude = -88.2249; // Default location for ECEB
		}

		launchGoogleMaps(context, longitude, latitude);
	}

	public static void goToMapApp(Context context, double longitude, double latitude) {
		launchGoogleMaps(context, longitude, latitude);
	}

	private static void launchGoogleMaps(Context context, double longitude, double latitude) {
		Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=" + 18);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
		mapIntent.setPackage("com.google.android.apps.maps");

		if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(mapIntent);
		} else {
			Toast.makeText(context,
					"Google Maps not installed!", Toast.LENGTH_SHORT).show();
		}
	}
}
