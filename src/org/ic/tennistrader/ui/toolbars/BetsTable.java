package org.ic.tennistrader.ui.toolbars;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.model.betting.BetManager;

public class BetsTable {
	private Shell shell;
	private Table table;
	private TableColumn[] column = new TableColumn[7];
	
	public BetsTable(int x,int y, final DashboardToolBar dtb) {
		shell = new Shell();
		shell.setText("Bets Table");
		shell.setBounds(x, y, 410, 200);
		shell.setLayout(new FillLayout());
		table = new Table(shell, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		column[0] = new TableColumn(table, SWT.CENTER);
		column[0].setText("Match");
		column[1] = new TableColumn(table, SWT.CENTER);
		column[1].setText("Player");
		column[2] = new TableColumn(table, SWT.CENTER);
		column[2].setText("Bet Type");
		column[3] = new TableColumn(table, SWT.CENTER);
		column[3].setText("Odds");
		column[4] = new TableColumn(table, SWT.CENTER);
		column[4].setText("Amount");
		column[5] = new TableColumn(table, SWT.CENTER);
		column[5].setText("Profit");
		column[6] = new TableColumn(table, SWT.CENTER);
		column[6].setText("Liability");
		BetManager.setBetsTable(this);
		for (int i = 0; i < column.length; i++) column[i].pack();
		List <Bet> mbets = BetManager.getMatchedBetTab();
		List <Bet> ubets = BetManager.getUnmatchedBetTab();
		for (Bet b : mbets) {
			add(b);
		}
		for (Bet b : ubets) {
			add(b);
		}
		shell.open();
		dtb.setBetsTable(null);
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dtb.setBetsTable(null);
				BetManager.setBetsTable(null);
			}
		});
	}
	
	public void setFocus() {
		shell.forceActive();
	}
	
	public void add( Bet bet ) {
		TableItem item = new TableItem(table,SWT.NONE);
		item.setText(0, bet.getMatch().getName());
		item.setText(1, bet.getPlayer().name());
		item.setText(2, bet.getType().toString());
		item.setText(3, bet.getOdds() + "");
		item.setText(4, bet.getAmount() + "");
		item.setText(5, bet.getProfit() + "");
		item.setText(6, bet.getPossibleLiability() + "");
	}
}
