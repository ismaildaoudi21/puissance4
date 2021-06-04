package packp4;

public class Case {
    public int i;
    public int j;

    public Case(int x, int y){
        this.i = x;
        this.j = y;
    }

    @Override 
    public boolean equals(Object obj){ 
        if(this == obj){ 
            return true; 
        }

        if(obj == null){ 
            return false; 
        }

        if(!(obj instanceof Case)){ 
            return false; 
        }

        Case other = (Case) obj; 

        if(this.i == other.i){
            if(this.j == other.j){
                return true;
            }
        }

        return false;
    }
} 
