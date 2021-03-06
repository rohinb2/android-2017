package org.hackillinois.android.api;

import org.hackillinois.android.api.response.announcement.AnnouncementRequest;
import org.hackillinois.android.api.response.announcement.AnnouncementResponse;
import org.hackillinois.android.api.response.announcement.AnnouncementStartResponse;
import org.hackillinois.android.api.response.event.EventResponse;
import org.hackillinois.android.api.response.location.LocationResponse;
import org.hackillinois.android.api.response.login.LoginRequest;
import org.hackillinois.android.api.response.login.LoginResponse;
import org.hackillinois.android.api.response.qrcode.TrackingResponse;
import org.hackillinois.android.api.response.tracking.TrackingStartResponse;
import org.hackillinois.android.api.response.tracking.TrackingRequest;
import org.hackillinois.android.api.response.user.AttendeeResponse;
import org.hackillinois.android.api.response.user.UserResponse;
import org.joda.time.DateTime;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HackIllinoisAPI {
	String SERVER_ADDRESS = "https://api.hackillinois.org";
	String AUTH = SERVER_ADDRESS + "/v1/auth";


	@POST("/v1/auth")
	Call<LoginResponse> verifyUser(@Body LoginRequest request);

	@POST("/v1/tracking/")
	Call<TrackingStartResponse> startTracking(@Header("Authorization") String auth, @Body TrackingRequest request);

	@POST("/v1/announcement")
	Call<AnnouncementStartResponse> makeAnnouncement(@Header("Authorization") String auth, @Body AnnouncementRequest request);

	@GET("/v1/registration/attendee")
	Call<AttendeeResponse> getAttendeeInfo(@Header("Authorization") String auth);

	@GET("/v1/user")
	Call<UserResponse> getUserInfo(@Header("Authorization") String auth);

	@GET("/v1/auth/github")
	Call<LoginResponse> verifyUser(@Query("code") String code);

	@GET("/v1/auth/refresh")
	Call<LoginResponse> refreshUser(@Header("Authorization") String auth);

	@GET("/v1/event/")
	Call<EventResponse> getEvents();

	@GET("/v1/tracking/{trackID}")
	Call<TrackingResponse> getTracking(@Header("Authorization") String auth, @Path("trackID") int trackID);

	@GET("/v1/announcement/all")
	Call<AnnouncementResponse> getAnnouncements(
			@Query("before") DateTime beforeDate,
			@Query("after") DateTime afterDate,
			@Query("limit") Integer limit
	);

	@GET("/v1/announcement/all")
	Call<AnnouncementResponse> getAnnouncements();

	@GET("/v1/event/location/all")
	Call<LocationResponse> getLocations();
}
