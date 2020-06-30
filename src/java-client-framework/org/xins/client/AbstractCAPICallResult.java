/*
 * $Id: AbstractCAPICallResult.java,v 1.26 2013/01/22 15:13:22 agoubard Exp $
 *
 * See the COPYRIGHT file for redistribution and use restrictions.
 */
package org.xins.client;

import java.io.Serializable;
import java.util.List;

import org.xins.common.MandatoryArgumentChecker;
import org.xins.common.service.CallException;
import org.xins.common.service.TargetDescriptor;

/**
 * Base class for generated CAPI function result classes.
 *
 * <p>This class should not be subclassed manually. It is only intended to be
 * subclassed by classes generated by XINS.
 *
 * @version $Revision: 1.26 $ $Date: 2013/01/22 15:13:22 $
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 *
 * @since XINS 1.0.0
 */
public abstract class AbstractCAPICallResult implements Serializable {

   /**
    * The XINS call result. This field cannot be <code>null</code>.
    */
   private XINSCallResult _result;

   /**
    * Creates a new <code>AbstractCAPICallResult</code> object, based on the
    * specified <code>XINSCallResult</code>.
    *
    * @param result
    *    the lower-level {@link XINSCallResult}, cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>result == null</code>.
    */
   protected AbstractCAPICallResult(XINSCallResult result)
   throws IllegalArgumentException {

      // Check preconditions
      MandatoryArgumentChecker.check("result", result);

      // Store field
      _result = result;

      // Check preconditions
      if (!result.isNotModified() && result.getErrorCode() != null) {
         throw new IllegalArgumentException("result.getErrorCode() != null");
      }
   }

   /**
    * Returns the underlying XINS call result.
    *
    * @return
    *    the underlying {@link XINSCallResult} object, never
    *    <code>null</code>.
    */
   XINSCallResult getXINSCallResult() {
      return _result;
   }

   /**
    * Returns the target for which the call succeeded.
    *
    * @return
    *    the {@link TargetDescriptor} for which the call succeeded, not
    *    <code>null</code>.
    *
    * @since XINS 1.1.0
    */
   public final TargetDescriptor succeededTarget() {
      return _result.getSucceededTarget();
   }

   /**
    * Returns the call duration, in milliseconds.
    *
    * @return
    *    the duration of the succeeded call, in milliseconds, guaranteed to
    *    be a non-negative number.
    *
    * @since XINS 1.1.0
    */
   public final long duration() {
      return _result.getDuration();
   }

   /**
    * Returns the list of <code>CallException</code>s.
    *
    * @return
    *    the {@link org.xins.common.service.CallException}s,
    *    collected in a {@link CallExceptionList} object,
    *    or <code>null</code> if the first call attempt succeeded.
    *
    * @since XINS 1.1.0
    */
   public final List<CallException> exceptions() {
      return _result.getExceptions();
   }
}
