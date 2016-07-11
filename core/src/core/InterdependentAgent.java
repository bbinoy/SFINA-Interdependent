/*
 * Copyright (C) 2015 SFINA Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package core;

import InterdepMessage.InterdependentTopology;
import InterdepMessage.StringMessage;
import org.apache.log4j.Logger;
import java.util.logging.Level;
import protopeer.network.Message;
import protopeer.network.NetworkAddress;
import protopeer.util.quantities.Time;

/**
 *
 * @author evangelospournaras
 */
public class InterdependentAgent extends SimulationAgentNew{
    
    private static final Logger logger = Logger.getLogger(InterdependentAgent.class);
    
    private InterdependentTopology interTopology;
    
    public InterdependentAgent(
            String experimentID,
            Time bootstrapTime, 
            Time runTime){
        super(experimentID, bootstrapTime, runTime);
    }
    
    public void addTopology(InterdependentTopology interdepTop){
        this.interTopology=interdepTop;
    }
    
    @Override
    public void handleIncomingMessage(Message message) {
        StringMessage stringMessage = (StringMessage)message;
        logger.debug("##############################");
        logger.debug("Incoming message at Peer " + this.getPeer().getIndexNumber());
        logger.debug("Time: " + this.getPeer().getClock().getCurrentTime());
        logger.debug("Message: " + stringMessage.getMessage());
        logger.debug("##############################");
        if (this.getPeer().getIndexNumber() == 0)
            this.getPeer().notify();
        else
            sendTestMessage();
    }

    @Override
    public void handleOutgoingMessage(Message message) {
        StringMessage stringMessage = (StringMessage)message;
        logger.debug("##############################");
        logger.debug("Outgoing message at Peer " + this.getPeer().getIndexNumber());
        logger.debug("Message: " + stringMessage.getMessage());
        logger.debug("##############################");   
        try {
            this.getPeer().wait();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(InterdependentAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendTestMessage(){
        logger.debug("Addresses: " + interTopology.getNetworkAddresses());
        StringMessage message = new StringMessage("Hello World says Peer " + getPeer().getIndexNumber());
        for (NetworkAddress address : interTopology.getNetworkAddresses())
            if (!address.equals(getPeer().getNetworkAddress()))
                getPeer().sendMessage(address, message);
    }
    
    @Override
    public void runInitialOperations(){
        if (getPeer().getIndexNumber() == 0){
            sendTestMessage();
        }
    }
}