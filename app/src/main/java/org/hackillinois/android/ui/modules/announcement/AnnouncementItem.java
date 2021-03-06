package org.hackillinois.android.ui.modules.announcement;

import android.view.View;
import android.widget.TextView;

import org.hackillinois.android.R;
import org.hackillinois.android.api.response.announcement.AnnouncementResponse;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;

public class AnnouncementItem extends AbstractFlexibleItem<AnnouncementItem.AnnouncementViewHolder> implements ISectionable<AnnouncementItem.AnnouncementViewHolder, TimeHeader> {
	private static final DateTimeFormatter GROUP_DTF = DateTimeFormat.forPattern("E h:00 a");
	private static final DateTimeFormatter DTF = DateTimeFormat.forPattern("h:mm a");
	private final AnnouncementResponse.Announcement announcement;

	private final TimeHeader timeHeader;

	public AnnouncementItem(AnnouncementResponse.Announcement announcement) {
		this.announcement = announcement;
		timeHeader = new TimeHeader(GROUP_DTF.print(announcement.getCreated()));
	}

	@Override
	public int getLayoutRes() {
		return R.layout.announcement_list_item;
	}

	@Override
	public AnnouncementViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
		return new AnnouncementViewHolder(view, adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, AnnouncementViewHolder holder, int position, List<Object> payloads) {
		holder.notificationType.setText(R.string.announcement);
		holder.notificationTime.setText(DTF.print(announcement.getCreated()));
		holder.notificationDescription.setText(announcement.getDescription());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AnnouncementItem that = (AnnouncementItem) o;

		return getAnnouncement().equals(that.getAnnouncement());
	}

	@Override
	public int hashCode() {
		return getAnnouncement().hashCode();
	}

	public AnnouncementResponse.Announcement getAnnouncement() {
		return announcement;
	}

	@Override
	public TimeHeader getHeader() {
		return timeHeader;
	}

	@Override
	public void setHeader(TimeHeader header) {

	}

	public static class AnnouncementViewHolder extends FlexibleViewHolder {
		@BindView(R.id.notificationType) TextView notificationType;
		@BindView(R.id.notificationTime) TextView notificationTime;
		@BindView(R.id.notificationDescription) TextView notificationDescription;


		public AnnouncementViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter);
			ButterKnife.bind(this, view);
		}
	}
}
