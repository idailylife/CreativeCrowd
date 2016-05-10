package edu.inlab.repo;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by inlab-dell on 2016/5/10.
 * Defines customized JSON usertype for Hibernate
 */
public class JSONUserType implements UserType {
    public int[] sqlTypes() {
        return new int[] {Types.VARCHAR};
    }

    public Class returnedClass() {
        return String.class;
    }

    public boolean equals(Object obj1, Object obj2) throws HibernateException {
        if(obj1 == null){
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }

    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset.
     * Should cope with null values.
     * @param resultSet a JDBC result set
     * @param names     the column names
     * @param sessionImplementor session
     * @param o         the containing entity (owner)
     * @return
     * @throws HibernateException
     * @throws SQLException
     */
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        JSONObject jsonObject = new JSONObject(resultSet.getString(names[0]));
        return jsonObject;
    }

    /**
     * Write an instance of the mapped class to a prepared statement
     * Should handle null values
     * @param preparedStatement a JDBC prepared statement
     * @param value the object to write
     * @param index statement parameter index
     * @param sessionImplementor session
     * @throws HibernateException
     * @throws SQLException
     */
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        if(value == null){
            preparedStatement.setNull(index, Types.OTHER);
            return;
        }

        preparedStatement.setString(index, value.toString());
    }

    public Object deepCopy(Object o) throws HibernateException {
        return o;
    }

    public boolean isMutable() {
        return true;
    }

    public Serializable disassemble(Object o) throws HibernateException {
        return (String)this.deepCopy(o);
    }

    public Object assemble(Serializable objectCached, Object owner) throws HibernateException {
        return this.deepCopy(objectCached);
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }
}
