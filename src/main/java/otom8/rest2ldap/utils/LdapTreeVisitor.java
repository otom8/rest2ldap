package otom8.rest2ldap.utils;

import org.springframework.ldap.core.DirContextOperations;

public interface LdapTreeVisitor {

    public void visit(DirContextOperations node, int currentDepth);
}