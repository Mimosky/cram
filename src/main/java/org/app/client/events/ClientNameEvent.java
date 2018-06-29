package org.app.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ClientNameEvent extends GwtEvent<ClientNameEvent.ClientNameHandler> {
    private static Type<ClientNameHandler> TYPE = new Type<ClientNameHandler>();
    
    public interface ClientNameHandler extends EventHandler {
        void onClientName(ClientNameEvent event);
    }
    
    
    private final String message;
   
    public ClientNameEvent(final String message) {
        this.message = message;
    }

    public static Type<ClientNameHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ClientNameHandler handler) {
        handler.onClientName(this);
    }

    @Override
    public Type<ClientNameHandler> getAssociatedType() {
        return TYPE;
    }
    
    public String getMessage() {
        return this.message;
    }
}