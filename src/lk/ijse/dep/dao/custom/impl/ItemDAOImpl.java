package lk.ijse.dep.dao.custom.impl;

import java.util.List;

import lk.ijse.dep.dao.CrudDAOImpl;
import lk.ijse.dep.dao.custom.ItemDAO;
import lk.ijse.dep.entity.Item;

public class ItemDAOImpl extends CrudDAOImpl<Item,String> implements ItemDAO {



  public String getLastItemCode() throws Exception {
    List list = entityManager.createQuery("SELECT i.code FROM lk.ijse.dep.entity.Item i ORDER BY i.code DESC").setMaxResults(1).getResultList();
    return list.size() > 0 ? (String) list.get(0) : null;

  }

}
