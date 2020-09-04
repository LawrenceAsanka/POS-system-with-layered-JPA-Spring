package lk.ijse.dep.dao.custom.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import lk.ijse.dep.dao.DAOFactory;
import lk.ijse.dep.dao.DAOType;
import lk.ijse.dep.dao.custom.QueryDAO;
import lk.ijse.dep.entity.CustomEntity;

class QueryDAOImplTest {

  public static void main(String[] args) throws Exception {

    QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);

    File file = new File("resources/application.properties");
    Properties properties = new Properties();
    properties.load(new FileInputStream(file));

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("DEP", properties);
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();

    queryDAO.setEntityManger(em);

    CustomEntity orderDetail = queryDAO.getOrderDetail2("OD001");
    System.out.println(orderDetail.getOrderId());
    System.out.println(orderDetail.getCustomerName());
    System.out.println(orderDetail.getOrderDate());

    em.getTransaction().commit();
    em.close();
    emf.close();
  }

}