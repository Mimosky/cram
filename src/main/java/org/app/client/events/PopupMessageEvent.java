package org.app.client.events;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PopupMessageEvent extends GwtEvent<PopupMessageEvent.PopupMessageHandler> {
    private static Type<PopupMessageHandler> TYPE = new Type<PopupMessageHandler>();
    
    public interface PopupMessageHandler extends EventHandler {
        void onPopupMessage(PopupMessageEvent event);
    }
    
    
    private final String message;
    private  JsDate date;
    private  String id;
    private String sender;
   
    public PopupMessageEvent(final String message, final JsDate date, final String id, final String sender) {
        this.message = message;
        this.date = date;
        this.id = id;
        this.sender = sender;
    }
    
    public PopupMessageEvent(final String message) {
        this.message = message;
    }
    


    public static Type<PopupMessageHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final PopupMessageHandler handler) {
        handler.onPopupMessage(this);
    }

    @Override
    public Type<PopupMessageHandler> getAssociatedType() {
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