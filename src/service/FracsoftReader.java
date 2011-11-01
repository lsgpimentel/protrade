package src.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import org.apache.log4j.Logger;

import src.Pair;
import src.domain.EventBetfair;
import src.domain.MOddsMarketData;
import src.domain.Match;

/**
 * Reads data in Fracsoft format from a given file
 * 
 * @author Paul Grigoras
 * 
 */
public class FracsoftReader implements DataUpdater {

    private static Logger log = Logger.getLogger(FracsoftReader.class);

    private List<MOddsMarketData> matchDataList = new ArrayList<MOddsMarketData>();
    
    private Iterator<MOddsMarketData> pointer = null;
    
    private int inPlayPointer = -1;
    
    private Match match;
    
    private static final int DELAY_OFFSET = 1;
    private static final int NAME_OFFSET = 4;
    private static final int BACK_OFFSET = 5;
    private static final int LAY_OFFSET = 11;
    
    
    public FracsoftReader(Match match, String filename) throws FileNotFoundException {
        this.match = match;
        log.info("Creating fracsoft reader from file " + filename);
        String line1, line2;
        Scanner scanner = null;
        
        try {
            scanner = new Scanner(new FileInputStream(filename));

            // skip file header
            for (int i = 0; i < 3; i++) {
                scanner.nextLine();
            }

            // start parsing data
            int i = 0;
            while (scanner.hasNextLine()) {
                line1 = scanner.nextLine();
                line2 = scanner.nextLine();
                String[] lines1 = line1.split(",");
                String[] lines2 = line2.split(",");

                MOddsMarketData data = new MOddsMarketData();
                data.setDelay(Integer.parseInt(lines1[DELAY_OFFSET]));

                // player 1 data
                data.setPlayer1(lines1[NAME_OFFSET]);
                data.setPl1Back(getOdds(lines1, BACK_OFFSET));
                data.setPl1Lay(getOdds(lines1, LAY_OFFSET));
                
                // player 2 data
                data.setPlayer2(lines2[NAME_OFFSET]);
                data.setPl2Back(getOdds(lines2, BACK_OFFSET));
                data.setPl2Lay(getOdds(lines2, LAY_OFFSET));
                
                matchDataList.add(data);
                if ( data.getDelay() != 0 && inPlayPointer == -1)
                    inPlayPointer = i;
                i++;
                
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }

        finally {
            if (scanner != null)
                scanner.close();
        }
        
        //pointer = matchDataList.iterator();
        pointer = matchDataList.listIterator(inPlayPointer);
    }

    private ArrayList<Pair<Double, Double>> getOdds(String[] lines1, int offset) {
        ArrayList<Pair<Double, Double>> pl1Backs = new ArrayList<Pair<Double,Double>>();
        for (int i=0;i<3;i++){
            double odds = Double.parseDouble(lines1[offset + i*2 ]);
            double amount = Double.parseDouble(lines1[offset + i*2 + 1]);
            Pair<Double, Double> p = new Pair<Double, Double>(odds, amount);
            pl1Backs.add(p);
        }
        return pl1Backs;
    }
    
    @Override
    public void run() {
        
        MOddsMarketData newData = getMarketData();

        HashMap<EventBetfair, MOddsMarketData> newMap = new HashMap<EventBetfair, MOddsMarketData>();
        
        if (newData != null )
            newMap.put(match.getEventBetfair(), newData);
        
        LiveDataFetcher.handleEvent(newMap);
    }
    
    private MOddsMarketData getMarketData(){
        if (pointer.hasNext())
            return pointer.next();
        return null;
    }

    @Override
    public void addEvent(Match match) {
        this.match = match;
    }

}
