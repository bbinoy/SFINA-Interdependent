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

import java.util.ArrayList;
import java.util.Collection;
import protopeer.network.NetworkAddress;

/**
 *
 * @author Ben
 */
public class InterdependentTopology{
    
    private ArrayList<NetworkAddress> networkAddresses;
    private ArrayList<InterLink> interLinks;
    
    public InterdependentTopology(ArrayList<NetworkAddress> networkAddresses){
        this.networkAddresses=networkAddresses;
    }

    /**
     * @return the networkAddresses
     */
    public Collection<NetworkAddress> getNetworkAddresses() {
        return networkAddresses;
    }

    /**
     * @param networkAddresses
     */
    public void setNetworkAddresses(ArrayList<NetworkAddress> networkAddresses) {
        this.networkAddresses = networkAddresses;
    }

    /**
     * @return the interLinks
     */
    public ArrayList<InterLink> getInterLinks() {
        return interLinks;
    }

    /**
     * @param interLinks the interLinks to set
     */
    public void setInterLinks(ArrayList<InterLink> interLinks) {
        this.interLinks = interLinks;
    }
    
}
