package com.auction.myAuction.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AuctionController {

    // Stores current price of each player
    public List<Integer> currentPrice = new ArrayList<>();
    // Player names (indexed by ID)
    public List<String> playersNames = new ArrayList<>();
    // List of players each team has bought
    public List<List<String>> teamPlayers = new ArrayList<>();
    // Purse remaining for each team
    public List<Integer> currentPurse = new ArrayList<>();
    // Mapping of playerId → teamId
    public HashMap<Integer, Integer> playerTeamMapping = new HashMap<>();
    // List of team names
    public List<String> teamName = new ArrayList<>();

    public AuctionController() {
        // Initialize base data
        for (int i = 0; i < 50; i++) currentPrice.add(0);
        for (int i = 0; i < 9; i++) {
            currentPurse.add(100000); // starting purse (modify if needed)
            teamPlayers.add(new ArrayList<>());
        }

        // Player Names
        playersNames.add("ANUJ SHARMA");
        playersNames.add("GOURAV MITTAL");
        playersNames.add("PIYUSH SINGH DHAKAD");
        playersNames.add("SHIRISH PAREEK");
        playersNames.add("CHARUL CHOUDHARY");
        playersNames.add("YASHIKA SHARMA");
        playersNames.add("HEMANG BHABHRA");
        playersNames.add("NAINAV PANCHOLI");
        playersNames.add("KARAN PARWANI");
        playersNames.add("VRINDA SINGHAL");
        playersNames.add("TANMAY KHANDELWAL");
        playersNames.add("ANOUSHKA PRAKASH");
        playersNames.add("DHARNA GUNIDIA");
        playersNames.add("SHRUTI KHANDELWAL");
        playersNames.add("ADITYA GOYAL");
        playersNames.add("AVIRAL SHARMA");
        playersNames.add("MANISHA PARWANI");
        playersNames.add("ANVESHA MITTAL");
        playersNames.add("SAGAR AGARWAL");
        playersNames.add("KARISHMA JETHWANI");
        playersNames.add("MEHAK JHA");
        playersNames.add("ABHINAV GUPTA");
        playersNames.add("SANCHIT PAWA");
        playersNames.add("MRIDUL KRISHNA SHARMA");
        playersNames.add("KRISHNA SRIVASTAVA");
        playersNames.add("YUVAN KHADOLIA");
        playersNames.add("YASHOWARDHAN SINGH WAGHELA");
        playersNames.add("SEHAR NAWAZ ALI");
        playersNames.add("MEHUL SHARMA");
        playersNames.add("CHARCHIT JAIN");
        playersNames.add("DHRUVIKA KHANDELWAL");
        playersNames.add("VANSH JAIN");
        playersNames.add("SUCHIT GUPTA");
        playersNames.add("HARSHIT KUSHWAHA");
        playersNames.add("ANSH RAJ");
        playersNames.add("YASHVARDHAN THANVI");
        playersNames.add("ANISHA MOTWANI");
        playersNames.add("GARVIT SAXENA");
        playersNames.add("NISCHAL AGRAWAL");
        playersNames.add("DEVESH JAIN");
        playersNames.add("SHAIKH FAHAD");
        playersNames.add("VANSH NAGAR");
        playersNames.add("DURGESH SAINI");
        playersNames.add("GAURANSHI MAHAWAR");
        playersNames.add("MUSKAN JAIN");
        playersNames.add("DISHA KODASIA");
        playersNames.add("VAIBHAV");
        playersNames.add("SHIVIN AGRAWAL");

        // Team Names
        teamName.add("Byte Busters");
        teamName.add("Ruby Renegades");
        teamName.add("Java Jesters");
        teamName.add("Code Commanders");
        teamName.add("Syntax Samurai");
        teamName.add("Data Mavericks");
        teamName.add("Quantum Coders");
        teamName.add("Logic Luminaries");
        teamName.add("Python Pioneers");
    }

    // ------------------- API Endpoints -------------------

    // Get current price of a player
    @GetMapping("/getCurrentPrice")
    public Integer getCurrentPrice(@RequestParam Integer id) {
        if (id < 0 || id >= currentPrice.size()) return -1;
        return currentPrice.get(id);
    }

    // Get current purse of all teams
    @GetMapping("/getPurse")
    public List<Integer> getPurse() {
        return currentPurse;
    }

    // Get the current team of a player (Safe)
    @GetMapping("/getCurrentTeamOfPlayer")
    public String getCurrentTeamOfPlayer(@RequestParam Integer id) {
        Integer teamIndex = playerTeamMapping.get(id);
        if (teamIndex == null || teamIndex < 0 || teamIndex >= teamName.size()) {
            return "Not Sold Yet";
        }
        return teamName.get(teamIndex);
    }

    // Update a player's price & assign to a team
    @GetMapping("/updateCurrentPrice")
    public String updateCurrentPrice(
            @RequestParam Integer id,
            @RequestParam Integer newPrice,
            @RequestParam Integer teamId) {

        if (id < 0 || id >= playersNames.size()) return "Invalid player ID";
        if (teamId < 0 || teamId >= teamPlayers.size()) return "Invalid team ID";

        // Prevent lower bids than current price
        if (newPrice <= currentPrice.get(id)) {
            return "Bid must be higher than current price!";
        }

        // Check if team has enough purse
        if (currentPurse.get(teamId) < newPrice) {
            return teamName.get(teamId) + " does not have enough purse!";
        }

        // If player was already sold → revert old team’s money
        if (playerTeamMapping.containsKey(id)) {
            int prevTeamId = playerTeamMapping.get(id);
            int prevPrice = currentPrice.get(id);

            currentPurse.set(prevTeamId, currentPurse.get(prevTeamId) + prevPrice);
            teamPlayers.get(prevTeamId).remove(playersNames.get(id));
        }

        // Deduct purse from new team
        currentPurse.set(teamId, currentPurse.get(teamId) - newPrice);

        // Update price and team ownership
        currentPrice.set(id, newPrice);
        teamPlayers.get(teamId).add(playersNames.get(id));
        playerTeamMapping.put(id, teamId);

        return playersNames.get(id) + " — current highest bid by " + teamName.get(teamId) + " at ₹" + newPrice;
    }

    // Get list of players owned by a team
    @GetMapping("/getTeamPlayers")
    public List<List<String>> getTeamPlayers(@RequestParam Integer id) {
        if (id < 0 || id >= teamPlayers.size()) return Collections.emptyList();
        return Collections.singletonList(teamPlayers.get(id));
    }
}
