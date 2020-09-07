package lk.ijse.dep;

import java.io.IOException;

import lk.ijse.dep.db.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInitializer extends Application {

  private static AnnotationConfigApplicationContext ctx;

  public static AnnotationConfigApplicationContext getCtx(){
    return ctx;
  }
  public static void main(String[] args) {
    ctx = new AnnotationConfigApplicationContext();
    ctx.registerShutdownHook();
    ctx.register(AppConfig.class);
    ctx.refresh();

    launch(args);

    JPAUtil.getEm().close();

  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/dep/view/MainForm.fxml"));
    Scene mainScene = new Scene(root);
    primaryStage.setScene(mainScene);
    primaryStage.setTitle("JDBC POS");
    primaryStage.centerOnScreen();
    primaryStage.show();

  }
}
