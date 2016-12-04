package edu.inlab.repo;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Created by inlab-dell on 2016/5/5.
 * This Generic class is the base class for all DAO implementation classes.
 * It provides the wrapper methods for common hibernate operations.
 * The SessionFactory created earlier will be autowired here.
 * See: http://websystique.com/springmvc/spring-4-mvc-and-hibernate4-integration-example-using-annotations/
 */
public abstract class AbstractDao <PK extends Serializable, T> {
    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractDao(){
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    private SessionFactory sessionFactory;
    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    protected StatelessSession getStatelessSession(){
        return sessionFactory.openStatelessSession();
    }

    @SuppressWarnings("unchecked")
    public T getByKey(PK key){
        return (T) getSession().get(persistentClass, key);
    }

    public void persist(T entity){
        getSession().persist(entity);
    }

    public void saveOrUpdate(T entity){
        getSession().saveOrUpdate(entity);
    }

    public void delete(T entity){
        getSession().delete(entity);
    }

    protected Criteria createEntityCriteria(){
        return getSession().createCriteria(persistentClass);
    }
}
