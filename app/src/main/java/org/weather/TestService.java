package org.weather;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.weather.entity.User;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class TestService {
    protected final SessionFactory sessionFactory;

    public void add() {
        User user = new User();

        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.persist(user);
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
