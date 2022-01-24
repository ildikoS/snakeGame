/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Ildikó
 */
class SnakeGameGUI extends JFrame{
    
    private static SnakeGameBoard board;
    static JLabel gameStatLabel;
    static int time = 0;
    java.util.Timer PlayedSeconds = new java.util.Timer();

    public SnakeGameGUI() {
        setTitle("Snake Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        board = new SnakeGameBoard();
        
        createMenu();
        
        createLabel();
        refreshGameStatLabel();
        startTime();
                
        setResizable(false);
        getContentPane().add(board, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }
   
    /**
     * Creates label
     */
    public void createLabel() {
        setLayout(new BorderLayout(0, 10));
        gameStatLabel = new JLabel("label");
        add(gameStatLabel, BorderLayout.NORTH);
    }
    
    /**
     * Refreshes label with points and spent time
     */
    public static void refreshGameStatLabel(){
        String s = "Pontszám: " + board.getScore() + "   Eltelt másodpercek: " + time;
        gameStatLabel.setText(s);
    }
    
    /**
     * Creates new game, exit, top10 list and game type menus.
     */
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Játék");
        JMenu menuGameType = new JMenu("Típus");
        createGameTypeMenuItems(menuGameType);
        
        JMenuItem menuTopTen = new JMenuItem(new AbstractAction("Top10") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HighScores highScores = new HighScores(10);
                    String scores = highScores.getHighScores().toString().replace(",", "").replace("[", "");
                    String[] data = scores.replace("]", "").split(" ");
                    JDialog dialog = new JDialog();
                    JPanel panel = new JPanel();
                    String[] header = {"Játékos", "Elért Pontok"};
                    DefaultTableModel model = new DefaultTableModel();
                    model.setColumnIdentifiers(header);
                    JTable scoreboard = new JTable();
                    scoreboard.setModel(model);

                    ArrayList<String> ar = new ArrayList<>();
                    for (int i = 0; i < data.length; i++) {
                        ar.add(data[i]);
                    }

                    for (int i = 0; i < (ar.size() / 2); i++) {
                        model.addRow(new Object[]{
                            String.valueOf(ar.get(2 * i)),
                            String.valueOf(ar.get((2 * i) + 1))
                        });
                    }
                    
                    panel.add(scoreboard);
                    dialog.add(panel);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setTitle("Dicsőség tábla");
                    dialog.setLocationRelativeTo(null);
                    dialog.pack();
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(SnakeGameGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JMenuItem menuGameNew = new JMenuItem(new AbstractAction("Új játék") {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Kilépés") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuGame.add(menuGameNew);
        menuGame.add(menuGameExit); 
        menuGame.addSeparator();
        menuGame.add(menuTopTen);
        menuGame.addSeparator();
        menuGame.add(menuGameType);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);
    }
    
    /**
     * New game method for new game menu, resets game properties
     */
    private void startNewGame() {
        setResizable(false);
        setLocationRelativeTo(null);
        board.startNewGame();
        pack();
        refreshGameStatLabel();
        setVisible(true);
        time=0;
    }
    
    /**
     * Increments time per second, refresh label
     */
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(board.getRunning())
            time++;
            refreshGameStatLabel();
        }
    };

    private void startTime() {
        PlayedSeconds.scheduleAtFixedRate(timerTask, 1000, 1000);
    }
    
    /**
     * Actionlistener for the game type menu, sets images in SnakeGameBoard then repaints 
     * @param menu 
     */
    private void createGameTypeMenuItems(JMenu menu) {
        JMenuItem itemDesert = new JMenuItem(new AbstractAction("Sivatag") {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setImages('S');
                board.refresh();
                pack();
            }
        });
        JMenuItem itemSnow = new JMenuItem(new AbstractAction("Téli") {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setImages('T');
                board.refresh();
                pack();
            }
        });
        menu.add(itemDesert);
        menu.add(itemSnow);
    }
}
