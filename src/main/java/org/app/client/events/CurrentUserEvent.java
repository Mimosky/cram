package org.app.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CurrentUserEvent extends GwtEvent<CurrentUserEvent.CurrentUserHandler> {
	private static Type<CurrentUserHandler> TYPE = new Type<CurrentUserHandler>();

	public interface CurrentUserHandler extends EventHandler {
		void onCurrentUser(CurrentUserEvent event);
	}

	public static void fire(String userName, HasHandlers source) {
		source.fireEvent(new CurrentUserEvent(userName));
	}

	private final String message;

	public CurrentUserEvent(final String message) {
		this.message = message;
	}

	public static Type<CurrentUserHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final CurrentUserHandler handler) {
		handler.onCurrentUser(this);
	}

	@Override
	public Type<CurrentUserHandler> getAssociatedType() {
		return TYPE;
	}

	public String getMessage() {
		return this.message;
	}
}
