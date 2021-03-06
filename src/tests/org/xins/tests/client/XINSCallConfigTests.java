/*
 * $Id: XINSCallConfigTests.java,v 1.9 2010/09/29 17:21:48 agoubard Exp $
 *
 * See the COPYRIGHT file for redistribution and use restrictions.
 */
package org.xins.tests.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.xins.client.XINSCallConfig;
import org.xins.common.http.HTTPMethod;

/**
 * Tests for class <code>XINSCallConfig</code>.
 *
 * @version $Revision: 1.9 $ $Date: 2010/09/29 17:21:48 $
 * @author <a href="mailto:anthony.goubard@japplis.com">Anthony Goubard</a>
 */
public class XINSCallConfigTests extends TestCase {

   /**
    * Constructs a new <code>XINSCallConfigTests</code> test suite with
    * the specified name. The name will be passed to the superconstructor.
    *
    * @param name
    *    the name for this test suite.
    */
   public XINSCallConfigTests(String name) {
      super(name);
   }

   /**
    * Returns a test suite with all test cases defined by this class.
    *
    * @return
    *    the test suite, never <code>null</code>.
    */
   public static Test suite() {
      return new TestSuite(XINSCallConfigTests.class);
   }

   public void testXINSCallConfig() throws Exception {

      // Test first constructor
      XINSCallConfig config = new XINSCallConfig();
      assertEquals("Incorrect HTTP method.", HTTPMethod.POST, config.getHTTPMethod());
      config.setHTTPMethod(HTTPMethod.GET);
      assertEquals("Incorrect HTTP method.", HTTPMethod.GET, config.getHTTPMethod());
      config.describe();
   }
}
