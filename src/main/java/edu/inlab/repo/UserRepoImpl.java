package edu.inlab.repo;

import edu.inlab.models.User;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/5.
 */
@Repository("userRepo")
public class UserRepoImpl extends AbstractDao<Integer, User> implements UserRepository {
    public User getUserById(int id) {
        return getByKey(id);
    }

    public User getUserByEmail(String email) {
        List<User> list = getSession().createCriteria(User.class)
                .add(Restrictions.eq("email", email))
                .list();
        if(list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    public void saveUser(User user) {
        persist(user);
    }

    public void deleteUserById(int id) {
        Query query = getSession().createSQLQuery("DELETE FROM User WHERE id= :id");
        query.setInteger("id", id);
        query.executeUpdate();
    }
}
