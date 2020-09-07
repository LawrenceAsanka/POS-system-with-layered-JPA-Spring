package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.ItemBO;
import lk.ijse.dep.dao.custom.ItemDAO;
import lk.ijse.dep.db.JPAUtil;
import lk.ijse.dep.entity.Item;
import lk.ijse.dep.util.ItemTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemBOImpl implements ItemBO {

    // Dependency Declaration
    @Autowired
    private ItemDAO itemDAO;

    public String getNewItemCode() throws Exception {
        EntityManagerFactory emf = JPAUtil.getEm();
        EntityManager em = emf.createEntityManager();
        String lastItemCode = null;
        itemDAO.setEntityManger(em);
        try {

            em.getTransaction().begin();

            lastItemCode = itemDAO.getLastItemCode();

            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            em.close();
        }
        if (lastItemCode == null) {
            return "I001";
        } else {
            int maxId = Integer.parseInt(lastItemCode.replace("I", ""));
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10) {
                id = "I00" + maxId;
            } else if (maxId < 100) {
                id = "I0" + maxId;
            } else {
                id = "I" + maxId;
            }
            return id;
        }
    }

    public List<ItemTM> getAllItems() throws Exception {

        EntityManagerFactory emf = JPAUtil.getEm();
        EntityManager em = emf.createEntityManager();
        List<Item> allItems = null;
        itemDAO.setEntityManger(em);
        try {

            em.getTransaction().begin();

            allItems = itemDAO.findAll();

            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            em.close();
        }
        List<ItemTM> items = new ArrayList<>();
        for (Item item : allItems) {
            items.add(new ItemTM(item.getCode(), item.getDescription(), item.getQtyOnHand(),
                    item.getUnitPrice().doubleValue()));
        }
        return items;
    }

    public void saveItem(String code, String description, int qtyOnHand, double unitPrice) throws Exception {

        EntityManagerFactory emf = JPAUtil.getEm();
        EntityManager em = emf.createEntityManager();
        itemDAO.setEntityManger(em);
        try {

            em.getTransaction().begin();

            itemDAO.save(new Item(code, description, BigDecimal.valueOf(unitPrice), qtyOnHand));

            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            em.close();
        }

    }

    public void deleteItem(String itemCode) throws Exception {

        EntityManagerFactory emf = JPAUtil.getEm();
        EntityManager em = emf.createEntityManager();
        itemDAO.setEntityManger(em);
        try {

            em.getTransaction().begin();

            itemDAO.delete(itemCode);

            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            em.close();
        }

    }

    public void updateItem(String description, int qtyOnHand, double unitPrice, String itemCode) throws Exception {
        EntityManagerFactory emf = JPAUtil.getEm();
        EntityManager em = emf.createEntityManager();
        itemDAO.setEntityManger(em);
        try {

            em.getTransaction().begin();

            itemDAO.update(new Item(itemCode, description,
                    BigDecimal.valueOf(unitPrice), qtyOnHand));

            em.getTransaction().commit();
        } catch (Throwable t) {
            em.getTransaction().rollback();
            throw t;
        } finally {
            em.close();
        }

    }
}
