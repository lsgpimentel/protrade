package org.ic.tennistrader.ui;

import java.awt.Canvas;
import java.awt.Frame;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.listener.MatchSelectionListener;
import org.ic.tennistrader.score.PredictionGui;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.updatable.UpdatableChart;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;

public class DisplayPanel extends StandardTabbedWidgetContainer implements
		MatchSelectionListener {

	private SashForm chartSash;

	private static Logger log = Logger.getLogger(DisplayPanel.class);
	
    public static void main(String args[]){

    	Display display = new Display();
        Shell shell = new Shell();
        shell.setLayout(new FillLayout());
    	DisplayPanel dp = new DisplayPanel(shell, SWT.NONE);
    	Match match = new HistoricalMatch("data/test/fracsoft-reader/tso-fed.csv");
    	dp.addMatchView(match);
    	shell.open();
    	display.sleep();
    	while(!shell.isDisposed()){
    		if (!display.readAndDispatch())
    			display.sleep();
    	}
    	display.dispose();
    }

	public DisplayPanel(Composite parent, int style) {
		super(parent, style);
	}

	private void addPredictionGui(Composite composite, Match match) {
		new PredictionGui(composite, SWT.BORDER, match).start();
	}

	public void handleMatchSelection(Match match) {
		addMatchView(match);
	}

	private boolean firstTime = true;

	private void addMatchView(Match match) {
		//CTabItem[] items = folder.getItems();
		String matchName = match.toString();

		int pos = getTabPosition(matchName);
        // check new tab has been open
		if (pos == -1) {

			final CTabItem item = addTab(matchName);

			Composite control = new Composite(folder, SWT.NONE);
			control.setLayout(new FillLayout());

			SashForm comp = new SashForm(control, SWT.VERTICAL);
			comp.setLayout(new FillLayout());
	/*        if (firstTime) {
				firstTime = false;
				Image newImage = setColor(item, comp);
				folder.setSelectionBackground(newImage);
			}*/

			addMatchData(comp, match);

			addPredictionGui(comp, match);

			this.chartSash = new SashForm(comp, SWT.HORIZONTAL);
			addChart(chartSash, match);
			addMatchViewer(chartSash);
			try{
				chartSash.setWeights(new int[] { 70, 30 });
			} catch (Exception e){
				e.printStackTrace();
			}

			item.setControl(control);

			folder.setSelection(item);

			comp.setWeights(new int[] { 5, 40, 50, 5 });

		} else
			// just bring the required tab under focus
			folder.setSelection(pos);
	}

	private Image setColor(CTabItem item, SashForm comp) {
		final Display display = comp.getDisplay();
		final Color foregroundColor = new org.eclipse.swt.graphics.Color(
				display, 105, 105, 105);
		final Color backgroundColor = new org.eclipse.swt.graphics.Color(
				display, 168, 168, 168);
		final Rectangle rect = item.getBounds();

		Image newImage = new Image(display, rect.width, rect.height);

		GC gc = new GC(newImage);
		gc.setForeground(foregroundColor);
		gc.setBackground(backgroundColor);
		gc.fillGradientRectangle(rect.x, rect.y, rect.width, rect.height / 2,
				true);

		gc.setForeground(backgroundColor);
		gc.setBackground(foregroundColor);
		gc.fillGradientRectangle(rect.x, rect.height / 2, rect.width,
				rect.height, true);
		gc.dispose();
		return newImage;
	}

	/**
	 * Adds an area for displaying match data
	 * 
	 * @param comp
	 * @param ti
	 */
	private void addMatchData(Composite comp, final Match match) {
		MatchDataView matchDataView = new MatchDataView(comp, SWT.BORDER, match);
		LiveDataFetcher.registerForMatchUpdate(matchDataView, match);
	}

	/**
	 * Adds the back and lay chart
	 * 
	 * @param comp
	 * @param ti
	 */
	private void addChart(Composite comp, Match match) {
		// Select values on chart
		SashForm form = new SashForm(comp, SWT.VERTICAL);
		//Composite c = new Composite(comp, SWT.NONE);
//		c.setLayout(new FillLayout());
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 1;
//		c.setLayout(gridLayout);
		// c.setLayout(new GridLayout());
		Composite slideComp = new Composite(comp.getParent(), SWT.NONE);
		slideComp.setLayout(new FillLayout());
		slideComp.setBackgroundMode(SWT.INHERIT_DEFAULT);
		Slider slider = new Slider(slideComp, SWT.HORIZONTAL);
		slider.setMaximum(1);
		slider.setValues(0, 0, 1, 0, 0, 0);
		// new Charts(c,SWT.BORDER,match,slider);
		final UpdatableChart chart = new UpdatableChart(form, SWT.BORDER, match,
				slider);
		form.setWeights(new int[]{65,35});
		LiveDataFetcher.registerForMatchUpdate(chart, match);
		comp.update();
	}

	public Control getControl() {
		return folder.getParent();
	}

	public void addMatchViewer(Composite comp) {
		Composite videoComposite; 
		try {
			final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
			videoComposite = new Composite(comp, SWT.EMBEDDED	| SWT.NO_BACKGROUND);
			Frame videoFrame = SWT_AWT.new_Frame(videoComposite);
			Canvas videoSurface = new Canvas();
			videoSurface.setBackground(java.awt.Color.black);
			videoFrame.add(videoSurface);
			//videoComposite.setBounds(100, 100, 450, 200);
			videoComposite.setVisible(true);

			// "mediaPlayer" is a regular vlcj MediaPlayer instance
			mediaPlayerComponent.getMediaPlayer().setVideoSurface(
					new CanvasVideoSurface(videoSurface,
							new LinuxVideoSurfaceAdapter()));

			mediaPlayerComponent.getMediaPlayer().playMedia(
					"/media/6C36C2F636C2BFF6/muzica/fed-tso.wmv");
		} catch (Exception e) {
			log.info("Failed to start media player: " + e.getMessage());
			videoComposite = new Composite(comp, SWT.NONE);
			Label label = new Label(videoComposite, SWT.NONE);
			label.setText("Video loading failed");
			label.pack();
		}
		//videoComposite.dispose();
		//comp.getParent().layout();
		/*
		 * 
		 * final Browser browser; try { browser = new Browser(comp, SWT.NONE); }
		 * catch (SWTError e) { log.error("Could not instantiate Browser: " +
		 * e.getMessage()); return; } browser.setUrl(
		 * "http://www.livescorehunter.ro/index.php?option=com_lsh&view=lsh&event_id=70396&tv_id=374&tid=26339&channel=0&tmpl=component&layout=popup&Itemid=207"
		 * ); comp.layout();
		 */
	}

	public void addMatchViewer() {
		addMatchViewer(chartSash);
	}

}
