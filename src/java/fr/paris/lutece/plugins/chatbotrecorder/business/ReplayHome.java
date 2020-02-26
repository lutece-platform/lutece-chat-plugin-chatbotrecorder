/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.chatbotrecorder.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Replay objects
 */
public final class ReplayHome
{
    // Static variable pointed at the DAO instance
    private static IReplayDAO _dao = SpringContextService.getBean( "chatbotrecorder.replayDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "chatbotrecorder" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ReplayHome( )
    {
    }

    /**
     * Create an instance of the replay class
     * 
     * @param replay
     *            The instance of the Replay which contains the informations to store
     * @return The instance of replay which has been created with its primary key.
     */
    public static Replay create( Replay replay )
    {
        _dao.insert( replay, _plugin );
        return replay;
    }

    /**
     * Update of the replay which is specified in parameter
     * 
     * @param replay
     *            The instance of the Replay which contains the data to store
     * @return The instance of the replay which has been updated
     */
    public static Replay update( Replay replay )
    {
        _dao.store( replay, _plugin );
        return replay;
    }

    /**
     * Remove the replay whose identifier is specified in parameter
     * 
     * @param nKey
     *            The replay Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a replay whose identifier is specified in parameter
     * 
     * @param nKey
     *            The replay primary key
     * @return an instance of Replay
     */
    public static Replay findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the replay objects and returns them as a list
     * 
     * @return the list which contains the data of all the replay objects
     */
    public static List<Replay> getReplaysList( )
    {
        return _dao.selectReplaysList( _plugin );
    }

    /**
     * Load the id of all the replay objects and returns them as a list
     * 
     * @return the list which contains the id of all the replay objects
     */
    public static List<Integer> getIdReplaysList( )
    {
        return _dao.selectIdReplaysList( _plugin );
    }

    /**
     * Load the data of all the replay objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the replay objects
     */
    public static ReferenceList getReplaysReferenceList( )
    {
        return _dao.selectReplaysReferenceList( _plugin );
    }

    /**
     * Load the data of all the replay objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the replay objects
     */
    public static List<Replay> getReplaysListByScenario( int nIdScenario )
    {
        return _dao.selectReplaysListByScenario( nIdScenario, _plugin );
    }
}
