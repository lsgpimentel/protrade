package org.ic.tennistrader.domain;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ic.tennistrader.domain.match.Player;

public class BetDisplayInfo {
	private Composite parent;
	private Label firstPlayerWinnerSummary;
	private Label secondPlayerWinnerSummary;
	private List<Label> bets;
	private String firstPlayerWinnerText;
	private String secondPlayerWinnerText;
	private double firstPlayerWinnerProfit;
	private double secondPlayerWinnerProfit;
	
	public BetDisplayInfo(Composite composite, Player firstPlayer, Player secondPlayer) {
		this.parent = composite;
		this.bets = new ArrayList<Label>();
		initPlayerWinnerSummaryLabels(firstPlayer.toString(), secondPlayer.toString());		
	}

	private void initPlayerWinnerSummaryLabels(String firstPlayerName, String secondPlayerName) {
		firstPlayerWinnerSummary = new Label(parent, SWT.NONE);
		firstPlayerWinnerText = "If " + firstPlayerName + " wins your profit is ";
		firstPlayerWinnerProfit = 0;
		secondPlayerWinnerSummary = new Label(parent, SWT.NONE);
		secondPlayerWinnerText = "If " + secondPlayerName + " wins your profit is ";
		secondPlayerWinnerProfit = 0;
		setPlayerLabels();
	}
	
	private void setPlayerLabels() {
		setFirstPlayerLabel();
		setSecondPlayerLabel();
		parent.layout();
	}

	private void setFirstPlayerLabel() {
		firstPlayerWinnerSummary.setText(firstPlayerWinnerText + firstPlayerWinnerProfit);
	}
	
	private void setSecondPlayerLabel() {
		secondPlayerWinnerSummary.setText(secondPlayerWinnerText + secondPlayerWinnerProfit);
	}
	
	public void setPlayerWinnerProfits(double firstPlayerWinnerProfit, double secondPlayerWinnerProfit) {
		setFirstPlayerWinnerProfit(firstPlayerWinnerProfit);
		setSecondPlayerWinnerProfit(secondPlayerWinnerProfit);
		setPlayerLabels();
	}

	public double getFirstPlayerWinnerProfit() {
		return firstPlayerWinnerProfit;
	}

	private void setFirstPlayerWinnerProfit(double firstPlayerWinnerProfit) {
		this.firstPlayerWinnerProfit = firstPlayerWinnerProfit;
	}

	public double getSecondPlayerWinnerProfit() {
		return secondPlayerWinnerProfit;
	}

	private void setSecondPlayerWinnerProfit(double secondPlayerWinnerProfit) {
		this.secondPlayerWinnerProfit = secondPlayerWinnerProfit;
	}

	public void setBets(List<Label> bets) {
		for (Label bet : bets) {
			Label newBet = new Label(parent, SWT.NONE);
			newBet.setText(bet.getText());
			this.bets.add(newBet);
		}
		parent.layout();
	}

	public List<Label> getBets() {
		return bets;
	}
	
	public void addBet(Label bet) {
		Label newBet = new Label(parent, SWT.NONE);
		newBet.setText(bet.getText());
		this.bets.add(newBet);
		parent.layout();
	}
}