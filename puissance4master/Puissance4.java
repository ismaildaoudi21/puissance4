import packp4.*;

import javax.swing.*;
import java.awt.event.*;
import java.net.CookiePolicy;
import java.awt.*;
import java.util.*;
import java.util.List;

class Puissance4 implements ActionListener 
{
    // L'alignement qui permet la victoire
    int N = 4;
    // La taille du plateau : colonnes et lignes
    int colonne = 7;
    int ligne = 6;


	Random random = new Random();
	JFrame frame = new JFrame();
	JPanel title_panel = new JPanel();
	JPanel button_panel = new JPanel();
	JLabel textfield = new JLabel();
    JButton[][] buttons =
    new JButton[ligne][colonne];
    int profondeurMax = 1;

    /* Représentation du plateau */
    int[][] plateau = new int[ligne][colonne];

    /* booléen qui indique le joueur en train de jouer */
    boolean player1_turn = true;

    /* booléen qui indique s'il y a une victoire */
    boolean victoire;

    /*booléen qui indique si on joue avec l'ordi ou un humain*/
    boolean bot;

    int tour = 0; // compteur de tour de jeu 

    Case lastPlay = new Case(0, 0);

    Case botPlay = new Case(5, 2);

    static int difficult;
    Puissance4() {
        
        /*** Intérface graphique ***/

        /* c'est pour arrêter le programme si on exit depuis l'interface graphique */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /*définir la taille de l'interface */ 
        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(20,50,50));
		frame.setLayout(new BorderLayout());
        frame.setSize(1000,800);
		frame.setVisible(true);
		
		textfield.setBackground(new Color(0, 204, 0));
		textfield.setForeground(new Color(250, 250, 240));
		textfield.setFont(new Font("Arial",Font.BOLD,75));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setText("Puissance 4");
		textfield.setOpaque(true);
		
		title_panel.setLayout(new BorderLayout());
		title_panel.setBounds(0,0,800,100);
		



        /* la grille contient 6 lignes et 7 colonnes et un espace de 20  entre les buttons */
        button_panel.setVisible(true);
        button_panel.setLayout(new GridLayout(ligne, colonne));


        /* définir la couleur de la grille */
        button_panel.setBackground(new Color(0,50,225));
        /* définir le style de chaque button puis l'ajouter dans la grille*/
        for(int i = 0; i < ligne; i++){
            for(int j = 0; j < colonne; j++){

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
		title_panel.add(textfield);
		frame.add(title_panel,BorderLayout.NORTH);
		frame.add(button_panel);
        frame.add(button_panel);
    }

        public boolean verifVictoire(Case last, int A){
            
            int joueur = plateau[last.i][last.j]; // Dernier coup joué
            int alignt = 0; // Nombre d'alignement max
            int l = last.i;
            int c = last.j;
            int x = l;
            int y = c;
            int compteur = -1;
            //afficher();

            /* Test des diagonales : */
            // Haut-Gauche vers Bas-Droite d'abord...

            while(x >= 0 && y >= 0 && plateau[x][y] == joueur){
                x--; y--; compteur++;
            }
            x = l; y = c;
            while(x < ligne && y < colonne && plateau[x][y] == joueur){
                x++; y++; compteur++;
            }
            if(compteur > alignt)
            alignt = compteur;

            // ... puis Bas-Gauche vers Haut-Droite

            x = l; y = c; compteur = -1;
            while(x < ligne && y >= 0 && plateau[x][y] == joueur){
                x++; y--; compteur++;
            }
            x = l; y = c;
            while(x >= 0 && y < colonne && plateau[x][y] == joueur){
                x--; y++; compteur++;
            }
            if(compteur > alignt)
                alignt = compteur;

            /* Test de l'horizontale */

            x = l; y = c; compteur = -1;
            while( y>= 0 && plateau[x][y] == joueur){
                y--; compteur++;
            }
            y = c;
            while(y < colonne && plateau[x][y] == joueur){
                y++; compteur++;
            }
            if(compteur > alignt)
                alignt = compteur;

            /* Test de la verticale */

            x = l; y = c; compteur = -1;
            while(x >= 0 && plateau[x][y] == joueur){
                x--; compteur++;
            }
            x = l;
            while(x < ligne && plateau[x][y] == joueur){
                x++; compteur++;
            }
            if(compteur > alignt)
                alignt = compteur;

            /* Alors ? Un vainqueur ? */

            if(alignt >= A)
                return true;
            else
                return false;
    }
    
    public void afficher() {
        for(int i = 0; i < ligne; i++){
            for(int j = 0; j < colonne; j++){ 
                System.out.print(plateau[i][j]);
            }
            System.out.print("\n");
        }

        System.out.println(lastPlay.i + " " + lastPlay.j);
    }

    public int aleatoire(int n) {
        int min = 1;

        int value = random.nextInt(n + min) + min;
        return value;
    }

    List<Case> casesDisponibles(){
        int nb_l;
        List<Case> casesDispo = new ArrayList<>();
        for(int i = 0; i < colonne; i++){
            if(plateau[0][i] == 0){
                nb_l = ligne - 1;
                /* verifier si la ligne d'avant est vide ou non */
                while (plateau[nb_l][i] != 0){
                    nb_l--;
                }
                casesDispo.add(new Case(nb_l, i));
            }
        }
        return casesDispo;
    }


    public int minmax(){

        /*liste des coups possibles*/
        List<Case> listeCoup = casesDisponibles();

        /*liste des coups qui bloquent un alignement de trois jetons*/
        List<Case> listeTresBonCoup = new ArrayList<>();

        /*liste des coups qui permettent l'alignement de trois jetons*/
        List<Case> listeBonCoup = new ArrayList<>();

        /*liste des coups qui permettent l'alignement de deux jetons*/
        List<Case> listeMoyenCoup = new ArrayList<>();


        for(int i = 0; i < listeCoup.size(); i++){
            Case coup = listeCoup.get(i);
            plateau[coup.i][coup.j] = 2;

            /*si une victoire en un coup est possible on joue*/
            if(verifVictoire(coup, 4)){
                botPlay = coup;
                plateau[coup.i][coup.j] = 0;
                return 0;
            }

            /*si un alignement est possible on l'ajoute a la bonne liste*/
            if(verifVictoire(coup, 3)){
                listeBonCoup.add(coup);
                //plateau[coup.i][coup.j] = 0;
            }
            if(verifVictoire(coup, 2)){
                listeMoyenCoup.add(coup);
                //plateau[coup.i][coup.j] = 0;
            }

            plateau[coup.i][coup.j] = 1;

            /*si une victoire en un coup est possible pour l'adversaire on bloque la case*/
            if(verifVictoire(coup, 4)){
                botPlay = coup;
                plateau[coup.i][coup.j] = 0;
                return 0;
            }

            /*si un alignement est possible pour l'adversaire on l'ajoute a la bonne liste*/
            if(verifVictoire(coup, 3)){
                listeTresBonCoup.add(coup);
                //plateau[coup.i][coup.j] = 0;
            }

            plateau[coup.i][coup.j] = 0;
        }

        /*choix du coup en fonction des priorités*/
        
        if(difficult == 0){ 
            System.out.println("difficulté 1");
            if(listeTresBonCoup.size() == 0){
                if(listeBonCoup.size() == 0){
                    botPlay = listeCoup.get(aleatoire(listeCoup.size() - 1) -1);
                }
                else{
                    botPlay = listeBonCoup.get(aleatoire(listeBonCoup.size() - 1) -1);
                }
            }
            else{
                botPlay = listeTresBonCoup.get(aleatoire(listeTresBonCoup.size() - 1) -1);
            }
        }

        if(difficult == 1){ 
            System.out.println("difficulté 2");
            Case coup;
            if(listeTresBonCoup.size() > 0){
                for(int i = 0; i < listeTresBonCoup.size(); i++){
                    coup = listeTresBonCoup.get(i);
                    if(listeBonCoup.contains(coup)){
                        botPlay = coup; 
                        return 0;
                    }
                }
                for(int i = 0; i < listeTresBonCoup.size(); i++){
                    coup = listeTresBonCoup.get(i);
                    if(listeMoyenCoup.contains(coup)){
                        botPlay = coup; 
                        return 0;
                    }
                }
                botPlay = listeTresBonCoup.get(aleatoire(listeTresBonCoup.size() - 1) -1);
            }
            else if(listeBonCoup.size() > 0){
                botPlay = listeBonCoup.get(aleatoire(listeBonCoup.size() - 1) -1);
            }       
            else if(listeMoyenCoup.size() > 0){
                botPlay = listeMoyenCoup.get(aleatoire(listeMoyenCoup.size() - 1) -1);
            }
            else{
                botPlay = listeCoup.get(aleatoire(listeCoup.size() - 1) -1);
            }
        }

        return 0;
    }

    
    public void bot(){
        
        minmax();
        plateau[botPlay.i][botPlay.j] = 2;
        buttons[botPlay.i][botPlay.j].setText("O");
        buttons[botPlay.i][botPlay.j].setForeground(new Color(255,0,0));

        lastPlay = botPlay;
        
        tour++;
        
        victoire = verifVictoire(lastPlay, 4);

        if(victoire){
            textfield.setText("vous avez perdu");
        }
        else{
            /* Si le plateau est plein : Fin du jeu */
            if(tour == ligne*colonne){
                textfield.setText("Match nul, bien joué !");
            }
            /* le tour passe à l'autre joueur */
            player1_turn = !player1_turn;
        }
    }

    /*** Ce qui se passe qu'on clique un button ***/
    @Override
    public void actionPerformed(ActionEvent e){
        int nb_l;
        int i; 
        int j;
        for(i = 0; i < ligne; i++){
            for(j = 0; j < colonne; j++){
                
                /* savoir quel button est cliqué */
                if(e.getSource()==buttons[i][j]) {

                    /* verifier si la case est vide */
                    if(buttons[i][j].getText()=="") {
                        nb_l = ligne - 1;
                           
                        /* verifier si la ligne d'avant est vide ou non */
                        while (buttons[nb_l][j].getText()!=""){
                            nb_l--;
                        }
                        
                        buttons[nb_l][j].setText("O");

                        if(player1_turn){
                            if(!bot){
                                textfield.setText("Tour du joueur 2");
                            }
                            buttons[nb_l][j].setForeground(new Color(255,253,0));
                            plateau[nb_l][j] = 1;
                        }
                        else if(!bot && !player1_turn){
                            textfield.setText("Tour du joueur 1");
                            buttons[nb_l][j].setForeground(new Color(253,0,0));
                            plateau[nb_l][j] = 2;
                        }

                        tour++;
                        lastPlay.i = nb_l;
                        lastPlay.j = j;
                        victoire = verifVictoire(lastPlay, 4);

                        if(victoire){
                            if(!bot){
                                if(plateau[nb_l][j] == 1){
                                    textfield.setText("Victoire des jaunes");
                                }
                                else{
                                    textfield.setText("Victoire des rouges");
                                }
                            }
                            else{
                                textfield.setText("Vous avez gagné");
                            }
                            bot = false;
                            break;
                        }
                        else{
                            /* Si le plateau est plein : Fin du jeu */
                            if(tour == ligne*colonne){
                                textfield.setText("Match nul, bien joué !");
                                bot = false;
                                break;
                            }
                            /* le tour passe à l'autre joueur */
                            player1_turn = !player1_turn;
                        }
                    }
                }	
            }
            if(victoire || tour == ligne*colonne)
                for (int k = 0; k < ligne; k++){
                    for (int l = 0; l < colonne; l++){
                        buttons[i][j].setEnabled(false);
                    }
                }
        }
        if(bot && !player1_turn){
            bot();
        }
    }


    public static void main(String[] args)
    {

		Object[] options = {"Seul contre l'ordinateur", "Joueur contre joueur"};
        Object[] options2 = {"Moi", "l'ordinateur"};
        Object[] options3 = {"1", "2"};
		int n = JOptionPane.showOptionDialog(new JFrame(), "Selectionnionner votre mode de jeux.", "Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
		Puissance4 p = null;
		if(n == 0){
            n = JOptionPane.showOptionDialog(new JFrame(), "Qui joue en premier ?", "Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options2,options2[0]);
            difficult = JOptionPane.showOptionDialog(new JFrame(), "difficulté","Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options3,options3[0]);
            
            if(n == 0){
                p = new Puissance4();
                p.bot = true;
            }
            else{
                p = new Puissance4();
                p.bot = true;
                p.bot();
                p.player1_turn = true;
            }
        }
        else {
            p = new Puissance4();
            
        }
    }
}