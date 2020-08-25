package dao.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import dao.custom.QueryDAO;
import entity.CustomEntity;

import org.hibernate.transform.Transformers;

public class QueryDAOImpl implements QueryDAO {

    private EntityManager entityManager;
    @Override
    public CustomEntity getOrderDetail(String orderId) throws Exception {
        try {
            return (CustomEntity) entityManager.createQuery("SELECT  o.id, c.name , o.date "
                 + "FROM entity.Order o INNER JOIN entity.Customer c WITH o.customerId = c.id WHERE o.id=?1")
                .setParameter(1,orderId).getSingleResult();
        } catch (NoResultException e) {
           return null;
        }

    }

    @Override
    public CustomEntity getOrderDetail2(String orderId) throws Exception {
        try {
            return (CustomEntity) entityManager.createQuery("SELECT NEW entity.CustomEntity(c.id,c.name,o.id) FROM entity.Order o INNER JOIN o.Customer c WHERE o.id=?1")
                .setParameter(1,orderId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public void setEntityManger(EntityManager entityManager) {
        this.entityManager=entityManager;
    }
}
