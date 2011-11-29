package org.ic.tennistrader.domain.match;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.exceptions.MatchNotFinishedException;
import org.ic.tennistrader.service.FracsoftReader;
import org.ic.tennistrader.utils.Pair;

public class HistoricalMatch extends Match {
    private String name;

    public HistoricalMatch(Player player1, Player player2) {
        this.score = new Score(3);
        this.player1 = player1;
        this.player2 = player2;
    }

    public HistoricalMatch(String filename) {
        this.score = new Score();
        this.marketDatas = new ArrayList<MOddsMarketData>();

        Pair<String, String> p = FracsoftReader.getPlayerNames(filename);
        setNames(player1, p.first());
        setNames(player2, p.second());
        this.filename = filename;
        this.name = getMatchName();
    }

    private void setNames(Player player, String name) {
        String names[] = name.split(" ");
        String firstname = names[0];

        for (int i = 1; i < names.length - 1; i++)
            firstname += " " + names[i];

        player.setFirstname(firstname);
        player.setLastname(names[names.length-1]);
    }

    @Override
    public boolean isInPlay() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    // needs to be in a different class!!!
    public String getMatchName() {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Match name";
        }
        String mName = scanner.nextLine();
        scanner.close();
        return mName;
    }

    public String toString() {
        return getName();
    }

    @Override
    public PlayerEnum getWinner() throws MatchNotFinishedException {
        return this.score.getWinner();
    }

    @Override
    public void addMarketData(MOddsMarketData data) {
        this.marketDatas.add(data);
    }

    @Override
    public boolean isFromFile() {
        return true;
    }
}
