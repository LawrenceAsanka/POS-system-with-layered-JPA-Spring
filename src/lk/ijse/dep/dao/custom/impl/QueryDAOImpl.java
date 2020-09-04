package lk.ijse.dep.dao.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import lk.ijse.dep.dao.custom.QueryDAO;
import lk.ijse.dep.entity.CustomEntity;
import org.hibernate.transform.Transformers;

import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    private EntityManager entityManager;

    @Override
    public void setEntityManger(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    @Override
    public List<CustomEntity> getOrderDetail() throws Exception {
        return entityManager.createNativeQuery("SELECT o.id AS orderId,o.date AS orderDate,c.id AS customerId,c.name AS customerName,(SUM(od.qty*od.unitPrice)) AS total FROM `Order` o\n" +
                "INNER JOIN OrderDetail od on o.id = od.orderId\n" +
                "INNER JOIN Customer c on o.customerId = c.id\n" +
                "GROUP BY o.id").getResultList();
    }
}
