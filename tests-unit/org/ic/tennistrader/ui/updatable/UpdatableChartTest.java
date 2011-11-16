package org.ic.tennistrader.ui.updatable;

import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.FracsoftReader;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.MainWindow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdatableChartTest{
	private UpdatableChart chart;
	private String filename;
	private Match match;
		
	@Before
	public void setUp() {
		Shell shell = new Shell();
		filename = "fracsoft-data/fracsoft1.csv";
	    match = new HistoricalMatch(filename);
	    Slider slider = new Slider(shell, SWT.BORDER);
		chart = new UpdatableChart(shell, SWT.BORDER, match, slider);
	}	
	
	@After
	public void tearDown() {
		LiveDataFetcher.stopAllThreads();
	}
	
    @Test
    public void testInvertAxis() {
    	try {
			FracsoftReader reader = new FracsoftReader(match, filename);
				chart.handleUpdate(reader.getMarketData());
				chart.handleUpdate(reader.getMarketData());
				chart.handleUpdate(reader.getMarketData());
				chart.handleUpdate(reader.getMarketData());
			/*chart.invertAxis();
	        for (double d : chart.getFirstSeries().getYSeries()) {
	        	assertTrue(d <= 1);
	        	assertTrue(d >= 0);
	        }*/
		} catch (FileNotFoundException e) {		
		}        
    }    
    
}