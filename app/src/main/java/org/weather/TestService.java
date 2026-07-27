package org.weather;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.weather.entity.Location;
import org.weather.entity.User;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class TestService {
    protected final SessionFactory sessionFactory;

    public void add() {

        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.persist();
                session.getTransaction().commit();
            } catch (Exception e) {
                Transaction transaction = session.getTransaction();
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }

        }
    }
}
