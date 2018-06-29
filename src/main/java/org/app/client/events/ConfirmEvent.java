package org.app.client.events;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ConfirmEvent extends GwtEvent<ConfirmEvent.ConfirmHandler> {
    private static Type<ConfirmHandler> TYPE = new Type<ConfirmHandler>();
    
    public interface ConfirmHandler extends EventHandler {
        void onConfirm(ConfirmEvent event);
    }
    
    
    private final String message;
    private  JsDate date;
    private String id;
    private String sender;
   
    public ConfirmEvent(final String message, final JsDate date, final String id, final String sender) {
        this.message = message;
        this.date = date;
        this.id = id;
        this.sender = sender;
    }

    public static Type<ConfirmHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ConfirmHandler handler) {
        handler.onConfirm(this);
    }

    @Override
    public Type<ConfirmHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }
    public JsDate getDate() {
		return date;
	}
    public String getId() {
		return id;
	}
    public String getSender() {
		return sender;
	}
}