package com.piotrkalitka.placer.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.piotrkalitka.placer.api.dbModels.User;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.lang.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class DataManager {

    private EntityManager entityManager = Persistence.createEntityManagerFactory("dbManager").createEntityManager();
    private Session session = (Session) entityManager.getDelegate();

    @Nullable
    public User getUser(String email) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(builder.equal(root.get("email"), email));
        query.select(root);
        Query<User> q = session.createQuery(query);
        return q.uniqueResult();
    }

    public boolean isEmailRegistered(String email) {
        return getUser(email) != null;
    }

    public void addUser(String email, String name, String surname, String password) {
        User user = new User(email, name, surname, password);
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }



    @Nullable
    public static String generateAccessToken(int userId, String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_SECRET);
            return JWT.create()
                    .withExpiresAt(new Date(new Date().getTime() + Constants.ACCESS_TOKEN_DURATION_MILLIS))
                    .withClaim("id", userId)
                    .withClaim("email", email)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

    @Nullable
    public static String generateRefreshToken(int userId, String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_SECRET);
            return JWT.create()
                    .withExpiresAt(new Date(new Date().getTime() + Constants.REFRESH_TOKEN_DURATION_MILLIS))
                    .withClaim("id", userId)
                    .withClaim("email", email)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
    }

}