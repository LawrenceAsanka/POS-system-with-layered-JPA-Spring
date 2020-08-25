package dao.custom.impl;

import java.util.List;

import org.hibernate.Session;

import dao.CrudDAOImpl;
import dao.custom.OrderDAO;
import entity.Customer;
import entity.Order;

public class OrderDAOImpl extends CrudDAOImpl<Order,String> implements OrderDAO {

  public String getLastOrderId() throws Exception {
    List list = session.createQuery("SELECT o.id FROM entity.Order o ORDER BY id DESC").setMaxResults(1).list();
    return list.size() > 0 ? (String) list.get(0) : null;
  }

}
