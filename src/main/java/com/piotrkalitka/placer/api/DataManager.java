package com.piotrkalitka.placer.api;

import com.piotrkalitka.placer.api.dbModels.User;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class DataManager {

    private EntityManager entityManager = Persistence.createEntityManagerFactory("dbManager").createEntityManager();
    private Session session = (Session) entityManager.getDelegate();

    public void addUser(String email, String name, String surname, String password) {
        User user = new User(email, name, surname, password);
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

}