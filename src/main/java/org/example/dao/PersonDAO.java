package org.example.dao;

import org.example.entities.Person;

import javax.persistence.EntityManager;

public class PersonDAO {
private EntityManager em;

    public PersonDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Person p){
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }
    public void delete(Person p){
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }
    public Person getById(long id){
        return em.find(Person.class, id);
    }
}
