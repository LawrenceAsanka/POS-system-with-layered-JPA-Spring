package lk.ijse.dep.db;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import lk.ijse.dep.entity.Customer;
import lk.ijse.dep.entity.Item;
import lk.ijse.dep.entity.Order;
import lk.ijse.dep.entity.OrderDetail;

public class HibernateUtil {

  private static final SessionFactory sessionFactory = buildSessionFactory();


  private static SessionFactory buildSessionFactory() {

    File file = new File("resources/application.properties");
    StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
        .loadProperties(file)
        .build();

    Metadata metadata = new MetadataSources(standardRegistry)
        .addAnnotatedClass(Customer.class)
        .addAnnotatedClass(Order.class)
        .addAnnotatedClass(Item.class)
        .addAnnotatedClass(OrderDetail.class)
        .getMetadataBuilder()
        .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
        .build();

    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
        .build();

    return sessionFactory;

  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

}
