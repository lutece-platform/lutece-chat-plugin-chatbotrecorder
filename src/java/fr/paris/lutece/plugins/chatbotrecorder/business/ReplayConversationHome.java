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
 * This class provides instances management methods (create, find, ...) for ReplayConversation objects
 */
public final class ReplayConversationHome
{
    // Static variable pointed at the DAO instance
    private static IReplayConversationDAO _dao = SpringContextService.getBean( "chatbotrecorder.replayConversationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "chatbotrecorder" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ReplayConversationHome( )
    {
    }

    /**
     * Create an instance of the replayConversation class
     * 
     * @param replayConversation
     *            The instance of the ReplayConversation which contains the informations to store
     * @return The instance of replayConversation which has been created with its primary key.
     */
    public static ReplayConversation create( ReplayConversation replayConversation )
    {
        _dao.insert( replayConversation, _plugin );
        return replayConversation;
    }

    /**
     * Create an instance of the replayConversation class
     * 
     * @param replayConversation
     *            a list of a replay conversations
     * @return the replayConversation list.
     */
    public static List<ReplayConversation> create( List<ReplayConversation> replayConversationList, int nIdReplay )
    {
        for ( ReplayConversation replayConversation : replayConversationList )
        {
            replayConversation.setIdReplay( nIdReplay );
            create( replayConversation );
        }
        return replayConversationList;
    }

    /**
     * Update of the replayConversation which is specified in parameter
     * 
     * @param replayConversation
     *            The instance of the ReplayConversation which contains the data to store
     * @return The instance of the replayConversation which has been updated
     */
    public static ReplayConversation update( ReplayConversation replayConversation )
    {
        _dao.store( replayConversation, _plugin );
        return replayConversation;
    }

    /**
     * Remove the replayConversation whose identifier is specified in parameter
     * 
     * @param nKey
     *            The replayConversation Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a replayConversation whose identifier is specified in parameter
     * 
     * @param nKey
     *            The replayConversation primary key
     * @return an instance of ReplayConversation
     */
    public static ReplayConversation findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the replayConversation objects and returns them as a list
     * 
     * @return the list which contains the data of all the replayConversation objects
     */
    public static List<ReplayConversation> getReplayConversationsList( )
    {
        return _dao.selectReplayConversationsList( _plugin );
    }

    /**
     * Load the data of all the replayConversation objects and returns them as a list
     * 
     * @return the list which contains the data of all the replayConversation objects
     */
    public static List<ReplayConversation> getReplayConversationsListByReplay( int idReplay )
    {
        return _dao.selectReplayConversationsListByReplay( idReplay, _plugin );
    }

    /**
     * Load the id of all the replayConversation objects and returns them as a list
     * 
     * @return the list which contains the id of all the replayConversation objects
     */
    public static List<Integer> getIdReplayConversationsList( )
    {
        return _dao.selectIdReplayConversationsList( _plugin );
    }

    /**
     * Load the data of all the replayConversation objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the replayConversation objects
     */
    public static ReferenceList getReplayConversationsReferenceList( )
    {
        return _dao.selectReplayConversationsReferenceList( _plugin );
    }
}
