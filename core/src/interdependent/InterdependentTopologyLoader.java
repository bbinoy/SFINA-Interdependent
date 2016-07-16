/*
 * Copyright (C) 2016 SFINA Team
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
package interdependent;

import input.TopologyLoader;
import network.Node;
import org.apache.log4j.Logger;
import protopeer.network.NetworkAddress;

/**
 *
 * @author Ben
 */
public class InterdependentTopologyLoader extends TopologyLoader{
    private static final Logger logger = Logger.getLogger(InterdependentTopologyLoader.class);
    
    /**
     *
     * @param interTopo
     * @param columnSeparator
     */
    public InterdependentTopologyLoader(InterdependentNetwork interTopo, String columnSeparator){
        super(interTopo, columnSeparator);
    }
    
    @Override
    public void loadNodes(String location){
        logger.debug("Method loadNodes doesn't make sense for interdependent network.");
    }
    
    /**
     * Currently just initializes Multiplex network 
     * i.e. 1-to-1 correspondance of nodes
     * @param location
     */
    @Override
    public void loadLinks(String location){
        int maxLinkId = 0;
        for(NetworkAddress address1 : this.getInterdependentNetwork().getNetworkAddresses()){
            for(NetworkAddress address2 : this.getInterdependentNetwork().getNetworkAddresses()){
                if(address1 != address2){
//                    FlowNetwork net1 = this.nets.get(address1);
//                    FlowNetwork net2 = this.nets.get(address2);
                    for (Node node1 : this.getInterdependentNetwork().getInterdependentNetworks().get(address1).getNodes()){
                        for (Node node2 : this.getInterdependentNetwork().getInterdependentNetworks().get(address2).getNodes()){
                            if(node1.getIndex().equals(node2.getIndex())){
                                maxLinkId += 1;
                                InterLink newLink = new InterLink(String.valueOf(maxLinkId), true, node1, node2, address1, address2);
                                this.getInterdependentNetwork().addLink(newLink);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public InterdependentNetwork getInterdependentNetwork(){
        return (InterdependentNetwork)this.getFlowNetwork();
    }
}
