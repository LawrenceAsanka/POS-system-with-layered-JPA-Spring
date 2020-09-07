package lk.ijse.dep.dao.custom.impl;

import lk.ijse.dep.dao.CrudDAOImpl;
import lk.ijse.dep.dao.custom.CustomerDAO;
import lk.ijse.dep.entity.Customer;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;

@Component
public class CustomerDAOImpl extends CrudDAOImpl<Customer, String> implements CustomerDAO {


    @Override
    public String getLastCustomerId() throws Exception {
        try {
            return (String) entityManager.createNativeQuery("SELECT id FROM Customer ORDER BY id DESC LIMIT 1").getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

}
