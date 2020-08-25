package business.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.custom.CustomerBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.custom.CustomerDAO;
import db.JPAUtil;
import entity.Customer;
import util.CustomerTM;

public class CustomerBOImpl implements CustomerBO {


  // Field Injection
  private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);

  public List<CustomerTM> getAllCustomers() throws Exception {
    EntityManagerFactory emf = JPAUtil.getEm();
    EntityManager em = emf.createEntityManager();
    customerDAO.setEntityManger(em);
    List<Customer> allCustomers = null;
    try {

      em.getTransaction().begin();

      allCustomers = customerDAO.findAll();

      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }

    List<CustomerTM> customers = new ArrayList<>();
    for (Customer customer : allCustomers) {
      customers.add(new CustomerTM(customer.getId(), customer.getName(), customer.getAddress()));
    }
    return customers;

  }

  public void saveCustomer(String id, String name, String address) throws Exception {
    EntityManagerFactory emf = JPAUtil.getEm();
    EntityManager em = emf.createEntityManager();
    customerDAO.setEntityManger(em);
    try {

      em.getTransaction().begin();

      customerDAO.save(new Customer(id, name, address));
      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }


  }

  public void deleteCustomer(String customerId) throws Exception {
    EntityManagerFactory emf = JPAUtil.getEm();
    EntityManager em = emf.createEntityManager();
    customerDAO.setEntityManger(em);
    try {

      em.getTransaction().begin();

      customerDAO.delete(customerId);
      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }

  }

  public void updateCustomer(String name, String address, String customerId) throws Exception {
    EntityManagerFactory emf = JPAUtil.getEm();
    EntityManager em = emf.createEntityManager();
    customerDAO.setEntityManger(em);
    try {

      em.getTransaction().begin();

      customerDAO.update(new Customer(customerId, name, address));
      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }


  }

  public String getNewCustomerId() throws Exception {
    EntityManagerFactory emf = JPAUtil.getEm();
    EntityManager em = emf.createEntityManager();
    customerDAO.setEntityManger(em);
    String lastCustomerId = null;

    try {

      em.getTransaction().begin();

      lastCustomerId = customerDAO.getLastCustomerId();

      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }

    if (lastCustomerId == null) {
      return "C001";
    } else {
      int maxId = Integer.parseInt(lastCustomerId.replace("C", ""));
      maxId = maxId + 1;
      String id = "";
      if (maxId < 10) {
        id = "C00" + maxId;
      } else if (maxId < 100) {
        id = "C0" + maxId;
      } else {
        id = "C" + maxId;
      }
      return id;
    }

  }

}
