package org.example.dao;

import org.example.entities.Library;

import javax.persistence.EntityManager;

public class LibraryDAO {
    private EntityManager em;

    public LibraryDAO(EntityManager em) {
        this.em = em;
    }
    public void save(Library l){
        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
    }
    public void delete(Library l){
        em.getTransaction().begin();
        em.remove(l);
        em.getTransaction().commit();
    }
    public Library getById(long id){
        return em.find(Library.class,id);
    }

}
