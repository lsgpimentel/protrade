package org.ic.tennistrader.ui.updatable;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ic.tennistrader.controller.BetController;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.ui.StandardWidgetContainer;
import org.ic.tennistrader.ui.betting.BetsDisplay;
import org.ic.tennistrader.utils.Colours;
import org.ic.tennistrader.utils.Pair;

public class UpdatableMarketDataGrid extends StandardWidgetContainer implements
        UpdatableWidget {
    private BetController betController;
    private OddsButton[] p1BackButtons = new OddsButton[3];
    private OddsButton[] p1LayButtons = new OddsButton[3];
    private OddsButton[] p2BackButtons = new OddsButton[3];
    private OddsButton[] p2LayButtons = new OddsButton[3];
    private OddsButton[] p1MarketInfoButtons = new OddsButton[6];
    private OddsButton[] p2MarketInfoButtons = new OddsButton[6];
    private int width = 2 * p1BackButtons.length + p1MarketInfoButtons.length + 1;     
    // min,max,weight
    double p1Values[] = {1000000.0, 0, 0};
    double p2Values[] = {1000000.0, 0, 0};    
    //private Color oddsButtonColor;
    private Font oddsFont;
    private Font titleFont;

    public UpdatableMarketDataGrid(Composite parent, int style, Match match) {
        super(parent, style);
        this.setLayout(new GridLayout(width, true));

        initFonts();

        // required for alignment
        @SuppressWarnings("unused")
        Label blankLabel = new Label(this, SWT.NONE);

        GridData headerData = new GridData();
        headerData.horizontalSpan = p1BackButtons.length;
        headerData.horizontalAlignment = GridData.FILL;
        headerData.grabExcessHorizontalSpace=true;
        
        GridData headerDataSmall = new GridData();
        headerDataSmall.horizontalSpan = 1;
        headerDataSmall.horizontalAlignment = GridData.FILL;
        headerDataSmall.grabExcessHorizontalSpace = true;

        createLabel("Back", Colours.backColor, headerData, SWT.RIGHT);
        createLabel("Lay", Colours.layColor, headerData, SWT.NONE);
        createLabel("LPM", Colours.oddsButtonColor, headerDataSmall, SWT.NONE);
        createLabel("Matched", Colours.oddsButtonColor, headerDataSmall, SWT.NONE);
        createLabel("Mkt%", Colours.oddsButtonColor, headerDataSmall, SWT.NONE);
        createLabel("Low", Colours.oddsButtonColor, headerDataSmall, SWT.NONE);
        createLabel("Wght", Colours.oddsButtonColor, headerDataSmall, SWT.NONE);
        createLabel("High", Colours.oddsButtonColor, headerDataSmall, SWT.NONE);

        initLayout(match.getPlayerOne().getLastname(), p1BackButtons,
                p1LayButtons, p1MarketInfoButtons, true);
        initLayout(match.getPlayerTwo().getLastname(), p2BackButtons,
                p2LayButtons, p2MarketInfoButtons, false);
        
    }

    private void createLabel(String text, Color color, GridData headerData,
            int textAlignment) {
        Label back = new Label(this, textAlignment);
        back.setLayoutData(headerData);
        back.setText(text);
        back.setBackground(color);
        back.setFont(titleFont);
    }

    private void initLayout(String playerName, OddsButton[] pBackButtons,
            OddsButton[] pLayButtons, OddsButton[] pMarketInfo, boolean pl1) {
        Label player = new Label(this, SWT.NONE);
        player.setFont(titleFont);
        player.setText(playerName);

        for (int i = 0; i < 2; i++) {
            pBackButtons[i] = new OddsButton(this, Colours.oddsButtonColor, oddsFont, this);
            /*
             * Composite comp = pBackButtons[i].getComp(); Image
             * backBackgroundImage =
             * GraphicsUtils.makeGradientBackgroundImage(comp, 150, 150, 150,
             * 238, 210, 238 ); Image backClickImage =
             * GraphicsUtils.makeGradientBackgroundImage(comp, 84, 139, 84, 84,
             * 139, 84); Image backHoverImage =
             * GraphicsUtils.makeGradientBackgroundImage(comp, 155, 205, 155,
             * 193, 255, 193);
             * pBackButtons[i].setBackgroundImage(backBackgroundImage);
             * pBackButtons[i].setClickImage(backClickImage);
             * pBackButtons[i].setHighlightImage(backHoverImage);
             */
        }

        pBackButtons[2] = new OddsButton(this, Colours.backColor, oddsFont, this);
        pLayButtons[0] = new OddsButton(this, Colours.layColor, oddsFont, this);

        for (int i = 1; i < 3; i++)
            pLayButtons[i] = new OddsButton(this, Colours.oddsButtonColor, oddsFont, this);
        
        for (int i=0;i<p1MarketInfoButtons.length;i++) {
        	pMarketInfo[i] = new OddsButton(this, Colours.oddsButtonColor, oddsFont, this);
        	pMarketInfo[i].setOdds("             ");
        	pMarketInfo[i].setCurrency("");
        }
    }

    private void initFonts() {
        this.oddsFont = new Font(this.getDisplay(), "Arial", 10, SWT.BOLD);
        this.titleFont = new Font(this.getDisplay(), "Arial", 8, SWT.None);
    }

    public void updateButtons(ArrayList<Pair<Double, Double>> valueList,
            OddsButton[] buttons, boolean back) {
        int i = 0, step = 1;
        if (back) {
            i = 3 - i - 1;
            step = -1;
        }
        for (Pair<Double, Double> p : valueList) {
            buttons[i].setOdds(p.first().toString());
            buttons[i].setAmount(p.second().toString());
            buttons[i].layout();
            i += step;
        }
    }

    public void handleUpdate(final MOddsMarketData newData) {
    	this.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (newData.getPl1Back() != null) {
                    updateButtons(newData.getPl1Back(), p1BackButtons, true);
                    updateButtons(newData.getPl1Lay(), p1LayButtons, false);
                    updateButtons(newData.getPl2Back(), p2BackButtons, true);
                    updateButtons(newData.getPl2Lay(), p2LayButtons, false);
                    double lpm1 = newData.getLastPriceMatched(PlayerEnum.PLAYER1);
                    double lpm2 = newData.getLastPriceMatched(PlayerEnum.PLAYER2);
                    
                    p1MarketInfoButtons[0].setOdds(lpm1 + "");
                    p2MarketInfoButtons[0].setOdds(lpm2 + "");
                    
                    if ( p1Values[0] > lpm1 ) {
                    	p1Values[0] = lpm1;
                    	p1MarketInfoButtons[3].setOdds(lpm1+"");
                    }
                    
                    if ( p1Values[1] < lpm1 ) {
                    	p1Values[1] = lpm1;
                    	p1MarketInfoButtons[5].setOdds(lpm1+"");
                    }
                    
                    if ( p2Values[0] > lpm2 ) {
                    	p2Values[0] = lpm2;
                    	p2MarketInfoButtons[3].setOdds(lpm2+"");
                    }
                    
                    if ( p2Values[1] < lpm2 ) {
                    	p2Values[1] = lpm2;
                    	p2MarketInfoButtons[5].setOdds(lpm2+"");
                    }
                    
                    p1MarketInfoButtons[4].setOdds(BetsDisplay.DOUBLE_FORMAT.format((p1Values[0] + p1Values[1])/2));
                    p2MarketInfoButtons[4].setOdds(BetsDisplay.DOUBLE_FORMAT.format((p2Values[0] + p2Values[1])/2));
                    
                    
                    double matched1 = newData.getPlayer1TotalAmountMatched();
                    double matched2 = newData.getPlayer2TotalAmountMatched();
                    	
                    p1MarketInfoButtons[1].setOdds(matched1 + "");
                    p2MarketInfoButtons[1].setOdds(matched2 + "");
                    
                    double total = matched1 + matched2;
                    double mktP1 = matched1 / total * 100;
                    double mktP2 = matched2 / total * 100;

                    p1MarketInfoButtons[2].setOdds(BetsDisplay.DOUBLE_FORMAT.format(mktP1) + "%");
                    p2MarketInfoButtons[2].setOdds(BetsDisplay.DOUBLE_FORMAT.format(mktP2) + "%");
                    UpdatableMarketDataGrid.this.layout();
                }
            }
        });
    }

    public OddsButton[] getP1BackButtons() {
        return p1BackButtons;
    }

    public void setP1BackButtons(OddsButton[] p1BackButtons) {
        this.p1BackButtons = p1BackButtons;
    }

    public OddsButton[] getP1LayButtons() {
        return p1LayButtons;
    }

    public void setP1LayButtons(OddsButton[] p1LayButtons) {
        this.p1LayButtons = p1LayButtons;
    }

    public OddsButton[] getP2BackButtons() {
        return p2BackButtons;
    }

    public void setP2BackButtons(OddsButton[] p2BackButtons) {
        this.p2BackButtons = p2BackButtons;
    }

    public OddsButton[] getP2LayButtons() {
        return p2LayButtons;
    }

    public void setP2LayButtons(OddsButton[] p2LayButtons) {
        this.p2LayButtons = p2LayButtons;
    }

    public void setBetController(BetController betController) {
        this.betController = betController;

    }

    public BetController getBetController() {
        return betController;
    }

    @Override
    public void setDisposeListener(DisposeListener listener) {
        this.addDisposeListener(listener);

    }
}
