package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashSet;

class PokerSinglePlayer extends PokerGameGui {

    /**
     * Timer for calling turnDecider()
     */
    Timer timer;

    /**
     * The milliseconds for timer
     */
    int timer_value = 2000; // milliseconds

    /**
     * Whether or not it's your turn to bet
     */
    boolean yourTurnToBet = true;


    /**
     * No arg constructor to create instance of PokerSinglePlayer to begin game
     */
    public PokerSinglePlayer(){
        player.setDelegate(this);
        opponent.setDelegate(this);
    }
    
    /**
     * Constructor to set the player and opponent's initial chips. 
     * This is used when we continue playing the game
     * @param pChips the player's chips
     * @param oChips the opponent's chips
     */
    public PokerSinglePlayer(int pChips, int oChips){
        player.setChips(pChips);
        opponent.setChips(oChips);
        player.setDelegate(this);
        opponent.setDelegate(this);
    }
    
    /**
     * Starts game between you and AI
     */
    public void go() {
        super.setUp();
        layoutSubViews(); //sets up all of the buttons and panels and GUI
        controlButtons(); //this function is in PokerGameGui.

        if(!gameOver){ 
            step = Step.BLIND; //This is the first step in the game.
            turn = Turn.OPPONENT;
            prompt = "opponent goes first!";
            
            int rng = (int) (Math.random()*2); //generate a random 0 or 1 
            if (rng == 1) { //1 = player 1 goes first.
                turn = Turn.PLAYER;
                message = "player goes first!";
                prompt = "what will you do?";
              
            }
            //Here, we are using a timer to control how the turns work
            //The timer comes from the swing class if you want to read up on it
            //Another thing to note is we used a lambda function deal with the thread in timer.
            timer = new Timer(timer_value, e -> turnDecider() );
            timer.setRepeats(false); //We want the timer to go off once. We will restart it as needed instead.
            updateFrame(); //function is inside of PokerGameGui
            timer.restart();
        }
    }

    /**
     * Method that directs the turn to who it needs to go to
     */
    public void turnDecider () {
        if (turn == Turn.PLAYER) {
            player.takeTurn();
        }
        else {
            opponent.takeTurn();
        }
    }

    
    /**
     * Method to activate the opponent AI on turn change.
     * Changes between you and the AI
     */
    public void changeTurn() {
        if (turn == Turn.PLAYER) {
            if (responding == true) {
                turn = Turn.OPPONENT;
                controlButtons();
                message = "opponent is thinking...";
                updateFrame();		
                timer.restart();
                } else {
                    updateFrame();
                    nextStep();
                    if (step != Step.SHOWDOWN) {
                        turn = Turn.OPPONENT;
                        controlButtons();
                        prompt = "opponent Turn.";
                        message = "opponent is thinking...";
                        updateFrame();
                        timer.restart();
                    }
                }
        } else if (turn == Turn.OPPONENT) {
            if (responding == true) {
                turn = Turn.PLAYER;
                controlButtons();
                responding = false;
                prompt = "What will you do?";
                updateFrame();
            } else {
                prompt = "What will you do?";
                turn = Turn.PLAYER;
                controlButtons();
                updateFrame();
            }
        }
    }

    /**
     * Updates GUI based on the player's decision
     */
    public void userTurn() {
        controlButtons();
        updateFrame();
    }

    /**
     * Method that moves the game to the next phase
     */
	public void nextStep() {
        if (step == Step.BLIND) { // Most like able to skip/remove this step
            step = Step.FLOP;
        } else if (step == Step.FLOP) {
            step = Step.TURN;
        } else if (step == Step.TURN) {
            step = Step.RIVER;
        } else {
            step = Step.SHOWDOWN;
            message = "All bets are in.";
            prompt = "Determine Winner: ";
            controlButtons();
        }
    }
	
	/**
	* Method overridden to allow for a new single player game to start.
	*/
	public void showWinnerAlert() {
		if(!gameOver){							// Branch 1

			String message = "";
			oSubPane2.remove(backCardLabel1);
			oSubPane2.remove(backCardLabel2);
			for(int i=0;i<2;i++){				// Branch 2

				oSubPane2.add(new JLabel(getCardImage((opponent.getHand()).get(i))));
			}
			updateFrame();

			message = winningHandMessage();

			if (winnerType == Winner.PLAYER) {	// Branch 3

				System.out.println("player");
				message = message + ("\n\nYou win!\n\nNext round?");
			} else if (winnerType == Winner.OPPONENT) {	// Branch 4

				System.out.println("opponent");
				message = message + ("\n\nOpponent wins.\n\nNext round?");
			} else if (winnerType == Winner.TIE){		// Branch 5

				System.out.println("tie");
				message = message + ("\n\nTie \n\nNext round?");
			} else {

			}

			int option = showWinnerConfirmDialog(message);

			if (option == JOptionPane.YES_OPTION) {		//Branch 6

				// Restart
				mainFrame.dispose();
				PokerSinglePlayer singlePlayerReplay;

				// Check if players have enough chips.
				// Create new game.
				if(player.getChips() < 5 || opponent.getChips() < 5){	// Branch 7 & 8

					JOptionPane.showMessageDialog(null, "Resetting chips...");
					singlePlayerReplay = new PokerSinglePlayer();
					singlePlayerReplay.go();
				} else {

					singlePlayerReplay = new PokerSinglePlayer(player.getChips(),opponent.getChips());
					singlePlayerReplay.go();
				}
			} else if (option == JOptionPane.NO_OPTION) {				// Branch 9

				if(player.getChips() < 5 || opponent.getChips() < 5) {	// Branch 10 & 11

					gameOver("GAME OVER! No chips left!");
				} else {

				}
				gameOver("GAME OVER! Thanks for playing.\n\n");
			} else {

				// Quit
				System.exit(1);
			}
		} else {

		}

		// CC calculation: (11 - 1) + 2 = 12
	}

	public int showWinnerConfirmDialog(String message) {
		return JOptionPane.showConfirmDialog(null, message, "Winner",
				JOptionPane.YES_NO_OPTION);
	}

    /**
     * Restarts the timer controlling turnDecider()
     */
    public void restartTimer() {
        timer.restart();
    }
}
