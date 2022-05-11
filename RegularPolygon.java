package Hex;

public class RegularPolygon {

    private int[] xValues, yValues;

    int inscribedRadius;
    int circumscribedRadius;

    int sideCount;

    RegularPolygon(int circumscribedRadius, int sideCount, double initangle) {

        this.circumscribedRadius = circumscribedRadius;
        inscribedRadius = (int) Math.round(Math.cos(Math.PI/sideCount)*circumscribedRadius);

        this.sideCount = sideCount;

        xValues = new int[sideCount];
        yValues = new int[sideCount];
        for(int k = 0;k<sideCount;k++){
            double angle = ((double)k/sideCount)*2.0*Math.PI + (Math.PI*initangle/180.0);
            xValues[k] = (int) Math.round(circumscribedRadius * Math.cos(angle));
            yValues[k] = (int) Math.round(circumscribedRadius * Math.sin(angle));
        }
    }

    public int[] getXs(int x){
        //Retourne toutes les coordonnées x du polygone déplacées par 'x'
        int[] out = new int[xValues.length];
        for(int k = 0;k<xValues.length;k++){
            out[k] = xValues[k] + x;
        }
        return out;
    }
    
    public int[] getYs(int y){
        //Retourne toutes les coordonnées y du polygone déplacées par 'y'
        int[] out = new int[yValues.length];
        for(int k = 0;k<yValues.length;k++){
            out[k] = yValues[k] + y;
        }
        return out;
    }
}

