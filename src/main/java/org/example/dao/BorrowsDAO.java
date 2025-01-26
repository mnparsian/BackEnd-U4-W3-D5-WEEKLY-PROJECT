package org.example.dao;



import org.example.entities.Borrows;

import javax.persistence.EntityManager;

public class BorrowsDAO {
    private EntityManager em;

    public BorrowsDAO(EntityManager em) {
        this.em = em;
    }
    public void save(Borrows b){
        em.getTransaction().begin();
        em.persist(b);
        em.getTransaction().commit();
    }
    public void delete(Borrows l){
        em.getTransaction().begin();
        em.remove(l);
        em.getTransaction().commit();
    }
    public Borrows getById(long id){
        return em.find(Borrows.class,id);
    }
}
