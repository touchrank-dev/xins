/*
 * $Id: CallXINSTask.java,v 1.18 2012/03/15 21:07:39 agoubard Exp $
 *
 * See the COPYRIGHT file for redistribution and use restrictions.
 */
package org.xins.common.ant;

import java.util.List;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Property;
import org.w3c.dom.Attr;

import org.xins.common.text.URLEncoding;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xins.common.xml.ElementList;

/**
 * Apache Ant task that call a function of a XINS API, the result is stored
 * in Ant properties.
 *
 * @version $Revision: 1.18 $ $Date: 2012/03/15 21:07:39 $
 * @author <a href="mailto:anthony.goubard@japplis.com">Anthony Goubard</a>
 *
 * @since XINS 1.5.0
 */
public class CallXINSTask extends Task {

   /**
    * The function to call.
    */
   private String _function;

   /**
    * The URL location of the API to call.
    */
   private String _apiLocation;

   /**
    * The prefix for the result properties.
    */
   private String _prefix;

   /**
    * The parameters to the function.
    */
   private Vector _params = new Vector();

   /**
    * Constructs a new <code>CallTargetsTask</code> instance.
    */
   public CallXINSTask() {
      // empty
   }

   /**
    * Sets the name of the API function to call.
    *
    * @param function
    *    the name of the function to call, can be <code>null</code>.
    */
   public void setFunction(String function) {
      _function = function;
   }

   /**
    * Sets the URL location of the API.
    *
    * @param apiLocation
    *    the URL location of the API without any parameters, cannot be <code>null</code>.
    */
   public void setApiLocation(String apiLocation) {
      _apiLocation = apiLocation;
   }

   /**
    * Sets the prefix for the result properties.
    *
    * @param prefix
    *    the prefix for the result properties.
    */
   public void setPrefix(String prefix) {
      _prefix = prefix;
   }

   /**
    * Adds an input parameter.
    *
    * @return
    *    the input parameters.
    */
   public Property createParam() {
      Property property = new Property();
      _params.add(property);
      return property;
   }

   /**
    * Called by the project to let the task do its work.
    *
    * @throws BuildException
    *    if something goes wrong with the build.
    */
   public void execute() throws BuildException {
      checkAttributes();
      try {

         // Create the URL
         StringBuffer sbRequestURL = new StringBuffer(_apiLocation + "?_convention=_xins-std&_function=" + _function);
         for (int i = 0; i < _params.size(); i++) {
             Property nextParam = (Property) _params.elementAt(i);
             String paramName = nextParam.getName();
             String paramValue = nextParam.getValue();
             sbRequestURL.append("&" + URLEncoding.encode(paramName) + "=" + URLEncoding.encode(paramValue));
         }
         String requestURL = sbRequestURL.toString();

         // Call the API
         Element resultXML = CreateExampleTask.getResultAsXML(requestURL);
         log("result: " + resultXML.toString(), Project.MSG_VERBOSE);

         // Put the output parameters in ANT properties
         //NodeList outputParams = resultXML.getElementsByTagName("param");
         ElementList outputParams = new ElementList(resultXML, "param");
         for (int i = 0; i < outputParams.size(); i++) {
            Element nextParam = (Element) outputParams.get(i);
            String paramName = nextParam.getAttribute("name");
            String paramValue = nextParam.getTextContent();
            if (_prefix != null && _prefix.length() > 0) {
                paramName = _prefix + "." + paramName;
            }
            getProject().setNewProperty(paramName, paramValue);
         }

         // Put the output data section in ANT properties
         // With Ant 1.6 XmlProperty task cannot be used.
         ElementList outputData = new ElementList(resultXML, "data");
         if (!outputData.isEmpty()) {
             Element dataSection = outputData.get(0);
             elementToProperties(dataSection, _prefix);
         }
      } catch (Exception ex) {
         throw new BuildException(ex);
      }
   }

   /**
    * Store the element values in Ant properties.
    *
    * @param element
    *    the data section element, cannot be <code>null</code>.
    * @param prefix
    *    the prefix for the Ant properties, can be <code>null</code>.
    */
   private void elementToProperties(Element element, String prefix) {
      String localName = element.getTagName();
      String elementPrefix;
      if (prefix != null && prefix.length() > 0) {
         elementPrefix = prefix + "." + localName;
      } else {
         elementPrefix = localName;
      }
      NamedNodeMap attributes = element.getAttributes();
      for (int i = 0; i < attributes.getLength(); i++) {
         Attr attribute = (Attr) attributes.item(i);
         String attributeName = attribute.getName();
         String attributeValue = attribute.getValue();
         String propertyName = elementPrefix + "." + attributeName;
         getProject().setNewProperty(propertyName, attributeValue);
      }
      String pcdata = element.getTextContent();
      if (pcdata != null && pcdata.length() > 0) {
         getProject().setNewProperty(elementPrefix, pcdata);
      }
      for (Element nextChild : new ElementList(element)) {
         elementToProperties(nextChild, elementPrefix);
      }
   }

   /**
    * Checks the attributes of the task.
    *
    * @throws BuildException
    *    if a required attribute is missing.
    */
   private void checkAttributes() throws BuildException {

      if (_function == null) {
         throw new BuildException("The \"function\" attribute needs to be specified.");
      }

      if (_apiLocation == null) {
         throw new BuildException("An \"exampleProperty\" attribute needs to be specified.");
      }
   }
}
