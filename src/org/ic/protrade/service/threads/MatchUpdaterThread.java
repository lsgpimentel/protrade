package org.ic.protrade.service.threads;

import org.ic.protrade.domain.match.Match;


public abstract class MatchUpdaterThread extends MatchThread{
    
    public MatchUpdaterThread(){
    }

    protected MatchUpdaterThread(Match match) {
        super(match);
    }
}