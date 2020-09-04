package lk.ijse.dep.dao.custom.impl;

import java.util.List;

import lk.ijse.dep.dao.CrudDAOImpl;
import lk.ijse.dep.dao.custom.OrderDAO;
import lk.ijse.dep.entity.Order;

public class OrderDAOImpl extends CrudDAOImpl<Order,String> implements OrderDAO {

  public String getLastOrderId() throws Exception {
    List list = entityManager.createQuery("SELECT o.id FROM lk.ijse.dep.entity.Order o ORDER BY id DESC").setMaxResults(1).getResultList();
    return list.size() > 0 ? (String) list.get(0) : null;
  }

}
