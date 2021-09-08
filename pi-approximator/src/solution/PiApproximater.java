package solution;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PiApproximater extends Application {
    private static boolean running = false;
    public static final Object lock = new Object();
    private static Label actualValueLabel;
    private static Label estimatedValueLabel;
    private static Label numberOfTrialsLabel;
    private static Button runAndPauseButton;


    public void start(Stage primaryStage){
        VBox root = new VBox();
        root.setPrefWidth(400);
        root.setPrefHeight(130);
        root.setSpacing(3);
        root.setAlignment(Pos.BASELINE_CENTER);
        root.setStyle("-fx-background-color: black; "
                + "-fx-border-color: black; -fx-border-width:3");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        actualValueLabel = new Label("First 15 digits of Pi:         3.14159265358979");
        actualValueLabel.setPrefWidth(root.getPrefWidth()-6);
        actualValueLabel.setStyle(" -fx-background-color: rgb(130,130,130); -fx-text-fill: black;-fx-font-weight:bold; -fx-font-size:18px");
        estimatedValueLabel = new Label("Estimated value of Pi:           ");
        estimatedValueLabel.setStyle(" -fx-background-color: rgb(130,130,130); -fx-text-fill: black;-fx-font-weight:bold; -fx-font-size:18px");
        estimatedValueLabel.setPrefWidth(root.getPrefWidth()-6);
        numberOfTrialsLabel = new Label("Number of trials:         ");
        numberOfTrialsLabel.setStyle(" -fx-background-color: rgb(130,130,130); -fx-text-fill: black;-fx-font-weight:bold; -fx-font-size:18px");
        numberOfTrialsLabel.setPrefWidth(root.getPrefWidth()-6);

        runAndPauseButton = new Button("Run");

        root.getChildren().addAll(actualValueLabel, estimatedValueLabel, numberOfTrialsLabel, runAndPauseButton);
        ApproximatorThread approximator = new ApproximatorThread();
        approximator.setDaemon(true);
        approximator.start();

        runAndPauseButton.setOnAction((e)->{
            doRunAndPause();
        });


        primaryStage.show();
    }

    public void doRunAndPause(){
        if(running){
            runAndPauseButton.setText("Run");
            running = false;
        }
        else{
            synchronized (lock){
                lock.notify();
            }
            runAndPauseButton.setText("Pause");
            running = true;
        }
    }

    private static class ApproximatorThread extends Thread{
        long trialCount = 0;
        final int BATCH_SIZE = 1000000;
        long pointsInCircle = 0;
        double estimatedValue;

        public void doWait(){
            synchronized (lock){
                try{
                    lock.wait();
                }catch (InterruptedException e){
                }
            }
        }

        public void run() {
            while(true){
                if(!running){
                    doWait();
                }
                for (int i = 0; i < BATCH_SIZE; i++) {
                    double xCoord = Math.random();
                    double yCoord = Math.random();
                    trialCount++;
                    if (xCoord*xCoord + yCoord*yCoord < 1)
                        pointsInCircle++;
                }
                estimatedValue = 4 * ((double)pointsInCircle / trialCount);
                Platform.runLater( () -> {
                    numberOfTrialsLabel.setText( " Number of Trials:           " + trialCount);
                    estimatedValueLabel.setText( " Current Estimate:           " + estimatedValue);
                } );

            }
        }
    }





    public static void main(String[] args){
        launch(args);
    }




}
