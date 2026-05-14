package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        Session session = connector.getSession();

        Query<Meeting> query = session.createQuery("FROM Meeting", Meeting.class);

        return query.list();
    }

    public Meeting findById(Long id) {
        Session session = connector.getSession();

        return session.get(Meeting.class, id);
    }

    public void add(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
    }
    public void delete(Long id) {
        Transaction transaction = connector.getSession().beginTransaction();

        Meeting meeting = connector.getSession().get(Meeting.class, id);

        if (meeting != null) {
            connector.getSession().delete(meeting);
        }

        transaction.commit();
    }
    public void update(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();

        connector.getSession().update(meeting);

        transaction.commit();
    }
}