package business.custom.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import business.custom.ItemBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.custom.ItemDAO;
import db.HibernateUtil;
import entity.Item;
import util.ItemTM;

public class ItemBOImpl implements ItemBO {

  // Dependency Declaration
  private final ItemDAO itemDAO;

  public ItemBOImpl() {
    // Constructor Injection
    this.itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
  }

  public String getNewItemCode() throws Exception {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    Session session = sessionFactory.openSession();
    itemDAO.setSession(session);
    String lastItemCode = null;
    Transaction tx = null;
    try {

      tx = session.beginTransaction();
     lastItemCode = itemDAO.getLastItemCode();

      tx.commit();

    } catch (Throwable t) {
      tx.rollback();
      throw t;
    } finally {
      session.close();
    }

    if (lastItemCode == null) {
      return "I001";
    } else {
      int maxId = Integer.parseInt(lastItemCode.replace("I", ""));
      maxId = maxId + 1;
      String id = "";
      if (maxId < 10) {
        id = "I00" + maxId;
      } else if (maxId < 100) {
        id = "I0" + maxId;
      } else {
        id = "I" + maxId;
      }
      return id;
    }
  }

  public List<ItemTM> getAllItems() throws Exception {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    Session session = sessionFactory.openSession();
    itemDAO.setSession(session);
    List<Item> allItems = null;
    Transaction tx = null;
    try {

      tx = session.beginTransaction();

      allItems = itemDAO.findAll();

      tx.commit();
    } catch (Throwable t) {
      tx.rollback();
      throw t;
    } finally {
      session.close();
    }

    List<ItemTM> items = new ArrayList<>();
    for (Item item : allItems) {
      items.add(new ItemTM(item.getCode(), item.getDescription(), item.getQtyOnHand(),
          item.getUnitPrice().doubleValue()));
    }
    return items;
  }

  public void saveItem(String code, String description, int qtyOnHand, double unitPrice) throws Exception {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    Session session = sessionFactory.openSession();
    itemDAO.setSession(session);
    Transaction tx = null;
    try {

      tx = session.beginTransaction();

      itemDAO.save(new Item(code, description, BigDecimal.valueOf(unitPrice), qtyOnHand));

      tx.commit();
    } catch (Throwable t) {
      tx.rollback();
      throw t;
    } finally {
      session.close();
    }

  }

  public void deleteItem(String itemCode) throws Exception {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    Session session = sessionFactory.openSession();
    itemDAO.setSession(session);
    Transaction tx = null;
    try {

      tx = session.beginTransaction();

      itemDAO.delete(itemCode);

      tx.commit();
    } catch (Throwable t) {
      tx.rollback();
      throw t;
    } finally {
      session.close();
    }

  }

  public void updateItem(String description, int qtyOnHand, double unitPrice, String itemCode) throws Exception {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    Session session = sessionFactory.openSession();
    itemDAO.setSession(session);
    Transaction tx = null;
    try {

      tx = session.beginTransaction();

      itemDAO.update(new Item(itemCode, description,
          BigDecimal.valueOf(unitPrice), qtyOnHand));
      tx.commit();
    } catch (Throwable t) {
      tx.rollback();
      throw t;
    } finally {
      session.close();
    }

  }
}
