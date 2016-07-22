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

import input.FlowNetworkDataTypesInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import network.FlowNetwork;
import network.Link;
import network.Node;
import org.apache.log4j.Logger;
import protopeer.network.NetworkAddress;

/**
 *
 * @author Ben
 */
public class InterdependentNetwork extends FlowNetwork{
    
    private static final Logger logger = Logger.getLogger(InterdependentNetwork.class);
    
    private int N; // Number of interconnected networks (i.e. peers)
    private HashMap<NetworkAddress, FlowNetwork> nets;
    private InterdependentTopologyLoader interTopoLoader;
    private InterdependentFlowLoader interFlowLoader;

    public InterdependentNetwork(int N){
        this.N=N;
        this.nets=new HashMap<>();
    }
    
    /**
     * @return the Number of Interdependent Networks
     */
    public int getNumberOfNets() {
        return N;
    }

    /**
     * @return the networkAddresses
     */
    public Collection<NetworkAddress> getNetworkAddresses() {
        return nets.keySet();
    }
    
    /**
     * @return the nets
     */
    public HashMap<NetworkAddress, FlowNetwork> getInterdependentNetworks() {
        return nets;
    }
    
    /**
     * Adding the FlowNetwork to the InterdependentNetwork.
     * If it wasn't added already.
     * If it is the last one which was added, the method to create interdependent
     * links between all the networks is called.
     * @param net
     * @param address
     * @param interdependentTopologyLocation
     * @param interdependentFlowLocation
     * @param columnSeparator
     * @param missingValue
     * @param dataTypesInterface
     */
    public void updateTopology(FlowNetwork net, NetworkAddress address, String interdependentTopologyLocation, String interdependentFlowLocation, String columnSeparator, String missingValue, FlowNetworkDataTypesInterface dataTypesInterface) {
        logger.info("InterdependentNetwork: adding FlowNetwork at NetworkAddress " + address);    
        this.getInterdependentNetworks().put(address, net);
        
        if (this.getInterdependentNetworks().keySet().size() == N) {
            this.interTopoLoader = new InterdependentTopologyLoader(this, columnSeparator);
            this.interFlowLoader = new InterdependentFlowLoader(this, columnSeparator, missingValue, dataTypesInterface);
            
            // Currently this just loads a multiplex network (i.e. 1-to-1 correspondance of nodes)
            // Todo: Implement loaders for InterLinks
            interTopoLoader.loadLinks(interdependentTopologyLocation);
            interFlowLoader.loadLinkFlowData(interdependentFlowLocation);
            
            // Don't need nodes for interdependent net
//            interTopoLoader.loadNodes(null);
//            interFlowLoader.loadNodeFlowData(null);
        }
    }

    
    public Collection<Node> getTargetNodes(String fromNodeId, NetworkAddress fromAddress, NetworkAddress toAddress) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (InterLink link : this.getInterLinks()) {
            if (link.getStartNode().getIndex().equals(fromNodeId) && link.getStartNodeAddress().equals(fromAddress) && link.getEndNodeAddress().equals(toAddress)) {
                nodes.add(link.getEndNode());
            }
        }
        return nodes;
    }
    
    /**
     * @return the interLinks
     */
    public Collection<InterLink> getInterLinks() {
        Collection<Link> before = this.getLinks();
        ArrayList<InterLink> after = new ArrayList<>();
        for (Link link : before){
            if(link instanceof InterLink)
                after.add((InterLink)link);
            else
                throw new ClassCastException("Link in InterdependentTopology can't be cast to InterLink.");
        }
        return (Collection<InterLink>)after;
    }
    
    /**
     *
     * @param index
     * @return
     */
    public InterLink getInterLink(String index){
        Link link = this.getLink(index);
        if(link instanceof InterLink)
            return (InterLink)link;
        else 
            throw new ClassCastException("Link in InterdependentTopology can't be cast to InterLink.");
    }

    @Override
    public String toString() {
        String string = "";
        string += "---- Interlinks ----\n";
        string += String.format("%-10s%-20s%-20s\n", "", "StartNode", "EndNode");
        string += String.format("%-10s%-10s%-10s%-10s%-10s\n", "Link ID", "ID", "Address", "ID", "Address");
        for (InterLink link : this.getInterLinks()) {
            string += String.format("%-10s%-10s%-10s%-10s%-10s\n", link.getIndex(), link.getStartNode().getIndex(), link.getStartNodeAddress(), link.getEndNode().getIndex(), link.getEndNodeAddress());
        }
        return string += "--------------------";
    }

    public boolean checkInterdependentTopology(boolean logDetails){
        boolean intact = true;
        if(logDetails){
            logger.debug("### Checking Interdependent Topology ###");
            logger.debug("Number of interlinks: " + this.getInterLinks().size());
            logger.debug("Number of involved networks: " + this.getNumberOfNets());
            logger.debug("Network Addresses: " + this.getNetworkAddresses());
            logger.debug("Analyzing links...");
        }
        for(NetworkAddress a : this.getNetworkAddresses())
            if(!this.getNetworkAddresses().contains(a)){
                intact = false;
                logger.debug("! NetworkAddress of FlowNetwork not in NetworkAddress list.");
            }
        for (InterLink link : this.getInterLinks()) {
            if(logDetails){
                logger.debug("Link StartNode: Index = " + link.getStartNode().getIndex() + ", Address = " + link.getStartNodeAddress() + ", contained in net = " + this.getInterdependentNetworks().get(link.getStartNodeAddress()).getNodes().contains(link.getStartNode()));
                logger.debug("Link EndNode: Index = " + link.getEndNode().getIndex() + ", Address = " + link.getEndNodeAddress() + ", contained in net = " + this.getInterdependentNetworks().get(link.getEndNodeAddress()).getNodes().contains(link.getEndNode()));
            }
            if(!this.getInterdependentNetworks().get(link.getStartNodeAddress()).getNodes().contains(link.getStartNode())){
                intact = false;
                logger.debug("! StartNode of InterLink not in corresponding FlowNetwork.");
            }
            if(!this.getInterdependentNetworks().get(link.getEndNodeAddress()).getNodes().contains(link.getEndNode())){
                intact = false;
                logger.debug("! EndNode of InterLink not in corresponding FlowNetwork.");
            }
        }
        logger.debug("-----> " + intact);
        return intact;
    }

}
