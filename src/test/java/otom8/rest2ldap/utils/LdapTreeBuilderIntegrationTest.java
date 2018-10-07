package otom8.rest2ldap.utils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.naming.ldap.LdapName;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(locations = { "/testContext.xml" })
public class LdapTreeBuilderIntegrationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private LdapTreeBuilder tested;

    @Test
    public void testGetLdapTree() {
        LdapTree ldapTree = tested.getLdapTree(LdapUtils.newLdapName("c=Sweden"));
        ldapTree.traverse(new TestVisitor());
    }

    private static final class TestVisitor implements LdapTreeVisitor {
        private static final LdapName DN_1 = LdapUtils.newLdapName("c=Sweden");
        private static final LdapName DN_2 = LdapUtils.newLdapName("ou=company1,c=Sweden");
        private static final LdapName DN_3 = LdapUtils.newLdapName("cn=Some Person,ou=company1,c=Sweden");
        private static final LdapName DN_4 = LdapUtils.newLdapName("cn=Some Person2,ou=company1,c=Sweden");

        private Map<LdapName, Integer> names = new LinkedHashMap<LdapName, Integer>();

        private Iterator<LdapName> keyIterator;

        public TestVisitor() {
            names.put(DN_1, 0);
            names.put(DN_2, 1);
            names.put(DN_3, 2);
            names.put(DN_4, 2);

            keyIterator = names.keySet().iterator();
        }

        public void visit(DirContextOperations node, int currentDepth) {
            LdapName next = keyIterator.next();
            assertThat(node.getDn()).isEqualTo(next);
            assertThat(currentDepth).isEqualTo(names.get(next).intValue());
        }
    }

}