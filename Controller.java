package vermaa.msoe.lab09.sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.nio.file.Path;

import static javafx.scene.paint.Color.color;


public class Controller {
    private Stage otherStage;
    private Controller2 otherController;

    @FXML
    private Pane mainGrid;

    private Path currentPath;
    @FXML
    private Label colorBar;
    @FXML
    public ImageView mainImage;

    /**
     * Takes in action event from GUI and executes the load image functionality
     *
     * @param actionEvent
     */
    public void loadImage(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.msoe", "*.bmsoe");
        chooser.getExtensionFilters().addAll(filter);
        java.nio.file.Path path = chooser.showOpenDialog(null).toPath();
        mainImage.setImage(ImageIO.read(path));
        currentPath = path;
        System.out.println("load button has been clicked and functioned");
    }

    /**
     * Takes in action event from GUI and executes the save image functionality
     *
     * @param actionEvent
     */

    public void saveImage(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.msoe", "*.bmsoe");
        chooser.getExtensionFilters().addAll(filter);
        Path path = chooser.showSaveDialog(null).toPath();
        ImageIO.write(path, mainImage.getImage());
        System.out.println("save button has been clicked and functioned");

    }

    /**
     * Takes in action event from GUI and executes the negate image functionality
     *
     * @param actionEvent
     */
    public void negateImage(ActionEvent actionEvent) {
        WritableImage image = new WritableImage((int) mainImage.getImage().getWidth(), (int) mainImage.getImage().getHeight());
        PixelWriter pixelWriter = image.getPixelWriter();
        PixelReader pixelReader = mainImage.getImage().getPixelReader();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixelWriter.setColor(x, y, pixelReader.getColor(x, y).invert());
            }
        }
        mainImage.setImage(image);
        System.out.println("negate button works");

    }

    /**
     * Takes in action event from GUI and executes the greysale image functionality
     *
     * @param actionEvent
     */
    public void greyImage(ActionEvent actionEvent) {
        mainImage.setImage(transformImage(mainImage.getImage(), ((y, color) -> color.grayscale())));
        System.out.println("grey button works");
    }
    /**
     * Takes in action event from GUI and executes the redsale image functionality
     *
     * @param actionEvent
     */
    public void reddenImage(ActionEvent actionEvent) {
        mainImage.setImage(transformImage(mainImage.getImage(), ((y, color) -> color( color.getRed(), 0, 0))));
        System.out.println("red button works");
    }
    /**
     * Takes in action event from GUI and executes the red-greysale image functionality
     *
     * @param actionEvent
     */
    public void redGreyImage(ActionEvent actionEvent) {
        mainImage.setImage(transformImage(mainImage.getImage(), ((y, color) -> {
            if(y%2==0){
               return color( color.getRed(), 0, 0);
            }
        else{
            return color.grayscale();
        }}
        )));
        System.out.println("red grey button works");
    }



    /**
     * Takes in action event from GUI and executes the reload image functionality
     *
     * @param actionEvent
     */
    public void reloadImage(ActionEvent actionEvent) {
        mainImage.setImage(ImageIO.read(currentPath));
        System.out.println("reload button works");

    }
    private static Image transformImage(Image image, Transformable transform) {
        WritableImage output = new WritableImage( (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = output.getPixelWriter();
        PixelReader pixelReader = image.getPixelReader();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixelWriter.setColor(x, y, transform.apply(y, pixelReader.getColor(x, y)));
            }
        }
        return output;
    }

    public void setOtherStage(Stage otherStage) {
        this.otherStage = otherStage;
    }

    public void setOtherController(Controller2 otherController) {
        this.otherController = otherController;
    }

    /**
     * Toggles the visibility of the second window.
     *
     * The second window is placed immediately below this window
     * @param actionEvent <b>Are you happy now?</b>
     */
    public void showOther(ActionEvent actionEvent) {
        if (otherStage.isShowing())
            otherStage.hide();

        else {
            Stage myStage = (Stage)mainGrid.getScene().getWindow();
            otherStage.setX(myStage.getX());
            otherStage.setY(myStage.getY() + myStage.getHeight());
            otherStage.setWidth(myStage.getWidth());
            otherStage.show();
        }
    }

    public void setMainGrid(Pane mainGrid) {
        this.mainGrid = mainGrid;
    }

    public void aboutDisplay(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("About this Program");
        alert.setContentText(" This program was made by Amish Verma in CS 1021 with Dr. Berisha. It transforms images using filters.");
        alert.showAndWait();

    }

    public void setColorBar(MouseEvent m) {
        try {
            PixelReader reader = mainImage.getImage().getPixelReader();
            Color color = reader.getColor((int) m.getX(), (int) m.getY());
            System.out.println(color);
            colorBar.setText(color.toString());
        }catch (NullPointerException e){

        }
    }
}
