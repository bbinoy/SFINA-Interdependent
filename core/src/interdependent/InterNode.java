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

import network.Node;
import protopeer.network.NetworkAddress;

/**
 * In addition to Node contains Protopeer NetworkAddress it belongs to.
 * @author Ben
 */
public class InterNode extends Node{
    
    private NetworkAddress netAddress;

    public InterNode(String index, boolean activated, NetworkAddress netAddress) {
        super(index, activated);
        this.netAddress=netAddress;
    }

    /**
     * @return the netAddress
     */
    public NetworkAddress getNetAddress() {
        return netAddress;
    }

    /**
     * @param netAddress the netAddress to set
     */
    public void setNetAddress(NetworkAddress netAddress) {
        this.netAddress = netAddress;
    }
    
}
