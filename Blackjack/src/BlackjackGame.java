import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlackjackGame extends JFrame implements ActionListener {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private JButton hitButton;
    private JButton standButton;
    private JTextArea playerTextArea;
    private JTextArea dealerTextArea;

    public BlackjackGame() {
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();

        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        playerTextArea = new JTextArea();
        dealerTextArea = new JTextArea();

        hitButton.addActionListener(this);
        standButton.addActionListener(this);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(hitButton);
        buttonsPanel.add(standButton);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(2, 1));
        cardsPanel.add(playerTextArea);
        cardsPanel.add(dealerTextArea);

        setLayout(new BorderLayout());
        add(cardsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);

        startGame();
    }

    public void startGame() {
        deck.shuffle();

        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        updatePlayerTextArea();
        updateDealerTextArea();
    }

    private void updatePlayerTextArea() {
        playerTextArea.setText("Player's Hand:\n" + playerHand.toString() +
                "Player's Hand Value: " + playerHand.getValue());
    }

    private void updateDealerTextArea() {
        dealerTextArea.setText("Dealer's Hand:\n" + dealerHand.toString() +
                "Dealer's Hand Value: " + dealerHand.getValue());
    }

    private void handleHit() {
        Card card = deck.drawCard();
        playerHand.addCard(card);
        updatePlayerTextArea();

        if (playerHand.getValue() > 21) {
            endGame("Player Busts! You lose.");
        }
    }

    private void handleStand() {
        while (dealerHand.getValue() < 17) {
            Card card = deck.drawCard();
            dealerHand.addCard(card);
            updateDealerTextArea();
        }

        if (dealerHand.getValue() > 21) {
            endGame("Dealer Busts! You win.");
        } else if (playerHand.getValue() > dealerHand.getValue()) {
            endGame("You win!");
        } else if (playerHand.getValue() < dealerHand.getValue()) {
            endGame("You lose.");
        } else {
            endGame("It's a tie.");
        }
    }

    private void endGame(String message) {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        int choice = JOptionPane.showConfirmDialog(this, message + "\nDo you want to restart the game?", "Game Over", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            playerHand.clear();
            dealerHand.clear();
            deck = new Deck();

            startGame();

            hitButton.setEnabled(true);
            standButton.setEnabled(true);
        } else {
            System.exit(0);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hitButton) {
            handleHit();
        } else if (e.getSource() == standButton) {
            handleStand();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BlackjackGame());
    }
}

class Card {
    private final String suit;
    private final String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public int getValue() {
        if (rank.equals("Ace")) {
            return 11;
        } else if (rank.equals("King") || rank.equals("Queen") || rank.equals("Jack")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

class Deck {
    private final java.util.List<Card> cards;

    public Deck() {
        cards = new java.util.ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
    }

    public void shuffle() {
        java.util.Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }
}

class Hand {
    private final java.util.List<Card> cards;

    public Hand() {
        cards = new java.util.ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getValue() {
        int value = 0;
        int numAces = 0;

        for (Card card : cards) {
            value += card.getValue();
            if (card.getValue() == 11) {
                numAces++;
            }
        }

        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card).append("\n");
        }
        return handString.toString();
    }
}
