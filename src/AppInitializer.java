import java.io.IOException;

import db.HibernateUtil;
import db.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application {

  public static void main(String[] args) {
    launch(args);

    JPAUtil.getEm().close();

  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
    Scene mainScene = new Scene(root);
    primaryStage.setScene(mainScene);
    primaryStage.setTitle("JDBC POS");
    primaryStage.centerOnScreen();
    primaryStage.show();

  }
}
