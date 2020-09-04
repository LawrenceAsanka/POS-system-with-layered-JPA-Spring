package lk.ijse.dep.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

  private static final EntityManagerFactory emf = buildEntityManagerFactory();

  private static EntityManagerFactory buildEntityManagerFactory() {

    File fileProp = new File("resources/application.properties");
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(fileProp));
    } catch (IOException e) {
      throw new RuntimeException();
    }
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("DEP", properties);
    return emf;
  }


  public static EntityManagerFactory getEm() {
    return emf;
  }

}
