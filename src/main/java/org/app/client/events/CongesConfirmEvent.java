package org.app.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CongesConfirmEvent extends GwtEvent<CongesConfirmEvent.CongesConfirmHandler> {
    private static Type<CongesConfirmHandler> TYPE = new Type<CongesConfirmHandler>();
    
    public interface CongesConfirmHandler extends EventHandler {
        void onCongesConfirm(CongesConfirmEvent event);
    }
    
    
    private final String message;
   
    public CongesConfirmEvent(final String message) {
        this.message = message;
    }

    public static Type<CongesConfirmHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final CongesConfirmHandler handler) {
        handler.onCongesConfirm(this);
    }

    @Override
    public Type<CongesConfirmHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }
}