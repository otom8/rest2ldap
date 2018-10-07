package otom8.rest2ldap.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameNotFoundException;
import otom8.rest2ldap.dao.PersonDao;
import otom8.rest2ldap.domain.Person;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ContextConfiguration(locations = { "/testContext.xml" })
public class PersonDaoSampleIntegrationTest extends
        AbstractJUnit4SpringContextTests {

    protected Person person;

    @Autowired
    private PersonDao personDao;

    @Before
    public void preparePerson() throws Exception {
        person = new Person();
        person.setCountry("Sweden");
        person.setCompany("company1");
        person.setFullName("Some Person");
        person.setLastName("Person");
        person
                .setDescription("Sweden, Company1, Some Person");
        person.setPhone("+46 555-123456");
    }

    /**
     * Having a single test method test create, update and delete is not exactly
     * the ideal way of testing, since they depend on each other. A better way
     * would be to separate the tests and load a test fixture before each
     * operation, in order to guarantee the expected state every time. See the
     * ldaptemplate-person sample for the correct way to do this.
     */
    @Test
    public void testCreateUpdateDelete() {
        try {
            person.setFullName("Another Person");
            personDao.create(person);
            personDao.findByPrimaryKey(
                    "Sweden", "company1",
                    "Another Person");
            // if we got here, create succeeded

            person.setDescription("Another description");
            personDao.update(person);
            Person result = personDao
                    .findByPrimaryKey(
                            "Sweden", "company1",
                            "Another Person");
            assertThat(result.getDescription()).isEqualTo("Another description");
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            personDao.delete(person);
            try {
                personDao.findByPrimaryKey(
                        "Sweden", "company1",
                        "Another Person");
                fail("NameNotFoundException (when using Spring LDAP) or RuntimeException (when using traditional) expected");
            } catch (NameNotFoundException expected) {
                // expected
            } catch (RuntimeException expected) {
                // expected
            }
        }
    }

    @Test
    public void testGetAllPersonNames() {
        List<String> result = personDao.getAllPersonNames();
        assertThat(result).hasSize(2);
        String first = result.get(0);
        assertThat(first).isEqualTo("Some Person");
    }

    @Test
    public void testFindAll() {
        List<Person> result = personDao.findAll();
        assertThat(result).hasSize(2);
        Person first = result.get(0);
        assertThat(first.getFullName()).isEqualTo("Some Person");
    }

    @Test
    public void testFindByPrimaryKey() {
        Person result = personDao.findByPrimaryKey(
                "Sweden", "company1", "Some Person");

        assertThat(result.getCountry()).isEqualTo("Sweden");
        assertThat(result.getCompany()).isEqualTo("company1");
        assertThat(result.getDescription()).isEqualTo("Sweden, Company1, Some Person");
        assertThat(result.getPhone()).isEqualTo("+46 555-123456");
        assertThat(result.getFullName()).isEqualTo("Some Person");
        assertThat(result.getLastName()).isEqualTo("Person");
    }
}