import packp4.*;

import javax.swing.*;
import java.awt.event.*;
import java.net.CookiePolicy;
import java.awt.*;
import java.util.*;
import java.util.List;

class Puissance4 implements ActionListener 
{
    // La taille du plateau : colonnes et lignes
    int colonne = 7;
    int ligne = 6;


	Random random = new Random();
	JFrame frame = new JFrame();
	JPanel title_panel = new JPanel();
	JPanel button_panel = new JPanel();
	JLabel textfield = new JLabel();
    JButton[][] buttons = new JButton[ligne][colonne];

    boolean fin_de_partie = false;

    ImageIcon vide = new ImageIcon("images/vide.jpg");

    /* hashmap qui stock les score de situations pour l'algorithme minimax */
    Map<String, Integer> table = new HashMap<>();

    /* Représentation du plateau */
    int[][] plateau = new int[ligne][colonne];

    /* booléen qui indique le joueur en train de jouer */
    boolean player1_turn = true;

    /* booléen qui indique s'il y a une victoire */
    boolean victoire;

    /*booléen qui indique si on joue avec l'ordi ou un humain*/
    boolean bot;

    int tour = 0; // compteur de tour de jeu 

    
    Case lastPlay = new Case(0, 0); //derniere case jouée

    Case botPlay = new Case(5, 5); //case que le bot joue

    List<Case> win = new ArrayList<>(); //liste des cases dans un alignement gagnant

    static int difficult; //profondeurde recherche du bot

    Puissance4() {
        
        /*** Intérface graphique ***/

        /* c'est pour arrêter le programme si on exit depuis l'interface graphique */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*définir la taille de l'interface */
        frame.getContentPane().setBackground(new Color(50,50,50));
        frame.setLayout(new BorderLayout());
        frame.setSize(800,800);
        frame.setResizable(false);


        textfield.setBackground(new Color(60, 60, 60));
        textfield.setForeground(new Color(250, 250, 240));
        textfield.setFont(new Font("Arial",Font.BOLD,75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Puissance 4");
        textfield.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0,0,800,100);

        /* la grille contient 6 lignes et 7 colonnes et un espace de 20  entre les buttons */
        button_panel.setLayout(new GridLayout(6, 7));
        button_panel.setVisible(true);

        JButton imageButton = new JButton();

        button_panel.setBackground(new Color(0,50,225));
        /* définir le style de chaque button puis l'ajouter dans la grille*/
        for(int i = 0; i < 6; i++){
            for(int j = 0; j <7; j++){

                /* creer un nouveau button*/
                imageButton = new JButton(new ImageIcon("images/vide.jpg"));
                buttons[i][j] = imageButton;
                buttons[i][j].setContentAreaFilled(false);
                buttons[i][j].setOpaque(false);
                buttons[i][j].setFocusable(false);
                //buttons[i][j].setBorderPainted(false);
                buttons[i][j].addActionListener(this);
                /* l'ajouter dans la grille */
                button_panel.add(buttons[i][j]);

            }
        }

        /* Ajouter la grille dans l'interface graphique */
        title_panel.add(textfield);
        frame.add(title_panel,BorderLayout.NORTH);
        frame.add(button_panel);
        frame.setVisible(true);
    }


    /**
     * Fonction qui renvoie la chaine de caractères représentant
     * la situation d'un plateau. 
     * @param tab le plateau duquel on veut une representation.
     * @return str la chaine de caracteres qui represente le plateau.
     */
    public String toString(int[][] tab){
        String str = "[";
        for(int i = 0; i < ligne; i++){
            for(int j = 0; j < colonne; j++){
                if(tab[i][j] == 0){
                    str = str + "O";
                }
                else if(tab[i][j] == 1){
                    str = str + "R";
                }
                else if(tab[i][j] == 2){
                    str = str + "J";
                }
            }
        }
        str = str + "]";
        return str;
    }

    /**
     * Fonction qui la longueur de l'alignement de jetons maximum
     * fait a partir du jeton la case last dans le plateau tab, 
     * place les cases qui font partie de cet alignement si verifVictoire
     * est true.
     * @param tab le plateau sur lequel on fait la verification.
     * @param last la case depuis laquelle on effectue la verification.
     * @param verifVictoire le booleen qui indique si on verifie une victoire.
     * @return alignt l'alignement maximal rencontré.
     */
    public int verif(int[][] tab, Case last, boolean verifVictoire){
            
            int joueur = tab[last.i][last.j]; // Dernier coup joué
            int alignt = 0; // Nombre d'alignement max
            int l = last.i;
            int c = last.j;
            int x = l;
            int y = c;
            int compteur = -1;
            List<Case> alignCases = new ArrayList<>();
            //afficher();

            /* Test des diagonales : */
            // Haut-Gauche vers Bas-Droite d'abord...
            while(x >= 0 && y >= 0 && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                x--; y--; compteur++;
            }
            x = l; y = c;
            while(x < ligne && y < colonne && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                x++; y++; compteur++;
            }
            if(compteur > alignt)
                alignt = compteur;

            if(verifVictoire){
                if(compteur >= 4){
                    win.addAll(alignCases);
                }
            }

            alignCases.clear();

            // ... puis Bas-Gauche vers Haut-Droite

            x = l; y = c; compteur = -1;
            while(x < ligne && y >= 0 && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                x++; y--; compteur++;               
            }
            x = l; y = c;
            while(x >= 0 && y < colonne && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                x--; y++; compteur++;               
            }
            if(compteur > alignt)
                alignt = compteur;

            if(verifVictoire){
                if(compteur >= 4){
                    win.addAll(alignCases);
                }
            }
            
            alignCases.clear();

            /* Test de l'horizontale */

            x = l; y = c; compteur = -1;
            while( y>= 0 && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                y--; compteur++;
            }
            y = c;
            while(y < colonne && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                y++; compteur++;
            }
            if(compteur > alignt)
                alignt = compteur;

            if(verifVictoire){
                if(compteur >= 4){
                    win.addAll(alignCases);
                }
            }

            alignCases.clear();

            /* Test de la verticale */

            x = l; y = c; compteur = -1;
            while(x >= 0 && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                x--; compteur++;           
            }
            x = l;
            while(x < ligne && tab[x][y] == joueur){
                if(verifVictoire){
                    Case coup = new Case(x, y);
                    alignCases.add(coup);
                }
                x++; compteur++;
            }
            if(compteur > alignt)
                alignt = compteur;

            if(verifVictoire){
                if(compteur >= 4){
                    win.addAll(alignCases);
                }
            }

            /* Alors ? Un vainqueur ? */

            return alignt;
    }
    

    /**
     * Fonction qui place un jeton dans une case.
     * @param coup la case dans laquelle on place le jeton.
     * @param num la couleur du jeton representée par l'entier 1 ou 2.
     */
    public void placer(Case coup, int num){
        if(num == 1){
            buttons[coup.i][coup.j].setIcon(new ImageIcon("images/jaune.jpg"));
        }
        else{
            buttons[coup.i][coup.j].setIcon(new ImageIcon("images/rouge.jpg"));
        }
        plateau[coup.i][coup.j] = num;
    }

    /**
     * Fonction qui affiche un plateau.
     * @param tab le plateau a afficher.
     */
    public void afficher(int[][] tab) {
        for(int i = 0; i < ligne; i++){
            for(int j = 0; j < colonne; j++){ 
                System.out.print(tab[i][j]);
            }
            System.out.print("\n");
        }

        System.out.println("lastplay :" + lastPlay.i + " " + lastPlay.j);
    }

    /**
     * Fonction renvoie la liste des jetons jouables.
     * @param tab le plateau duquel on veut la liste des jetons jouables.
     * @return casesDspo la liste des cases jouables.
     */
    List<Case> casesDisponibles(int[][] tab){
        int nb_l;
        List<Case> casesDispo = new ArrayList<>();
        for(int i = 0; i < colonne; i++){
            if(tab[0][i] == 0){
                nb_l = ligne - 1;
                /* verifier si la ligne d'avant est vide ou non */
                while (tab[nb_l][i] != 0){
                    nb_l--;
                }
                casesDispo.add(new Case(nb_l, i));
            }
        }
        return casesDispo;
    }

    /**
     * Fonction qui determine si une Case est comprise dans une liste de cases.
     * @param coups une liste de cases.
     * @param q la case dont on veut verifier la presence.
     * @return un booleen : true si la liste contient la case q false sinon
     */
    public boolean contient(List<Case> coups, Case q){
        for(int i = 0; i < coups.size(); i++){
            if(coups.get(i).equals(q)){ 
                return true;
            }
        }
        return false;
    }


    /**
     * Fonction qui pour chaque case jouable du plateau place un jeton jaune 
     * dans cette case appelle la fonction min() puis retire ce jeton, la case ou etait
     * le jetons qui engendrait le plus grand score.
     * @param depth la profondeur souhaitée
     * @return un entier correspondant au score minimum des appels de max.
     */
    public Case minmax(int depth){
        System.out.println(depth);
        List<Case> coupsPossibles = casesDisponibles(plateau);

        int max = Integer.MIN_VALUE;

        Case play = new Case(0, 0);

        for(int i = 0; i < coupsPossibles.size(); i++){
            Case c = coupsPossibles.get(i);
            plateau[c.i][c.j] = 2;
            if(verif(plateau, c, false) >= 4){
                plateau[c.i][c.j] = 0;
                play = c;
                break;
            }
            int m = min(Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);
            if(m > max){ 
                max = m;
                play = c;
            }
            plateau[c.i][c.j] = 0;
        }

        return play;
    }

    /**
     * Fonction qui pour chaque case jouable du plateau place un l'entier 2 
     * dans cette case appelle la fonction min() puis retire cet entier,
     * elle renvoie le maximum des retours des fonctions min(). Si on est 
     * a la profondeur maximale elle renvoie le score du plateau actuel.
     * @param alpha la meilleure option pour le max.
     * @param beta la meilleure option pour le min.
     * @param depth la profondeur suivante.
     * @return un entier correspondant au score max des appels de min().
     */
    public int max(int alpha, int beta, int depth){
        String str = toString(plateau); // représentation en chaine de caractere du plateau actuel
        if(table.containsKey(str))
        {
            return table.get(str);
        }

        if(depth == 0){
            int score = score();
            return score;
        }    

        int v = Integer.MIN_VALUE; //valeur max;
        
        List<Case> coupsPossibles = casesDisponibles(plateau);

        for(int i = 0; i < coupsPossibles.size(); i++){
            Case c = coupsPossibles.get(i);
            plateau[c.i][c.j] = 2;

            //si le coup est gagnant on arrete la reherche
            if(verif(plateau, c, false) >= 4){
                plateau[c.i][c.j] = 0;
                v = 100000;
                break;
            }
            int v2 = min(alpha, beta, depth - 1); // max du score de toute les situations suivantes
            plateau[c.i][c.j] = 0;
            //elagage
            if(v2 > v)
                v = v2;
            if(v2 >= beta){ 
                table.put(str, v2);
                return v2;
            }
            if(v2 > alpha)
                alpha = v2;
        }

        table.put(str, v);
        return v;
    }


    /**
     * Fonction qui pour chaque case jouable du plateau place l'entier 1 
     * dans cette case appelle la fonction max() puis retire cet entier,
     * elle renvoie le minimum des retours des fonctions max(). Si on est 
     * a la profondeur maximale elle renvoie le score du plateau actuel.
     * @param alpha la meilleure option pour le max.
     * @param beta la meilleure option pour le min.
     * @param depth la profondeur suivante.
     * @return un entier correspondant au score min des appels de max().
     */
    public int min(int alpha, int beta, int depth){
        String str = toString(plateau); // représentation en chaine de caractere du plateau actuel
        
        if(table.containsKey(str))
        {
            return table.get(str);
        }

        if(depth == 0){
            int score = score();
            return score;
        }

        int v = Integer.MAX_VALUE; //valeur min
        
        List<Case> coupsPossibles = casesDisponibles(plateau);

        for(int i = 0; i < coupsPossibles.size(); i++){
            Case c = coupsPossibles.get(i);
            plateau[c.i][c.j] = 1;

            //si le coup est perdant on arrete la reherche
            if(verif(plateau, c, false) >= 4){
                plateau[c.i][c.j] = 0;
                v = -100000;
                break;
            }

            int v2 = max(alpha, beta, depth - 1); // min du score de toute les situations suivantes
            plateau[c.i][c.j] = 0;
            //elagage
            if(v2 < v)
                v = v2;
            if(v2 <= alpha){ 
                table.put(str, v2);
                return v2;
            }
            if(v2 < beta)
                beta = v2;
        }

        table.put(str, v);
        return v;
    }


    /**
     * Fonction renvoie le score d'un plateau.
     * @return le score du plateau.
     */
    public int score(){
        int score = 0;
        for(int i = 0; i < ligne; i++){
            for(int j = 0; j < colonne; j++){
                /* pour chaque case vide du plateau on fait varier le score 
                 * en fonction des alignements potentiels qu'un joueur pourrait faire
                 * a partir de cette case
                 */
                if(plateau[i][j] == 0){
                    Case c = new Case(i, j);
                    plateau[i][j] = 2; 
                    if(verif(plateau, c, false) >= 4){
                        score += 50;
                    }
                    else if(verif(plateau, c, false) >= 3){
                        score += 10;
                    }
                    else if(verif(plateau, c, false) >= 2){
                        score += 2;
                    }
                    plateau[i][j] = 1;
                    if(verif(plateau, c, false) >= 4){
                        score -= 50;
                    }
                    else if(verif(plateau, c, false) >= 3){
                        score -= 10;
                    }
                    else if(verif(plateau, c, false) >= 2){
                        score -= 2;
                    }
                    plateau[i][j] = 0;
                }
            }
        }

        return score;
    }


    /**
     * Fonction qui vas placer un coup determiné par la fonction minmax
     */
    public void bot(){

        botPlay = minmax(difficult); // initialisation du coup du bot
        table.clear(); // vidage de la hashMap

        placer(botPlay, 2); // placement du coup
 
        lastPlay = botPlay; 
        
        tour++;
        
        //verification de victoire
        if(verif(plateau, lastPlay, true) >= 4){
            victoire = true;
        }

        if(victoire){
            textfield.setText("vous avez perdu");
            textfield.setBackground(new Color(240, 0, 0));
            textfield.setForeground(new Color(250, 250, 240));
        }
        else{
            /* Si le plateau est plein : Fin du jeu */
            if(tour == ligne*colonne){
                textfield.setText("Match nul");
            }
            /* le tour passe à l'autre joueur */
            player1_turn = !player1_turn;
        }
    }

    /**
     *
     * Lorsque on appuie sur un button dans la grille d'abord on verifie que on peut mettre
     * le jeton dans la colonne (i.e on verifie que la ligne d'avant est-ce qu'elle est vide ou
     * non pour mettre le jeton au plus bas possible si la case est vide), après à chaque tour on
     * verifie si il y a un alignement de 4 jetons avec la fonction verif
     * @param e l'évenement ici c'est le clique sur le button
     */
    @Override
    public void actionPerformed(ActionEvent e){
        int nb_l;
        int i; 
        int j;

        if(!victoire){
            for(i = 0; i < ligne; i++){
                for(j = 0; j < colonne; j++){
                
                    /* savoir quel button est cliqué */
                    if(e.getSource()==buttons[i][j]) {

                        /* verifier si la case est vide */
                            if(plateau[0][j] == 0) {
                                nb_l = ligne - 1;
                           
                                /* verifier si la ligne d'avant est vide ou non */
                                while (plateau[nb_l][j] != 0){
                                    nb_l--;
                            }
                        
                            Case coup = new Case(nb_l, j);
                        
                            if(player1_turn){
                                if(!bot){
                                    textfield.setText("Tour du joueur 2");
                                }
                                placer(coup, 1);
                            }
                            else if(!bot && !player1_turn){
                                textfield.setText("Tour du joueur 1");
                                placer(coup, 2);
                            }

                            tour++;
                            lastPlay.i = nb_l;
                            lastPlay.j = j;

                            if(verif(plateau, lastPlay, true) >= 4){
                                victoire = true;
                            }

                            if(victoire){
                                if(plateau[nb_l][j] == 1){
                                    if(!bot){
                                        textfield.setText("victoire jaune");
                                        textfield.setBackground(new Color(250, 255, 0));
                                        textfield.setForeground(new Color(88, 88, 88));}
                                    else{
                                        textfield.setText("vous avez gagné");
                                        textfield.setBackground(new Color(0, 230, 20));
                                        textfield.setForeground(new Color(250, 250, 240));
                                    }
                                }else{
                                    textfield.setText("victoire rouge");
                                    textfield.setBackground(new Color(255, 0, 0));
                                }
                            }else{
                                if(tour == ligne*colonne){
                                    textfield.setText("match nul");
                                }
                                player1_turn = !player1_turn;

                            }
                        
                            break;
                        }
                    }	
                } 
            }
        }
        if(victoire || tour == ligne*colonne){
            fin_de_partie = true;
            for (int k = 0; k < ligne; k++){
                for (int l = 0; l < colonne; l++){
                    Case q = new Case(k, l);
                    if(!(contient(win, q))){
                        buttons[k][l].setIcon(null);
                        buttons[k][l].setEnabled(false);
                    }
                }
            }
            finDePartie();
        }
        if(bot && !player1_turn){
            bot();
        }
    }

    /**
     * La methode finDePartie il affiche un message avec 2 option (rejouer et quitter).
     */
    public void finDePartie(){

        Object[] options1 = {"Rejouer", "quitter"};
        if (fin_de_partie == true){

            int n = JOptionPane.showOptionDialog(new JFrame(), "Fin de partie", "Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options1,options1[0]);
            if (n == 0) {
                Object[] options = {"Seul contre l'ordinateur", "Joueur contre joueur"};
                Object[] options3 = {"1", "2", "3"};
                n = JOptionPane.showOptionDialog(new JFrame(), "Selectionnionner votre mode de jeux.", "Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                Puissance4 p = null;
                if(n == 0){
                    n = JOptionPane.showOptionDialog(new JFrame(), "difficulté","Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options3,options3[0]);
                    if(n == 0)
                        difficult = 1;
                    else if(n == 1)
                        difficult = 2;
                    else if(n == 2)
                        difficult = 6;
                    p = new Puissance4();
                    p.bot = true;
                }
                else {
                    p = new Puissance4();
                }
            }
            else {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }

    }



    public static void main(String[] args)
    {
		Object[] options = {"Seul contre l'ordinateur", "Joueur contre joueur"};
        Object[] options3 = {"1", "2", "3"};
		int n = JOptionPane.showOptionDialog(new JFrame(), "Selectionnionner votre mode de jeux.", "Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
		Puissance4 p = null;
		if(n == 0){
            n = JOptionPane.showOptionDialog(new JFrame(), "difficulté","Bienvenue sur Puissance 4", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options3,options3[0]);
            if(n == 0)
                difficult = 1;
            else if(n == 1)
                difficult = 2;
            else if(n == 2)
                difficult = 6;
            p = new Puissance4();
            p.bot = true;
        }
        else {
            p = new Puissance4();
        }
    }
}