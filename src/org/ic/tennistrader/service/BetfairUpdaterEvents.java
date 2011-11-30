package org.ic.tennistrader.service;

import java.util.ArrayList;
import java.util.List;
import org.ic.tennistrader.domain.EventBetfair;

public class BetfairUpdaterEvents {
	private List<EventBetfair> events;
	
	public BetfairUpdaterEvents() {
		events = new ArrayList<EventBetfair>();
	}
	
	public synchronized List<EventBetfair> getEvents() {
		return this.events;
	}
	
	public synchronized void addEvent(EventBetfair eb) {
		if (!events.contains(eb)) {
			this.events.add(eb);
		}
	}

	public void removeEvent(EventBetfair eventBetfair) {
		this.events.remove(eventBetfair);
	}
}