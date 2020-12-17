import java.util.ArrayList;
import java.util.List;

public class Player {

    public final List<Card> hand;
    private int chips = 25;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public List<Card> getHand(boolean partial) {
        if (partial == true) {
            return hand.subList(0,1);
        } else {
            return hand;
        }
    }

    public void takeCard(Card card) {
        hand.add(card);
        sortHand();
    }

    public void clearHand() {
        hand.clear();
    }

    public int findScore() {
        int score = 0;
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for (int k = 0; k < hand.size(); k++) {
            String playerTemp = hand.get(0).getRank();
            if (playerTemp.equals("A")) {
                scores.add(11);
            } else {
                scores.add(Card.getOrderedRank(hand.get(k).getRank()));
            }
        }
        score = addList(scores);
        while (score > 21) {
            if (!scores.contains(11)) {
                break;
            }
            for (int q = 0; q < scores.size(); q++) {
                if (scores.get(q) == 11) {
                    scores.set(q,1);
                    break;
                }
            }
            score = addList(scores);
        }
        return score;
    }

    private int addList(ArrayList<Integer> list) {
        int total = 0;
        for (int j = 0; j < list.size(); j++) {
            total += list.get(j);
        }
        return total;
    }

    private void sortHand() {
        hand.sort((a, b) -> {
            if (Card.getOrderedRank(a.getRank()) == Card.getOrderedRank(b.getRank())) {
                return Card.getOrderedSuit(a.getSuit()) - Card.getOrderedSuit(b.getSuit());
            }

            return Card.getOrderedRank(a.getRank()) - Card.getOrderedRank(b.getRank());
        });
    }

}