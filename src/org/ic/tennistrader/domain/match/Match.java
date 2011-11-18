package org.ic.tennistrader.domain.match;

import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public interface Match {    
    public boolean isInPlay();
    public String getName();
    public Player getPlayerOne();
    public Player getPlayerTwo();
    public Score getScore();
    public void setScore(Score score);    
    public void registerForUpdate(UpdatableWidget widget);
}
