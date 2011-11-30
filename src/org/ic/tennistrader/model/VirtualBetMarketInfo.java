package org.ic.tennistrader.model;

import java.util.HashMap;

public class VirtualBetMarketInfo {
    // amounts available to match on the market 
    private HashMap<Double, Double> backFirstPlayerAvailableMatches;   
    private HashMap<Double, Double> layFirstPlayerAvailableMatches;
    private HashMap<Double, Double> backSecondPlayerAvailableMatches;
    private HashMap<Double, Double> laySecondPlayerAvailableMatches;
    // amount already bet by the user
    private HashMap<Double, Double> matchedBackBetsFirstPlayer;
    private HashMap<Double, Double> matchedLayBetsFirstPlayer;
    private HashMap<Double, Double> matchedBackBetsSecondPlayer;
    private HashMap<Double, Double> matchedLayBetsSecondPlayer;

    public VirtualBetMarketInfo() {
        backFirstPlayerAvailableMatches = new HashMap<Double, Double>();
        layFirstPlayerAvailableMatches = new HashMap<Double, Double>();
        backSecondPlayerAvailableMatches = new HashMap<Double, Double>();
        laySecondPlayerAvailableMatches = new HashMap<Double, Double>();
        matchedBackBetsFirstPlayer = new HashMap<Double, Double>();
        matchedLayBetsFirstPlayer = new HashMap<Double, Double>();
        matchedBackBetsSecondPlayer = new HashMap<Double, Double>();
        matchedLayBetsSecondPlayer = new HashMap<Double, Double>();
    }
    
    public HashMap<Double, Double> getBackFirstPlayerAvailableMatches() {
        return backFirstPlayerAvailableMatches;
    }

    public void setBackFirstPlayerAvailableMatches(
            HashMap<Double, Double> backFirstPlayerAvailableMatches) {
        this.backFirstPlayerAvailableMatches = backFirstPlayerAvailableMatches;
    }
    
    public void addBackFirstPlayerAvailableMatches(Double odds, Double amount) {
        this.backFirstPlayerAvailableMatches.put(odds, amount);
    }

    public HashMap<Double, Double> getLayFirstPlayerAvailableMatches() {
        return layFirstPlayerAvailableMatches;
    }

    public void setLayFirstPlayerAvailableMatches(
            HashMap<Double, Double> layFirstPlayerAvailableMatches) {
        this.layFirstPlayerAvailableMatches = layFirstPlayerAvailableMatches;
    }
    
    public void addLayFirstPlayerAvailableMatches(Double odds, Double amount) {
        this.layFirstPlayerAvailableMatches.put(odds, amount);
    }

    public HashMap<Double, Double> getBackSecondPlayerAvailableMatches() {
        return backSecondPlayerAvailableMatches;
    }

    public void setBackSecondPlayerAvailableMatches(
            HashMap<Double, Double> backSecondPlayerAvailableMatches) {
        this.backSecondPlayerAvailableMatches = backSecondPlayerAvailableMatches;
    }
    
    public void addBackSecondPlayerAvailableMatches(Double odds, Double amount) {
        this.backSecondPlayerAvailableMatches.put(odds, amount);
    }

    public HashMap<Double, Double> getLaySecondPlayerAvailableMatches() {
        return laySecondPlayerAvailableMatches;
    }

    public void setLaySecondPlayerAvailableMatches(
            HashMap<Double, Double> laySecondPlayerAvailableMatches) {
        this.laySecondPlayerAvailableMatches = laySecondPlayerAvailableMatches;
    }
    
    public void addLaySecondPlayerAvailableMatches(Double odds, Double amount) {
        this.laySecondPlayerAvailableMatches.put(odds, amount);
    }

    public HashMap<Double, Double> getMatchedBackBetsFirstPlayer() {
        return matchedBackBetsFirstPlayer;
    }

    public void setMatchedBackBetsFirstPlayer(
            HashMap<Double, Double> matchedBackBetsFirstPlayer) {
        this.matchedBackBetsFirstPlayer = matchedBackBetsFirstPlayer;
    }
    
    public void addMatchedBackBetsFirstPlayer(Double odds, Double amount) {
        Double previousAmount = this.matchedBackBetsFirstPlayer.get(odds);
        if (previousAmount == null)
            previousAmount = 0.0;
        this.matchedBackBetsFirstPlayer.put(odds, previousAmount + amount);
    }

    public HashMap<Double, Double> getMatchedLayBetsFirstPlayer() {
        return matchedLayBetsFirstPlayer;
    }

    public void setMatchedLayBetsFirstPlayer(
            HashMap<Double, Double> matchedLayBetsFirstPlayer) {
        this.matchedLayBetsFirstPlayer = matchedLayBetsFirstPlayer;
    }
    
    public void addMatchedLayBetsFirstPlayer(Double odds, Double amount) {
        Double previousAmount = this.matchedLayBetsFirstPlayer.get(odds);
        if (previousAmount == null)
            previousAmount = 0.0;
        this.matchedLayBetsFirstPlayer.put(odds, previousAmount + amount);
    }

    public HashMap<Double, Double> getMatchedBackBetsSecondPlayer() {
        return matchedBackBetsSecondPlayer;
    }

    public void setMatchedBackBetsSecondPlayer(
            HashMap<Double, Double> matchedBackBetsSecondPlayer) {
        this.matchedBackBetsSecondPlayer = matchedBackBetsSecondPlayer;
    }
    
    public void addMatchedBackBetsSecondPlayer(Double odds, Double amount) {
        Double previousAmount = this.matchedBackBetsSecondPlayer.get(odds);
        if (previousAmount == null)
            previousAmount = 0.0;
        this.matchedBackBetsSecondPlayer.put(odds, previousAmount + amount);
    }

    public HashMap<Double, Double> getMatchedLayBetsSecondPlayer() {
        return matchedLayBetsSecondPlayer;
    }

    public void setMatchedLayBetsSecondPlayer(
            HashMap<Double, Double> matchedLayBetsSecondPlayer) {
        this.matchedLayBetsSecondPlayer = matchedLayBetsSecondPlayer;
    }
    
    public void addMatchedLayBetsSecondPlayer(Double odds, Double amount) {
        Double previousAmount = this.matchedLayBetsSecondPlayer.get(odds);
        if (previousAmount == null)
            previousAmount = 0.0;
        this.matchedLayBetsSecondPlayer.put(odds, previousAmount + amount);
    }
}