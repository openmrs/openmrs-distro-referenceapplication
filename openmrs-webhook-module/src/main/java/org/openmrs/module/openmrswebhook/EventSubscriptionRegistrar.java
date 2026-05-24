package org.openmrs.module.openmrswebhook;

import javax.jms.MessageListener;

public class EventSubscriptionRegistrar {
    private static final String[] ACTIONS = {"CREATED", "UPDATED", "VOIDED", "UNVOIDED"};

    public void registerEncounterListener(MessageListener listener) {
        try {
            Class<?> eventClass = Class.forName("org.openmrs.event.Event");
            Class<?> encounterClass = Class.forName("org.openmrs.Encounter");
            var subscribe = eventClass.getMethod("subscribe", Class.class, String.class, MessageListener.class);

            for (String action : ACTIONS) {
                subscribe.invoke(null, encounterClass, action, listener);
            }
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("OpenMRS Event Module subscription failed.", ex);
        }
    }
}
