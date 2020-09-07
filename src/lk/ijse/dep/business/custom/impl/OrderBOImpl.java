package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.OrderBO;
import lk.ijse.dep.dao.custom.*;
import lk.ijse.dep.db.JPAUtil;
import lk.ijse.dep.entity.CustomEntity;
import lk.ijse.dep.entity.Item;
import lk.ijse.dep.entity.Order;
import lk.ijse.dep.entity.OrderDetail;
import lk.ijse.dep.util.OrderDetailTM;
import lk.ijse.dep.util.OrderTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBOImpl implements OrderBO {

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private CustomerDAO customerDAO;
    @Autowired
    private QueryDAO queryDAO;


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

    public void placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) throws Exception {
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
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw t;

        } finally {
            em.close();
        }
    }

    @Override
    public List<OrderTM> getAllOrders() throws Exception {
        EntityManagerFactory emf = JPAUtil.getEm();
        EntityManager em = emf.createEntityManager();
        queryDAO.setEntityManger(em);
        List<CustomEntity> odl = null;
        try {

            em.getTransaction().begin();

            odl = queryDAO.getOrderDetail();
            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            em.close();
        }
        List<OrderTM> orderDetailsList = new ArrayList<>();
        for (CustomEntity orderDetails : odl) {
            BigDecimal total = orderDetails.getTotal();

            orderDetailsList.add(new OrderTM(orderDetails.getOrderId(), orderDetails.getOrderDate().toLocalDate(), orderDetails.getCustomerId(),
                    orderDetails.getCustomerName(), Double.parseDouble(total.toString())));
        }
        return orderDetailsList;
    }
}
