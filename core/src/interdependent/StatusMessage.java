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

import org.apache.log4j.Logger;
import protopeer.network.Message;

/**
 *
 * @author Ben
 */
public class StatusMessage extends Message{
    private static final Logger logger = Logger.getLogger(StatusMessage.class);
    private boolean iterationFinished;
    private int iteration;
    
    public StatusMessage(boolean isIterationFinished, int iteration){
        this.iterationFinished = isIterationFinished;
        this.iteration = iteration;
    }

    /**
     * @return the iterationFinished
     */
    public boolean isIterationFinished() {
        return iterationFinished;
    }

    /**
     * @return the iteration
     */
    public int getIteration() {
        return iteration;
    }

}
