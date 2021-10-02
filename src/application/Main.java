package application;
	
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Main extends Application {
	
	static int speed = 5;
	static int width = 20;
	static int height = 20;
	static int foodcolor = 0;
	static int foodX = 0;
	static int foodY = 0;
	static int size = 25;
	static List<Corner> snake = new ArrayList<>();
	static Dir direction = Dir.right;
	static boolean GameOver = false;
	static Random rand = new Random();
	
	
	public enum Dir{
		right,left,up,down;
	}
	
	public static class Corner{
		int x,y;
		
		public Corner(int x,int y) {
			this.x = x;
			this.y = y;
		}
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			
			newfood();
			VBox root = new VBox();
			Canvas can = new Canvas(width*size, height*size);
			GraphicsContext gc = can.getGraphicsContext2D();
			root.getChildren().add(can);
			
			
			new AnimationTimer() {
				long lasttick = 0;
				@Override
				public void handle(long now) {
					// TODO Auto-generated method stub
					if(lasttick == 0) {
						lasttick=now;
						tick(gc);
						return;
					}
					if((now - lasttick) > (1000000000/speed)) {
						lasttick = now;
						tick(gc);
					}
				}
			}.start();
			
			
			
			
			Scene scene = new Scene(root,width*size,height*size);
			
			scene.addEventFilter(KeyEvent.KEY_PRESSED, key->{
				
				if(key.getCode() == KeyCode.Z) {
					direction = Dir.up;
				}
				if(key.getCode() == KeyCode.D) {
					direction = Dir.right;
				}
				if(key.getCode() == KeyCode.S) {
					direction = Dir.down;
				}
				if(key.getCode() == KeyCode.Q) {
					direction = Dir.left;
				}
			});
			
			scene.setOnKeyPressed((EventHandler<? super KeyEvent>) new EventHandler<KeyEvent>() {
				
				public void handle(KeyEvent evt) {
					
					 if (evt.getCode().equals(KeyCode.ENTER)) {
						// addRestartButton();
						 reset();
			            }

				}
			
			});
			
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			snake.add(new Corner(width/2,height/2));
			snake.add(new Corner(width/2,height/2));
			snake.add(new Corner(width/2,height/2));
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Game Snake");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void tick(GraphicsContext gc) {
		
		if(GameOver) {
			
			gc.setFill(Color.RED);
			gc.setFont(new Font("",30));
			gc.fillText("GAME OVER", 150, 250);
			
			return ;
			
		}
		
		for(int i=snake.size()-1; i>=1; i--) {
			snake.get(i).x = snake.get(i-1).x;
			snake.get(i).y = snake.get(i-1).y;
		}
		switch(direction) {
			case up:
				snake.get(0).y--;
				if(snake.get(0).y<0)	GameOver = true;
				break;
			case down:
				snake.get(0).y++;
				if(snake.get(0).y>height)	GameOver = true;
				break;
			case right:
				snake.get(0).x++;
				if(snake.get(0).x>width)	GameOver = true;
				break;
			case left:
				snake.get(0).x--;
				if(snake.get(0).x<0)	GameOver = true;
				break;
		}
		
		if(foodX == snake.get(0).x && foodY == snake.get(0).y) {

			snake.add(new Corner(-1, -1));
			newfood();
		}
		
		for(int i=1; i<snake.size(); i++) {
			if(snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) GameOver = true;
		}
		
		
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width*size, height*size);
		
		gc.setFont(new Font("",30));
		gc.setFill(Color.WHITE);
		gc.fillText("SCORE:"+(speed-6),10,30);

		
		Color c = Color.WHITE;
		
		switch(foodcolor){
			case 0:
				c = Color.WHITE;
				break;
			case 1:
				c = Color.BLUE;
				break;
			case 2:
				c = Color.BROWN;
				break;
			case 3:
				c = Color.DARKORANGE;
				break;
			case 4:
				c = Color.LIGHTGREEN;
				break;
		}
		
		gc.setFill(c);
		gc.fillOval(foodX*size, foodY*size, size, size);
		
		for(Corner cs:snake) {
			gc.setFill(Color.LIGHTGREEN);
			gc.fillRect(cs.x*size, cs.y*size, size-1, size-1);
			gc.setFill(Color.GREEN);
			gc.fillRect(cs.x*size, cs.y*size, size-2, size-2);
		}
	}
	
	
	public static void newfood() {
		start: while(true) {
			foodX = rand.nextInt(width);
			foodY = rand.nextInt(height);
			
			for(Corner c:snake) {
				if(c.x == foodX && c.y == foodY) {
					continue start;
				}
			}
			foodcolor = rand.nextInt(5);
			speed++;
			break;	
		}
	}
	public void reset() {
		 	
		    foodcolor=0;
		    foodX = 0;
		    foodY = 0;
		    size = 25;
		    speed = 5;
		    snake.clear();
		    GameOver = false;
	}
	
	public void addRestartButton() {
	    String buttonText = "New Game?";
	    JButton resetB = new JButton(buttonText);
	    
	   
	    resetB.setLayout(null);
	    resetB.setBounds(100,10,200,100);
	    resetB.add(resetB);

	    resetB.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				reset();
			}
	    });  

	    resetB.setVisible(false);
	}
	public static void main(String[] args) {
		launch(args);
	}
}
