package src.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import src.domain.EventBetfair;
import src.domain.MOddsMarketData;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class BetfairDataUpdater implements Runnable {
	private Chart chart;
	private Composite comp;
	private List<EventBetfair> events;
	private static Logger log = Logger.getLogger(BetfairDataUpdater.class);

	public BetfairDataUpdater(/*String chartTitle,*/ Composite comp) {
		/*
		chart = new Chart(comp, SWT.NONE);
		chart.getTitle().setText(chartTitle);
		GridData chartData = new GridData();
		chartData.horizontalSpan = 2;
		// chartData.horizontalAlignment = SWT.FILL;
		// chart.setLayoutData(chartData);
		//charts.add(chart);
		//this.chart = chart;
		 * */
		this.comp = comp;
				
		events = new ArrayList<EventBetfair>();
	}
	
	public void addEvent(EventBetfair eb) {
		events.add(eb);
	}
	
	public List<EventBetfair> getEvents() {
		return events;
	}
	
	@Override
	public void run() {
		/*
		comp.getDisplay().timerExec(0, new Runnable() {
			@Override
			public void run() {
				fillChartData(chart);
				chart.redraw();
				if (!comp.isDisposed()) comp.update();
				comp.getDisplay().timerExec(1000, this);
			}
		});
		*/
		
		comp.getDisplay().timerExec(0, new Runnable() {
			@Override
			public void run() {
				
				HashMap<EventBetfair, MOddsMarketData> newMap = new HashMap<EventBetfair, MOddsMarketData>();
				for (EventBetfair eb : events) {
					newMap.put(eb, BetfairExchangeHandler.getMarketOdds(eb));
				}
				LiveDataFetcher.handleEvent(newMap);
				if (!comp.isDisposed())
					comp.getDisplay().timerExec(5000, this);
			}
		});
		
		/*
		while (true) {
			HashMap<EventBetfair, MOddsMarketData> newMap = new HashMap<EventBetfair, MOddsMarketData>();
			for (EventBetfair eb : events) {
				newMap.put(eb, BetfairExchangeHandler.getMarketOdds(eb));
			}
			LiveDataFetcher.handleEvent(newMap);
			log.info("done reading values once");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error("BetfairDataUpdater interrupted " + e.getMessage());
			}
		}
		*/
	}
	
	/*
	private void fillChartData(Chart chart) {
		// temporarily for filling charts with random data
		double[] xSeries = new double[60];
		double[] ySeries = new double[60];
		double[] xSeries2 = new double[60];
		double[] ySeries2 = new double[60];
		Random randomGenerator = new Random();
		for (int i = 0; i < 60; i++) {
			xSeries[i] = i;
			xSeries2[i] = i;
			ySeries[i] = randomGenerator.nextDouble() + 1;
			ySeries2[i] = (Math.cos(i) + 2) / 2;
		}
		ISeriesSet seriesSet = chart.getSeriesSet();
		ILineSeries series = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "back odds");
		series.setXSeries(xSeries);
		series.setYSeries(ySeries);
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		series.setLineColor(color);
		series.setSymbolSize(4);
		ILineSeries series2 = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "MA-Fast");
		series2.setXSeries(xSeries2);
		series2.setYSeries(ySeries2);
		series.setSymbolType(PlotSymbolType.CROSS);
		series2.setSymbolType(PlotSymbolType.DIAMOND);
		final IAxisSet axisSet = chart.getAxisSet();
		axisSet.adjustRange();
		// end filling charts
	}
	*/
}