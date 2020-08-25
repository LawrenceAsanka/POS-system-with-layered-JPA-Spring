package dao.custom.impl;

import dao.CrudDAOImpl;
import dao.custom.CustomerDAO;
import entity.Customer;

import java.util.List;

import org.hibernate.Session;

public class CustomerDAOImpl extends CrudDAOImpl<Customer,String> implements CustomerDAO {


    @Override
    public String getLastCustomerId() throws Exception {
        return (String)session.createNativeQuery("SELECT id FROM Customer ORDER BY id DESC LIMIT 1").uniqueResult();

    }

}
