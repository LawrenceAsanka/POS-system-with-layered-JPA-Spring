package dao.custom.impl;

import java.util.List;

import dao.CrudDAOImpl;
import dao.custom.ItemDAO;
import entity.Item;

public class ItemDAOImpl extends CrudDAOImpl<Item,String> implements ItemDAO {



  public String getLastItemCode() throws Exception {
    List list = entityManager.createQuery("SELECT i.code FROM entity.Item i ORDER BY i.code DESC").setMaxResults(1).getResultList();
    return list.size() > 0 ? (String) list.get(0) : null;

  }

}
