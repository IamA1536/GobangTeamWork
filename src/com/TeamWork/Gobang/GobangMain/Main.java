package com.TeamWork.Gobang.GobangMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MENU.fxml"));
			Parent root = fxmlLoader.load();
			primaryStage.setTitle("GoBang");
			primaryStage.setResizable(false);
			Scene scene = new Scene(root,1248,846);
			Controller controller =fxmlLoader.getController();
			controller.SetStage(primaryStage);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
