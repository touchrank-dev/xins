/*
 * $Id: DataSection3Impl.java,v 1.3 2010/11/18 20:35:05 agoubard Exp $
 */
package com.mycompany.allinone.api;

import java.util.Iterator;

/**
 * Implementation of the <code>DataSection3</code> function.
 *
 * @version $Revision: 1.3 $ $Date: 2010/11/18 20:35:05 $
 * @author John Doe (<a href="mailto:john.doe@mycompany.com">john.doe@mycompany.com</a>)
 */
public class DataSection3Impl extends DataSection3  {

   /**
    * Constructs a new <code>DataSection3Impl</code> instance.
    *
    * @param api
    *    the API to which this function belongs, guaranteed to be not
    *    <code>null</code>.
    */
   public DataSection3Impl(APIImpl api) {
      super(api);
   }

   public final Result call(Request request) throws Throwable {
      SuccessfulResult result = new SuccessfulResult();

      Iterator itAddresses = request.listAddress().iterator();
      while (itAddresses.hasNext()) {
         Request.Address nextAddress = (Request.Address) itAddresses.next();
         Envelope envelope = new Envelope();
         envelope.setDestination(nextAddress.getPostcode());
         result.addEnvelope(envelope);
      }

      // Create the packet
      Packet packet = new Packet();
      packet.setDestination("20 West Street, New York");

      Envelope envelope = new Envelope();
      envelope.setDestination("55 Kennedy lane, Washinton DC");

      // Add the packets
      result.addPacket(packet);
      result.addEnvelope(envelope);

      return result;
   }
}
