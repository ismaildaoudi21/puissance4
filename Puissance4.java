

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

class Puissance4 implements ActionListener 
{
    /* test */ 
    /* La grille */
    JFrame frame = new JFrame();
    /* les buttons */
    JPanel button_panel = new JPanel();
    /* Création des buttons */
    JButton[][] buttons = new JButton[6][7];
    
    /* boolean pour savoir le tour de quel joueur */
    boolean player1_turn;
    Puissance4() {
        
        /*** Intérface graphique ***/

        /* c'est pour arrêter le programme si on exit depuis l'interface graphique */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /*définir la taille de l'interface */ 
        frame.setSize(800,800);
        
        /* L'interface doit être visible */
        frame.setVisible(true);

        /* la grille contient 6 lignes et 7 colonnes et un espace de 20  entre les buttons */
        button_panel.setLayout(new GridLayout(6, 7, 20, 20));

        /* définir la couleur de la grille */
        button_panel.setBackground(new Color(0,50,225));

        /* définir le style de chaque button puis l'ajouter dans la grille*/
        for(int i = 0; i < 6; i++){
            for(int j = 0; j <7; j++){

                /* creer un nouveau button*/
                buttons[i][j] = new JButton();

                /* l'ajouter dans la grille */
                button_panel.add(buttons[i][j]);
                
                /* définir le style de texte affiché dans le button */
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 120));
                
                /* le rendre Focusable */ 
                buttons[i][j].setFocusable(true);

                //buttons[i][j].setPreferredSize( new Dimension(78, 76));

                /* ajouter un ActionListener à chaque button */ 
                buttons[i][j].addActionListener(this);

                /* rendre le button transparent */
                buttons[i][j].setContentAreaFilled(false);


                
            }
        }

        /* Ajouter la grille dans l'interface graphique */
        frame.add(button_panel);
    }
    

    /*** Ce qui se passe qu'on clique un button ***/
    @Override
    public void actionPerformed(ActionEvent e){
        for(int i = 0; i < 6; i++){
            for(int j = 0; j <7; j++){
                
                /* savoir quel button est cliqué */
                if(e.getSource()==buttons[i][j]) {

                    /* si c'est le tour du permier joueur */
                    if(player1_turn) {

                        /* verifier si la case est vide */
                        if(buttons[i][j].getText()=="") {
                            int nb_l = 5;
                           
                            /* verifier si la ligne d'avant est vide ou non */
                            while (buttons[nb_l][j].getText()!=""){
                                nb_l--;
                            }

                        /* l'action est faite sur la bonne case */ 
                        buttons[nb_l][j].setForeground(new Color(255,0,0));
                        buttons[nb_l][j].setText("O");

                        /* le tour passe à l'autre joueur */
                        player1_turn=false;
                        }
                    }
                    else {
                        if(buttons[i][j].getText()=="") {
                            int nb_l = 5;
                            while (buttons[nb_l][j].getText()!=""){
                                nb_l--;
                            
                            }
                        buttons[nb_l][j].setForeground(new Color(253,253,0));
                        buttons[nb_l][j].setText("O");
                        player1_turn=true;
                        }
                    }
                }	
            }
        }
    }


    public static void main(String[] args)
    {


        Puissance4 puissance4 = new Puissance4();

    }
}
