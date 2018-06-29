package org.app.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ValidCongesEvent extends GwtEvent<ValidCongesEvent.ValidCongesHandler> {
    private static Type<ValidCongesHandler> TYPE = new Type<ValidCongesHandler>();
    
    public interface ValidCongesHandler extends EventHandler {
        void onValidConges(ValidCongesEvent event);
    }
    
    
    private final String message;
   
    public ValidCongesEvent(final String message) {
        this.message = message;
    }

    public static Type<ValidCongesHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ValidCongesHandler handler) {
        handler.onValidConges(this);
    }

    @Override
    public Type<ValidCongesHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }
}