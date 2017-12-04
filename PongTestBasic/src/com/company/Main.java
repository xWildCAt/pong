package com.company;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {


    private int score1 = 0;
    private int score2 = 0;
    private boolean gameStarted;

    private static final int width = 800;
    private static final int height = 600;

    private static final double ballRadius = 15;
    private static final int playerHeight = 150;
    private static final int playerWidth = 15;

    private int ballSpeedY = 1;
    private int ballSpeedX = 1;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;

    private double playerOneY = height / 2;
    private double playerTwoY = height / 2;



    public void start(Stage stage) throws Exception {

        Canvas canvas = new Canvas(width, height); //for drawing
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D(); //Each call pushes the necessary parameters onto the buffer where they will be later rendered onto the image of the Canvas node by the rendering thread at the end of a pulse.
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {//event handler
                Main.this.run(graphicsContext);
            }
        })); //processes individual KeyFrame sequentially

        timeline.setCycleCount(Timeline.INDEFINITE); // runs repeatedly until the stop() method is explicitly called. stopo nera, tai tiesiog be galo :P

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                playerOneY = e.getY()-playerHeight/2;
            }
        }); //player'io control
        canvas.setOnMouseClicked(e ->  gameStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();


        timeline.play(); // paledziam laika

    }

    private void run(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, width, height);

        graphicsContext.setFill(Color.RED);
        graphicsContext.setFont(Font.font(30));


        if(gameStarted) {
            ballXPos+= ballSpeedX;// taip ir judam. kiekviena frame vis pridedam "greiti" prie "vietos"
            ballYPos+= ballSpeedY;
            graphicsContext.fillOval(ballXPos, ballYPos, ballRadius, ballRadius); // piesiam kamuoli


            playerTwoY = ballYPos; // paprastas variantas, bet uz ekrano ribu labai iseina. ir stengiasi atmust visada su virsutiniu kampu. not good.
            //TODO: parasyti grazesni varianta...


        } else {//pradinis screen, arba kai scorina kazkas
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.strokeText("Right Click To Play", 400, 300);
            // per vidury nustatom
            ballXPos = 400;
            ballYPos = 300;

            //TODO: Padaryt, kad i random puse pradetu judet is pradziu
            ballSpeedX = -1;
            ballSpeedY = 1;
        }
        if(ballYPos > height || ballYPos < 0) ballSpeedY *=-1; // nuo sienu atmusinejam
        //valdom playerio X pos
        double playerOneXPos = 0;
        if(ballXPos < playerOneXPos - playerWidth) { // score!!
            score2++;
            gameStarted = false;
        }
        double playerTwoXPos = width - playerWidth; // nuo "sienos" atstumiam, nes kitaip uz screen ribu
        if(ballXPos > playerTwoXPos + playerWidth) {
            score1++; // yaaaay!
            gameStarted = false;
        }
        //basically, cia mes tikrinam, kad normaliai atsimusinetu.... Lyg tai nebugintas variantas...
        if( ((ballXPos + ballRadius > playerTwoXPos) && ballYPos >= playerTwoY && ballYPos <= playerTwoY + playerHeight) ||
                ((ballXPos < playerOneXPos+ + playerWidth) && ballYPos >= playerOneY && ballYPos <= playerOneY + playerHeight)) {
            ballSpeedY += 1 * Math.signum(ballSpeedY); //Returns the signum function of the argument; zero if the argument is zero, 1.0 if the argument is greater than zero, -1.0 if the argument is less than zero.
            ballSpeedX += 1 * Math.signum(ballSpeedX); // Nice, super patogu signum :D realiai bendras, kad neskaiciuoti, nuo ko atsimusa ir automatiskai apversti jurejimo krypti.
            ballSpeedX *= -1;// revers i kita puse
            ballSpeedY *= -1; // same
        }
        graphicsContext.fillText(score1 + "\t\t\t\t\t\t\t\t" + score2, width / 2, 100);
        graphicsContext.fillRect(playerTwoXPos, playerTwoY, playerWidth, playerHeight);
        graphicsContext.fillRect(playerOneXPos, playerOneY, playerWidth, playerHeight);
    }

}