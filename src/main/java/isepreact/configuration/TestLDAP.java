package isepreact.configuration;

import isepreact.configuration.ldap.LDAPAccess;
import isepreact.configuration.ldap.LDAPObject;

class TestLDAP
{
	public static void main(String[] args)
	{
		LDAPAccess access = new LDAPAccess();
		try {
			//Enter login and password Here
		LDAPObject test = access.LDAPget("vostertag", "");
		if (test == null)
		{
			System.out.println("login invalide");
			System.exit(1);
		}
		System.out.println(test.toString());
		System.exit(0);
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}