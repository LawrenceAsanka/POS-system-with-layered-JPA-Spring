package dao.custom.impl;

import java.util.List;

import dao.CrudDAOImpl;
import dao.custom.OrderDAO;
import entity.Order;

public class OrderDAOImpl extends CrudDAOImpl<Order,String> implements OrderDAO {

  public String getLastOrderId() throws Exception {
    List list = entityManager.createQuery("SELECT o.id FROM entity.Order o ORDER BY id DESC").setMaxResults(1).getResultList();
    return list.size() > 0 ? (String) list.get(0) : null;
  }

}
