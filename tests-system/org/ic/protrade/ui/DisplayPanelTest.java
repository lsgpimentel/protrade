package org.ic.protrade.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Match;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DisplayPanelTest {

	private Display display;
	private Shell shell;
	private SWTBot bot;
	private DisplayPanel dp;
	private Match match;

	@Before
	public void setUp() {
		match = new HistoricalMatch("data/test/fracsoft-reader/tso-fed.csv",
				null);
		display = new Display();
		shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setSize(0, 0);
		dp = new DisplayPanel(shell, SWT.NONE);
		shell.open();
		bot = new SWTBot(shell);
	}

	@After
	public void tearDown() {
		while (display.readAndDispatch()) {
			// handle remaining work
		}
		display.dispose();
	}

	@Test
	public void addMatchView() throws Exception {
		assertEquals(0, dp.getFolder().getItemCount());
		dp.handleMatchSelection(match);
		assertEquals(1, dp.getFolder().getItemCount());
		SWTBotCTabItem item = bot.cTabItem(match.toString());
		assertEquals(item.getText(), match.toString());
		assertNotSame(item.getText(), match.toString() + "asd");
	}
}
