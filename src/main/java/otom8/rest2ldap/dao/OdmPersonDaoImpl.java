package otom8.rest2ldap.dao;

import otom8.rest2ldap.dao.PersonDao;
import otom8.rest2ldap.domain.Person;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


public class OdmPersonDaoImpl implements PersonDao {
    private LdapTemplate ldapTemplate;

    @Override
    public void create(Person person) {
        ldapTemplate.create(person);
    }

    @Override
    public void update(Person person) {
        ldapTemplate.update(person);
    }

    @Override
    public void delete(Person person) {
        ldapTemplate.delete(ldapTemplate.findByDn(buildDn(person), Person.class));
    }
    @Override
    public List<String> getAllPersonNames() {
        return ldapTemplate.search(query()
                        .attributes("cn")
                        .where("objectclass").is("person"),
                new AttributesMapper<String>() {
                    public String mapFromAttributes(Attributes attrs) throws NamingException {
                        return attrs.get("cn").get().toString();
                    }
                });
    }

    @Override
    public List<Person> findAll() {
        return ldapTemplate.findAll(Person.class);
    }

    @Override
    public Person findByPrimaryKey(String country, String company, String fullname) {
        LdapName dn = buildDn(country, company, fullname);
        Person person = ldapTemplate.findByDn(dn, Person.class);

        return person;
    }

    private LdapName buildDn(Person person) {
        return buildDn(person.getCountry(), person.getCompany(), person.getFullName());
    }

    private LdapName buildDn(String country, String company, String fullname) {
        return LdapNameBuilder.newInstance()
                .add("c", country)
                .add("ou", company)
                .add("cn", fullname)
                .build();
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }
}
