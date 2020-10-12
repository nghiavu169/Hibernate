package service.customer;

import model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class CustomerService implements ICustomerService {

    @Autowired
    private SessionFactory sessionFactory;

    @PersistenceContext
    EntityManager entityManager;

    public Iterable<Customer> findAll() {
        TypedQuery<Customer> query = entityManager.createQuery("SELECT s FROM Customer AS s", Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findById(int id) {
        String queryStr = "SELECT c FROM Customer AS c WHERE c.id = :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Customer save(Customer customer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if (customer.getId() > 0) {
                session.merge(customer);
            } else {
                session.persist(customer);
            }
            transaction.commit();
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    @Override
    public Customer delete(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            Customer customer = findById(id);
            transaction = session.beginTransaction();
            session.delete(customer);
            transaction.commit();
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }
}
