package org.example;

import org.example.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

/**
 * Hello world!
 *
 */
public class Main 
{

    public static void main( String[] args ) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Library");
        EntityManager em = emf.createEntityManager();


    }
}
