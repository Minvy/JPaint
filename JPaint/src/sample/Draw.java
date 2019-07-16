package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Draw {
    GraphicsContext gc;

    double x1; double y1;
    boolean flag;

    public Draw(GraphicsContext gc){
        this.gc = gc;
        flag = true;
    }

    public void drawFill(double x, double y, int size, Color c){
        gc.setFill(c);
        gc.fillArc(x-2,y,size,size,180,360,ArcType.ROUND);
    }
    public void drawLine(double x, double y, int size, Color c){

        drawFill(x,y,size,c);

        if(flag){
            x1 = x;
            y1 = y;
        }else{
            gc.setStroke(c);
            gc.setLineWidth(size);
            gc.strokeLine(x1,y1,x,y);
        }
        flag = !flag;
    }

    public void reset(){
        gc.clearRect(0,0,1000,1000);
    }

    public GraphicsContext getGc() {
        return gc;
    }
}
