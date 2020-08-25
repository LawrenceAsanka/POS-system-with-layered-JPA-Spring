package dao.custom.impl;

import dao.custom.CustomerDAO;
import entity.Customer;

import java.util.List;

import org.hibernate.Session;

public class CustomerDAOImpl2 implements CustomerDAO {

    @Override
    public String getLastCustomerId() {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public Customer find(String key) {
        return null;
    }

    @Override
    public void save(Customer entity) {
    }

    @Override
    public void update(Customer entity) {
    }

    @Override
    public void delete(String key) {
    }

    @Override
    public void setSession(Session session) {

    }
}
