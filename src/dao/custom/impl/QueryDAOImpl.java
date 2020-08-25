package dao.custom.impl;

import javax.persistence.EntityManager;

import dao.custom.QueryDAO;
import entity.CustomEntity;

import org.hibernate.transform.Transformers;

public class QueryDAOImpl implements QueryDAO {

    private EntityManager session;
    @Override
    public CustomEntity getOrderDetail(String orderId) throws Exception {
        return (CustomEntity) session.createQuery("SELECT  o.id AS orderId, c.name AS customerName, o.date AS orderDate "
            + "FROM entity.Order o INNER JOIN entity.Customer c WITH o.customerId = c.id WHERE o.id=?")
            .setResultTransformer(Transformers.aliasToBean(CustomEntity.class)).setParameter(1,orderId).uniqueResult();

    }

    @Override
    public CustomEntity getOrderDetail2(String orderId) throws Exception {
        return (CustomEntity) session.createQuery("SELECT NEW entity.CustomEntity(c.id,c.name,o.id) FROM entity.Order o INNER JOIN o.Customer c WHERE o.id=?1")
            .setParameter(1,orderId).list();

    }

    @Override
    public void setEntityManger(EntityManager session) {
        this.session=session;
    }
}
