package application;
	
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.lang.Math;
import java.util.Random;

public class Main extends Application{
	//rotational offset of the camera object, used to render the block in a better angle
	private int rotateY = -20;
	private int rotateX = -20;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        // instantiates the block
        Box block = new Box(7, 1, 5);
        block.setMaterial(new PhongMaterial(Color.RED));
        block.setDrawMode(DrawMode.LINE);
        
        //creates the lines marking the x, y, and z axis
        Box lineX = new Box(100, 0.01, 0.01);
        Box lineY = new Box(0.01, 100, 0.01);
        Box lineZ = new Box(0.01, 0.01, 100);
 
        // Create and position camera
        Camera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
                new Rotate(rotateY, Rotate.Y_AXIS),
                new Rotate(rotateX, Rotate.X_AXIS),
                new Translate(0, 0, -15));
 
        // Build the Scene Graph
        Group root = new Group();       
        root.getChildren().add(camera);
        root.getChildren().add(block);
        root.getChildren().add(lineX);
        root.getChildren().add(lineY);
        root.getChildren().add(lineZ);
 
        //sets up the camera and window size properly      
        SubScene subScene = new SubScene(root, 1280, 720); //window size, adjust numbers if necessary
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);
        Scene scene = new Scene(group);
        
        //displays the scene with all the objects in it on screen
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //creates an infinite loop to get the updated rotation of the block and render it out
        RotateTransition rt = new RotateTransition(Duration.millis(40), block); //The refresh rate of the loop is set in the constructor, can be changed to make it loop faster or slower
        rt.setCycleCount(1);
        rt.play();
        rt.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/*
		         * TODO: replace the getPos() function with a function that gets the quaternion
		         * as a double array with 4 indexes and the w, i, j, and k values stored
		         * in the indexes 0-3. 
		         */
				double[] vec = getPos();
				
				//math to calculate the angle of rotation and the axis of rotation from the given quaternion
		        double theta, Ax, Ay, Az, angle;
		        theta = Math.acos(vec[0]);
				Ax = vec[1] / Math.sin(theta);
				Ay = vec[2] / Math.sin(theta);
				Az = vec[3] / Math.sin(theta);
				angle = Math.toDegrees(2 * theta);
				
				//renders out the new rotation of the block
				block.getTransforms().clear();
				block.getTransforms().add(new Rotate(angle, new Point3D(Ax, Ay, Az)));	        
				rt.play(); //loops the transition again
			}    	
        });
    }
    
    //for testing, spits out a couple random rotations for the rendering to be updated
    public double[] getPos() {
    	Random rand = new Random();
    	int r = rand.nextInt(7);
    	switch(r) {
    	case 0:
    		return new double[]{0.95, -0.14, 0.14,-0.25};
    	case 1:
    		return new double[]{0.31, -0.21, -0.92, -0.066};
    	case 2:
    		return new double[]{0.69, 0.096, 0.68, -0.21};
    	case 3:
    		return new double[]{0.48, -0.29, 0.79, 0.24};
    	case 4:
    		return new double[]{0.5, 0.14, 0.63, 0.58};
    	case 5:
    		return new double[]{-0.77, -0.23, -0.094, -0.59};
    	default:
    		return new double[]{1, 0, 0, 0};
    	}
    }
    
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
