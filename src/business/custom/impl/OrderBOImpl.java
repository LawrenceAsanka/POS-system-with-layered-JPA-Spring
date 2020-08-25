package business.custom.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.custom.OrderBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import db.JPAUtil;
import entity.Item;
import entity.Order;
import entity.OrderDetail;
import util.OrderDetailTM;
import util.OrderTM;

public class OrderBOImpl implements OrderBO { // , Temp

  private final OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
  private final OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER_DETAIL);
  private final ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
  private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);

  // Interface through injection
/*    @Override
    public void injection() {
        this.orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
    }  */

  // Setter method injection
/*    private void setOrderDAO(){
        this.orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
    }*/

  public String getNewOrderId() throws Exception {

    EntityManagerFactory emf = JPAUtil.getEm();
    EntityManager em = emf.createEntityManager();
    orderDAO.setEntityManger(em);
    String lastOrderId = null;

    try {

      em.getTransaction().begin();

      lastOrderId = orderDAO.getLastOrderId();

      em.getTransaction().commit();
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }

    if (lastOrderId == null) {
      return "OD001";
    } else {
      int maxId = Integer.parseInt(lastOrderId.replace("OD", ""));
      maxId = maxId + 1;
      String id = "";
      if (maxId < 10) {
        id = "OD00" + maxId;
      } else if (maxId < 100) {
        id = "OD0" + maxId;
      } else {
        id = "OD" + maxId;
      }
      return id;
    }
  }

  public boolean placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) throws Exception {
    EntityManagerFactory emf = JPAUtil.getEm();
    EntityManager em = emf.createEntityManager();
    orderDAO.setEntityManger(em);
    customerDAO.setEntityManger(em);
    orderDetailDAO.setEntityManger(em);
    itemDAO.setEntityManger(em);
    try {

      em.getTransaction().begin();
      orderDAO.save(new Order(order.getOrderId(),
          Date.valueOf(order.getOrderDate()),
          customerDAO.find(order.getCustomerId())));

      for (OrderDetailTM orderDetail : orderDetails) {
        orderDetailDAO.save(new OrderDetail(
            order.getOrderId(), orderDetail.getCode(),
            orderDetail.getQty(), BigDecimal.valueOf(orderDetail.getUnitPrice())
        ));
        Item item = itemDAO.find(orderDetail.getCode());
        item.setQtyOnHand(item.getQtyOnHand() - orderDetail.getQty());
        itemDAO.update(item);

      }
      em.getTransaction().commit();
      return true;
    } catch (Throwable t) {
      em.getTransaction().rollback();
      throw t;

    } finally {
      em.close();
    }
  }
}
