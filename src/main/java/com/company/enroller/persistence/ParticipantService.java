package com.company.enroller.persistence;

import java.util.Collection;
import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;


import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}
    public Participant findByLogin(String login) {
        return (Participant) connector.getSession().get(Participant.class, login);
    }
    public void add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
    }
    public void delete(String login) {
        Transaction transaction = connector.getSession().beginTransaction();

        Participant participant = connector.getSession().get(Participant.class, login);

        if (participant != null) {
            connector.getSession().delete(participant);
        }

        transaction.commit();
    }
    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();

        connector.getSession().update(participant);

        transaction.commit();
    }
    public List<Participant> getAll(String sortBy,
                                    String sortOrder,
                                    String key) {

        String hql = "FROM Participant WHERE 1=1";

        if (key != null && !key.isEmpty()) {
            hql += " AND login LIKE '%" + key + "%'";
        }

        if (sortBy != null && sortOrder != null) {
            hql += " ORDER BY " + sortBy + " " + sortOrder;
        }

        return connector.getSession()
                .createQuery(hql, Participant.class)
                .list();
    }


}
