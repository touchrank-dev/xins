/*
 * $Id: FunctionResult.java,v 1.50 2013/01/18 09:06:48 agoubard Exp $
 *
 * See the COPYRIGHT file for redistribution and use restrictions.
 */
package org.xins.server;

import java.util.LinkedHashMap;
import java.util.Map;

import org.xins.common.MandatoryArgumentChecker;
import org.xins.common.collections.MapStringUtils;
import org.w3c.dom.Element;
import org.xins.common.xml.DataElementBuilder;

/**
 * Result from a function call. Defines an error code, parameters and output
 * data section. All are optional.
 *
 * @version $Revision: 1.50 $ $Date: 2013/01/18 09:06:48 $
 * @author <a href="mailto:anthony.goubard@japplis.com">Anthony Goubard</a>
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 *
 * @since XINS 1.0.0
 *
 * @see FunctionRequest
 */
public class FunctionResult {

   /**
    * The result code. This field is <code>null</code> if no code was
    * returned.
    */
   private final String _code;

   /**
    * The parameters and their values. This field is never <code>null</code>.
    */
   private final Map<String, String> _parameters;

   /**
    * The data element builder. This field is lazily initialized, it is
    * <code>null</code> if there is no data element.
    */
   private DataElementBuilder _dataElementBuilder;

   /**
    * Creates a new successful <code>FunctionResult</code> instance with no
    * parameters.
    */
   public FunctionResult() {
      this(null, null);
   }

   /**
    * Creates a new <code>FunctionResult</code> instance with no parameters.
    *
    * @param code
    *    the error code, can be <code>null</code> if the result is successful.
    */
   public FunctionResult(String code) {
      this(code, null);
   }

   /**
    * Creates a new <code>FunctionResult</code> instance with a specified set
    * of parameters.
    *
    * @param code
    *    the error code, can be <code>null</code> if the result is successful.
    *
    * @param parameters
    *    the parameters for the result, can be <code>null</code> if there are
    *    no parameters.
    */
   public FunctionResult(String code, Map<String, String> parameters) {
      _code = code;
      if (parameters == null) {
          _parameters = new LinkedHashMap<String, String>();
      } else {
        _parameters = parameters;
      }
   }

   /**
    * Returns the result code.
    *
    * @return
    *    the result code or <code>null</code> if no code was returned.
    */
   public String getErrorCode() {
      return _code;
   }

   /**
    * Checks that the output parameters are set as specified. If a parameter
    * is missing or if the value for it is invalid, then an
    * <code>InvalidResponseResult</code> is returned. Otherwise the parameters
    * are considered valid, and <code>null</code> is returned.
    *
    * <p>The implementation of this method in class {@link FunctionResult}
    * always returns <code>null</code>.
    *
    * @return
    *    an {@link InvalidResponseResult} instance if at least one output
    *    parameter is missing or invalid, or <code>null</code> otherwise.
    *
    * @since XINS 2.0.
    */
   public InvalidResponseResult checkOutputParameters() {
      return null;
   }

   /**
    * Adds an output parameter to the result. The name and the value must
    * both be specified.
    *
    * @param name
    *    the name of the output parameter, not <code>null</code> and not an
    *    empty string.
    *
    * @param value
    *    the value of the output parameter, not <code>null</code> and not an
    *    empty string.
    *
    * @throws IllegalArgumentException
    *    if <code>name  == null || "".equals(name)
    *          || value == null || "".equals(value)</code>.
    */
   protected void param(String name, String value) throws IllegalArgumentException {

      // Check preconditions
      MandatoryArgumentChecker.check("name", name, "value", value);
      if (name.length() < 1) {
         throw new IllegalArgumentException("\"\".equals(name)");
      } else if (value.length() < 1) {
         throw new IllegalArgumentException("\"\".equals(value)");
      }

      // This will erase any value set before with the same name.
      _parameters.put(name, value);
   }

   /**
    * Gets all parameters.
    *
    * @return
    *    a {@link Map} containing all parameters, never <code>null</code>;
    *    the keys will be the names of the parameters
    *    ({@link String} objects, cannot be <code>null</code>),
    *    the values will be the parameter values
    *    ({@link String} objects as well, cannot be <code>null</code>).
    */
   public Map<String, String> getParameters() {
      return _parameters;
   }

   /**
    * Gets the value of the specified parameter.
    *
    * @param name
    *    the parameter element name, cannot be <code>null</code>.
    *
    * @return
    *    string containing the value of the parameter element,
    *    or <code>null</code> if the value is not set.
    *
    * @throws IllegalArgumentException
    *    if <code>name == null</code>.
    */
   public String getParameter(String name) throws IllegalArgumentException {

      // Check preconditions
      MandatoryArgumentChecker.check("name", name);

      return _parameters.get(name);
   }

   /**
    * Adds a new <code>Element</code> to the data element.
    *
    * @param element
    *    the new element to add to the result, cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>element == null</code>.
    *
    * @since XINS 1.1.0
    * @deprecated Use getDataElementBuilder()
    */
   protected void add(Element element) throws IllegalArgumentException {

      // Check preconditions
      MandatoryArgumentChecker.check("element", element);

      Element elementToAdd = (Element) getDataElementBuilder().getDocument().importNode(element, true);
      _dataElementBuilder.getDataElement().appendChild(elementToAdd);
   }

   /**
    * Adds the <code>DataElementBuilder</code> to create a new the data element.
    *
    * @return
    *    a <code>DataElementBuilder</code> to create the data element.
    * @since XINS 3.0.0
    */
   protected DataElementBuilder getDataElementBuilder() {

      // Lazily initialize _dataElementBuilder
      if (_dataElementBuilder == null) {
         _dataElementBuilder = new DataElementBuilder();
      }
      return _dataElementBuilder;
   }

   /**
    * Gets the data element from this result.
    *
    * @return
    *    the data element of the result, can be <code>null</code>.
    */
   public Element getDataElement() {
      if (_dataElementBuilder == null) {
         return null;
      } else {
         return _dataElementBuilder.getDataElement();
      }
   }

   public String toString() {
      String asString = "";
      if (_code != null) {
         asString += "Error code: " + _code + "; ";
      } else {
         asString += "Successful result; ";
      }
      asString += MapStringUtils.toString(_parameters, "no parameters") + "; ";
      if (_dataElementBuilder == null) {
         asString += "no data section";
      } else {
         asString += _dataElementBuilder.toString();
      }
      return asString;
   }
}
