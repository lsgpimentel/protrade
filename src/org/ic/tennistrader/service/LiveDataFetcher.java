package org.ic.tennistrader.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.model.BetManager;
import org.ic.tennistrader.service.threads.MatchRecorderThread;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.ic.tennistrader.utils.Pair;

public class LiveDataFetcher {
    
    // one Betfair updater and many Fracsoft updater
    private static BetfairDataUpdaterThread dataUpdater = null;    
    private static HashMap<Match, MatchRecorderThread>  recordersMap = new HashMap<Match, MatchRecorderThread>();
    private static HashMap<Match, FracsoftReader> fileReaders = new HashMap<Match, FracsoftReader>();    
    // map of updatable widgets waiting for updates from the same betfair event id
    private static HashMap<Integer, List<UpdatableWidget>> listeners = new HashMap<Integer, List<UpdatableWidget>>();
    // map of updatable widgets waiting for updates from the same match from file
    private static HashMap<Match, List<UpdatableWidget>> fileListeners = new HashMap<Match, List<UpdatableWidget>>();
    private static Logger log = Logger.getLogger(LiveDataFetcher.class);
    private static boolean started = false;

    public static void registerForMatchUpdate(final UpdatableWidget widget, final Match match){
        if (match.isFromFile())
            registerFromFile(widget, match, match.getFilename());
        else
            registerLive(widget, (RealMatch)match);
        
        if (recordersMap.get(match) == null){
            MatchRecorderThread mrt = new MatchRecorderThread(match);
            recordersMap.put(match, mrt);
            mrt.start();
        }
    }
    
    public static void unregister(final UpdatableWidget widget, final Match match){
        if (match.isFromFile())
            unregisterFromFile(widget, match);
        else
            unregisterLive(widget, (RealMatch)match);
    }
    
    private static void registerLive(final UpdatableWidget widget, final RealMatch match) {
        if (dataUpdater == null)
            dataUpdater = new BetfairDataUpdaterThread();
        dataUpdater.setMatch(match);
        List<UpdatableWidget> widgets;
        // add the widget as listener to the given match
        if (listeners.containsKey(match.getEventBetfair().getBetfairId())) {
            widgets = listeners.get(match.getEventBetfair().getBetfairId());
        } else {
            widgets = new ArrayList<UpdatableWidget>();
        }
        widgets.add(widget);
        listeners.put(match.getEventBetfair().getBetfairId(), widgets);
        
        widget.setDisposeListener(new ThreadDisposeListener(widget, match));

        // start the thread
        if (!started){
            started = true;
            start();
        }
    }
    
    private static void unregisterLive(UpdatableWidget widget, RealMatch match) {
    	//System.out.println("Unregister live entered");
    	List<UpdatableWidget> widgets = null;
    	if (listeners.containsKey(match.getEventBetfair().getBetfairId())) {
            widgets = listeners.get(match.getEventBetfair().getBetfairId());
        }
    	if (widgets != null) {
    		widgets.remove(widget);
    		if (widgets.size() == 0)
    			removeMatch(match);
    	}
    	    	
	}

	private static void removeMatch(RealMatch match) {
		listeners.remove(match.getEventBetfair().getBetfairId());
		dataUpdater.removeEvent(match.getEventBetfair());
	}
	
	private static void registerFromFile(final UpdatableWidget widget, final Match match, String fileName) {
        List<UpdatableWidget> widgets;
        boolean isNewMatch = !fileListeners.containsKey(match);
        if (isNewMatch)
            widgets = new ArrayList<UpdatableWidget>();
        else
            widgets = fileListeners.get(match);
        widgets.add(widget);
        fileListeners.put(match, widgets);
        widget.setDisposeListener(new ThreadDisposeListener(widget, match));
        if(isNewMatch) {
            startFromFile(match, fileName);
        }
    }

    private static void unregisterFromFile(UpdatableWidget widget, Match match) {
    	//System.out.println("Unregister live entered");
    	List<UpdatableWidget> widgets = null;
    	if (fileListeners.containsKey(match)) {
            widgets = fileListeners.get(match);
        }
    	if (widgets != null) {
    		widgets.remove(widget);
    		if (widgets.size() == 0) {
    			FracsoftReader fracsoftReaderThread = fileReaders.get(match);
    			//removeMatch(match);
    			fracsoftReaderThread.setStop();
    			fracsoftReaderThread.interrupt();
    			fileListeners.remove(match);
    		}
    	}
	}

	public static void start() {
        log.info("Started Betfair thread");
        dataUpdater.start();
    }
    
    private static void startFromFile(Match match, String fileName) {    	
        final FracsoftReader fracsoftUpdater;
        try {
            fracsoftUpdater = new FracsoftReader(match, fileName);
            fileReaders.put(match, fracsoftUpdater);            
            log.info("Started Fracsoft thread");
            fracsoftUpdater.start();            
        } catch(FileNotFoundException fnfe) {
            log.error("Fracsoft file to be opened not found");
        }
    }

    /*
    public static void handleMatchUpdate(Match match){
        if (match.isFromFile()){
            List<UpdatableWidget> widgets = fileListeners.get(match);
            for (UpdatableWidget w : widgets) {
                w.handleUpdate(new MOddsMarketData());
            }
        } else{
            
        }
            
    }
    */
    public static void handleEvent(HashMap<EventBetfair, MOddsMarketData> data) {
        Iterator<EventBetfair> i = data.keySet().iterator();
        while (i.hasNext()) {
            EventBetfair eb = i.next();
            List<UpdatableWidget> widgets = listeners.get(eb.getBetfairId());
            for (UpdatableWidget w : widgets)
                w.handleUpdate(data.get(eb));
        }
    }

    public static void handleFileEvent(Match match, Pair<MOddsMarketData, Score> dataScore) {        
        match.setScore(dataScore.second());
        //System.out.println("Market status - " + dataScore.first().getMatchStatus());
        if (dataScore.first().getMatchStatus().toLowerCase().equals("closed"))
        	BetManager.setBetsOutcome(match);
        if (fileListeners.containsKey(match)) {
            List<UpdatableWidget> widgets = fileListeners.get(match);
            for (UpdatableWidget w : widgets) {
                w.handleUpdate(dataScore.first());
            }
        }
    }
    
    public static void handleEndOfFile(Match match) {
		BetManager.setBetsOutcome(match);
	}
    
    public static void stopAllThreads() {
    	if (dataUpdater != null) {
    		dataUpdater.setStop();
    		dataUpdater.interrupt();
    	}
    	
    	for (FracsoftReader fr : fileReaders.values()) {
    		fr.setStop();
    		fr.interrupt();
    	}
    	
    	for (MatchRecorderThread mrt : recordersMap.values()){
    	    mrt.setStop();
    	    mrt.interrupt();
    	}
    }
    
    public static void setPlaybackSpeed(Match match, int updatesPerSecond) {
    	FracsoftReader fracsoftReader = fileReaders.get(match);
    	if (fracsoftReader != null)
    	    fracsoftReader.setUpdatesPerSecond(updatesPerSecond);
    }
}