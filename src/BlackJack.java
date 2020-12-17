import java.util.*;

public class BlackJack {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player;
    private final Player computer;
    private List<Card> deck;
    private final Scanner in;
    private boolean ongoingTurn = true;
    private int playerScore = 0;
    private int computerScore = 0;
    private int chips = -1;
    private int wager;
    private boolean turn = true;
    private int count = 0;

    public BlackJack() {
        this.player = new Player();
        this.computer = new Player();
        this.in = new Scanner(System.in);
    }
    public void play() {
        turn = true;
        startingChips();
        wager();
        clearHand("player");
        clearHand("computer");
        shuffleDeal();
        takeTurn(false);
        endRound();
    }
    private void startingChips() {
        if(chips == -1) {
            do {
                System.out.println("How many chips do you want to start with?");
                chips = in.nextInt();
                if (chips < 1) {
                    chips = -1;
                }
            } while (chips == -1);
        }
    }
    private void wager() {
        int chipMaxNum = chips;
        if(chips >= 25) {
            chipMaxNum = 25;
        } else {
            chipMaxNum = chips;
        }

        do {
            System.out.println("Your total chips right now: " + chips);
            if(chips > 0) {
                System.out.println("How many chips would you like to wager (Min: 1, Max: " + chipMaxNum + " )");
            } else {
                System.out.println("How many chips would you like to wager (Min: 0, Max: " + chipMaxNum + " )");
            }
            wager = in.nextInt();
            if (wager > chips) {
                System.out.println("Sorry, you only have " + chips + " chips remaining, but you wagered " + wager + " chips.");
            } if(wager > 25) {
                System.out.println("Sorry, you can only wager 25 or less.");
            }
        } while (wager < 1 || wager > chips || wager > 25);
    }
    public void shuffleDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);
        while (player.getHand(false).size() < 2) {
            player.takeCard(deck.remove(0));
            computer.takeCard(deck.remove(0));
        }

        playerScore = player.findScore();
        computerScore = computer.findScore();
    }
    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));
            }
        }
    }
    private void takeTurn(boolean cpu) {
        if (!cpu) {
            showHand("initial");
            String action = "x";
            while (action.equals("H") == false && action.equals("S") == false) {
                System.out.println("Hit (H) or Stand (S)");
                action = in.nextLine().toUpperCase();
            } if (action.equals("H")) {
                player.takeCard(deck.remove(0));
                playerScore = player.findScore();
                if (playerScore < 21) {
                    takeTurn(false);
                } else {
                    if(deck.contains("A")) {
                        playerScore = playerScore - 10;
                    }
                    endRound();
                }

            } else if (action.equals("S")) {
                takeTurn(true);
            }
        } else if (cpu) {
            showHand("cpu");
            while (computerScore < 17) {
                computer.takeCard(deck.remove(0));
                computerScore = computer.findScore();
                showHand("cpu");
            }
        }
    }

    private void endRound() {
        showHand("player");
        showHand("computer");

        if (playerScore > 21) {
            System.out.println("Sorry, your score has gone over a 21 and you have lost this round.");
            chips -= (wager);
            if (chips <= 0) {
                end();
            }
        } else if(computerScore > 21) {
            System.out.println("The computer has gone over 21 so you have won this round.");
            chips += wager;
        } else if (computerScore == playerScore && playerScore <= 21) {
            System.out.println("You have tied with the computer this round.");
        } else if (playerScore == 21 && player.hand.size() == 2) {
            System.out.println("Congratulations, that is a BlackJack. You have won this round.");
            chips += (int) (wager * 1.5);
        } else if (computerScore > playerScore && computerScore <= 21) {
            System.out.println("You have lost this round " + playerScore + " to " + computerScore + ".");
            chips -= wager;
        } else if (playerScore > computerScore && playerScore <= 21) {
            System.out.println("You have won this round " + playerScore + " to " + computerScore + ".");
            chips += wager;
        }
        String answer = "";
        do {
            System.out.println("Would you like to end the game here (E) or continue playing (C)");
            answer = in.nextLine().toUpperCase();
        } while (answer.equals("E") == false && answer.equals("C") == false);
        if (answer.equals("E")) {
            end();
        } else {
            play();
        }
    }
    private void showHand(String type) {
        if (type.equals("initial")) {
            System.out.println("\nPLAYER hand: " + player.getHand(false));
            System.out.println("Your Score:" + player.findScore());
            System.out.println("\nCPU hand: " + computer.getHand(true));
        } else if (type.equals("cpu")) {
            System.out.println("\nCPU hand: " + computer.getHand(false));
        } else if (type.equals("player")) {
            System.out.println("\nPLAYER hand: " + player.getHand(false));
        }
    }
    private void clearHand(String type) {
        if (type.equals("player")) {
            player.clearHand();
        } else if (type.equals("computer")) {
            computer.clearHand();
        }
    }
    private void end() {
        if (chips <= 0) {
            System.out.println("You ran out of chips and lost.");
        } else {
            System.out.println("You ended up with " + chips + " chips. Good job!");
        }
        System.exit(0);
    }
    public static void main(String[] args) {
        System.out.println("Ready to Gamble! Let's play BLACKJACK!");
        new BlackJack().play();
    }
}