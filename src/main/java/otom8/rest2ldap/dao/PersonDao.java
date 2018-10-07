package otom8.rest2ldap.dao;

import otom8.rest2ldap.domain.Person;
import java.util.List;

public interface PersonDao {
    void create(Person person);

    void update(Person person);

    void delete(Person person);

    List<String> getAllPersonNames();

    List<Person> findAll();

    Person findByPrimaryKey(String country, String company, String fullname);
}
