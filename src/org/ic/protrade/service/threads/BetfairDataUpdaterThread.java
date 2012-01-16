package org.ic.protrade.service.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.ic.protrade.domain.markets.CompleteMarketData;
import org.ic.protrade.domain.markets.EventBetfair;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.markets.SetBettingMarketData;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.RealMatch;
import org.ic.protrade.model.connection.BetfairExchangeHandler;
import org.ic.protrade.service.DataManager;

public class BetfairDataUpdaterThread extends MatchUpdaterThread {
    // private List<EventBetfair> events;
    private BetfairUpdaterEvents synchronizedEvents;
    private HashMap<EventBetfair, RealMatch> matches;
    private int i = 0;

    private static Logger log = Logger
            .getLogger(BetfairDataUpdaterThread.class);

    public BetfairDataUpdaterThread() {
        matches = new HashMap<EventBetfair, RealMatch>();
        // events = new ArrayList<EventBetfair>();
        synchronizedEvents = new BetfairUpdaterEvents();
    }

    public void setMatch(RealMatch match) {
        EventBetfair eventBetfair = match.getEventBetfair();
        matches.put(eventBetfair, match);
        // events.add(eventBetfair);
        synchronizedEvents.addEvent(eventBetfair);
    }

	@Override
	public void runBody() {
		HashMap<Match, CompleteMarketData> newMap = new HashMap<Match, CompleteMarketData>();
		List<EventBetfair> events = new ArrayList<EventBetfair>(
				synchronizedEvents.getEvents());
		for (EventBetfair eb : events) {
			//System.out.println("Size of events - " + events.size());
			RealMatch match = matches.get(eb);
			if (match.isInPlay() || match.getLastMarketData() == null || i == 0) {
				/*			
				MOddsMarketData marketData = BetfairExchangeHandler
						.getMarketOdds(eb);
				SetBettingMarketData setBetting = BetfairExchangeHandler
						.getSetBettingMarketData(match);
				
				CompleteMarketData completeMarketData = 
					new CompleteMarketData(marketData, setBetting);
				*/
				
				/*
				MOddsMarketData marketData = BetfairExchangeHandler
						.getMatchOddsMarketData(match);
						*/
				MOddsMarketData marketData = BetfairExchangeHandler
				.getCompressedMatchOddsMarketData(match);
		
				SetBettingMarketData setBetting = new SetBettingMarketData();

				CompleteMarketData completeMarketData = new CompleteMarketData(
						marketData, setBetting);
				/*
				CompleteMarketData completeMarketData = 
					BetfairExchangeHandler.getCompleteMarketData(match);
				MOddsMarketData marketData = completeMarketData.getmOddsMarketData();
				*/
				if (marketData.getPl1Back() != null) {
					match.addMarketData(marketData);
				}
				newMap.put(match, completeMarketData);
			}
			i = (i + 1) % 1;
		}
		DataManager.handleEvent(newMap);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			log.info("Betfair thread interrupted");
		}
	}

    public List<EventBetfair> getEvents() {
        return synchronizedEvents.getEvents();
    }

    public void removeEvent(EventBetfair eventBetfair) {
        synchronizedEvents.removeEvent(eventBetfair);
    }
}