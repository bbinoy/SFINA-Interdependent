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
package experiments;

import InterdepMessage.InterdependentTopology;
import core.InterdependentAgent;
import core.SimulationAgentNew;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import power.backend.InterpssFlowDomainAgent;
import protopeer.Experiment;
import protopeer.Peer;
import protopeer.PeerFactory;
import protopeer.PeerIdentifier;
import protopeer.SimulatedExperiment;
import protopeer.network.NetworkAddress;
import protopeer.util.quantities.Time;

/**
 *
 * @author evangelospournaras
 */
public class TestInterdependent extends SimulatedExperiment{
    
    private static final Logger logger = Logger.getLogger(TestInterdependent.class);
    
    private final static String expSeqNum="01";
    
    private static String experimentID="experiment-"+expSeqNum;
    
    //Simulation Parameters
    private final static int bootstrapTime=2000;
    private final static int runTime=1000;
    private final static int runDuration=5;
    private final static int N=2; // Number of peerlets
    
    
    public static void main(String[] args) {
        Experiment.initEnvironment();
        TestInterdependent test = new TestInterdependent();
        test.init();
        
        PeerFactory peerFactory = new PeerFactory() {
            public Peer createPeer(int peerIndex, Experiment experiment) {
                // First network
                Peer peer = new Peer(peerIndex);
                peer.addPeerlet(new InterdependentAgent(
                        experimentID, 
                        Time.inMilliseconds(bootstrapTime),
                        Time.inMilliseconds(runTime)));
                peer.addPeerlet(new InterpssFlowDomainAgent(
                        experimentID, 
                        Time.inMilliseconds(bootstrapTime),
                        Time.inMilliseconds(runTime)));
                return peer;
            }
        };
        test.initPeers(0,N,peerFactory);
        test.startPeers(0,N);
        
        //distribute network addresses to all peers
        ArrayList<NetworkAddress> networkAddresses = new ArrayList();
        for(Peer peer : test.getPeers()){
            networkAddresses.add(peer.getNetworkAddress());
            
        }
        InterdependentTopology it = new InterdependentTopology(networkAddresses);
        logger.debug(it.getNetworkAddresses());
        for(Peer peer : test.getPeers()){
            InterdependentAgent simuAgent = (InterdependentAgent)peer.getPeerletOfType(InterdependentAgent.class);
            simuAgent.addTopology(it);
        }
        
        //run the simulation
        test.runSimulation(Time.inSeconds(runDuration));
    }
}
