package vermaa.msoe.lab09.sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller2 {
    /**
     * All necessary FXML Components Below
     */
    @FXML
    private TextField text1;
    @FXML
    private TextField text2;
    @FXML
    private TextField text3;
    @FXML
    private TextField text4;
    @FXML
    private TextField text5;
    @FXML
    private TextField text6;
    @FXML
    private TextField text7;
    @FXML
    private TextField text8;
    @FXML
    private TextField text9;

    /**
     * Stage and Controller for primary stage stuffs
     */
    private Stage mainStage;
    private Controller mainController;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    /**
     * Applies a convolution to imageview in controller 1
     * @param actionEvent Is a button
     */
    public void apply(ActionEvent actionEvent) {
        double one=  Double.parseDouble(text1.getText());
        double two=  Double.parseDouble(text2.getText());
        double three=  Double.parseDouble(text3.getText());
        double four=  Double.parseDouble(text4.getText());
        double five=  Double.parseDouble(text5.getText());
        double six=  Double.parseDouble(text6.getText());
        double seven=  Double.parseDouble(text7.getText());
        double eight=  Double.parseDouble(text8.getText());
        double nine= Double.parseDouble(text9.getText());
        double sum=one+two+three+four+five+six+seven+eight+nine;
        double[] kernel = { one/sum,  two/sum,  three/sum,
                four/sum, five/sum, six/sum,
                seven/sum,  eight/sum,  nine/sum};;
        mainController.mainImage.setImage( ImageUtil.convolve(mainController.mainImage.getImage(), kernel));
    }

    /**
     * Sets kernel values to sharpening
     * @param actionEvent
     */
    public void sharpen(ActionEvent actionEvent) {
        text1.setText("0");
        text2.setText("-1");
        text3.setText("0");
        text4.setText("-1");
        text5.setText("5");
        text6.setText("-1");
        text7.setText("0");
        text8.setText("-1");
        text9.setText("0");
    }
    /**
     * Sets kernel values to blur
     * @param actionEvent
     */
    public void blur(ActionEvent actionEvent) {
        text1.setText("0");
        text2.setText("1");
        text3.setText("0");
        text4.setText("1");
        text5.setText("5");
        text6.setText("1");
        text7.setText("0");
        text8.setText("1");
        text9.setText("0");
    }
}
