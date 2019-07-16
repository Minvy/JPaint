package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class Controller {
    public Canvas canvasID;
    public Draw draw;

    public BorderPane borderPane;

    public ColorPicker colorPickerID;

    public TextField numFieldID;

    public Button addBtn;
    public Button subBtn;

    public ToggleGroup tgl;
    public RadioButton fillRadio;
    public RadioButton lineRadio;

    Stack<Image> canvasImageStack;

    public void setup(){
        numFieldID.setDisable(true);

        draw = new Draw(canvasID.getGraphicsContext2D());

        tgl = new ToggleGroup();
        fillRadio.setToggleGroup(tgl);
        lineRadio.setToggleGroup(tgl);
        fillRadio.setSelected(true);

        canvasImageStack = new Stack<>();
        canvasImageStack.push(canvasID.snapshot(null,null));

        //Event handlers
        canvasID.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if(fillRadio.isSelected()){
                int size = Integer.parseInt(numFieldID.getText());
                Color color = colorPickerID.getValue();

                draw.drawFill(e.getX(),e.getY(),size, color);
            }
        });
        canvasID.addEventHandler(MouseEvent.MOUSE_RELEASED, e ->{
            canvasImageStack.add(canvasID.snapshot(null,null));
            if(lineRadio.isSelected()){
                int size = Integer.parseInt(numFieldID.getText());
                Color color = colorPickerID.getValue();

                draw.drawLine(e.getX(), e.getY(), size, color);
            }
        });
        addBtn.setOnAction(e->{
            int size = Integer.parseInt(numFieldID.getText());
            numFieldID.setText(size+1+"");
        });
        subBtn.setOnAction(e->{
            int size = Integer.parseInt(numFieldID.getText());
            numFieldID.setText(size-1+"");
        });
    }

    public void reset(){
        draw.reset();
    }

    public void toFile(){
        WritableImage writableImage = canvasID.snapshot(null,null);
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", ".png"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG", ".jpeg"));
        File out = fc.showSaveDialog(borderPane.getScene().getWindow());
        if(out != null){
            try{
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null),"png", out);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void undo(){

        if(canvasImageStack.size() < 2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error end of stack");
            alert.setContentText("There are no more saved states of this drawing");
            alert.setHeaderText("Unable to undo");
            alert.showAndWait();
        }else{
            canvasImageStack.pop();
            draw.getGc().drawImage(canvasImageStack.peek(),0,0);
        }
    }
    public void open(){
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(borderPane.getScene().getWindow());
        if(file != null){
            try{
                Image image = SwingFXUtils.toFXImage(ImageIO.read(file), null);
                draw.getGc().drawImage(image,0,0);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void close(){
        Stage window = (Stage) borderPane.getScene().getWindow();
        window.close();
    }
}
