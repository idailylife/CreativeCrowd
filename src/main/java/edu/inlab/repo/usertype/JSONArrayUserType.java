package edu.inlab.repo.usertype;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.json.JSONArray;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by inlab-dell on 2016/5/12.
 */
public class JSONArrayUserType implements UserType {
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

    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        String resultStr = resultSet.getString(names[0]);
        JSONArray jsonArray = null;
        if(null != resultStr)
            jsonArray = new JSONArray(resultSet.getString(names[0]));
        return jsonArray;
    }

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

    public Object assemble(Serializable serializable, Object objectCached) throws HibernateException {
        return this.deepCopy(objectCached);
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }
}
