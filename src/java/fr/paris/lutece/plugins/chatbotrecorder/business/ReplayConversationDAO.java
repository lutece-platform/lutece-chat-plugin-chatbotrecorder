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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for ReplayConversation objects
 */
public final class ReplayConversationDAO implements IReplayConversationDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_replay_conversation, user_msg, bot_response, status, id_replay FROM chatbotrecorder_replay_conversation WHERE id_replay_conversation = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO chatbotrecorder_replay_conversation ( user_msg, bot_response, status, id_replay ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM chatbotrecorder_replay_conversation WHERE id_replay_conversation = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE chatbotrecorder_replay_conversation SET id_replay_conversation = ?, user_msg = ?, bot_response = ?, status = ?, id_replay = ? WHERE id_replay_conversation = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_replay_conversation, user_msg, bot_response, status, id_replay FROM chatbotrecorder_replay_conversation";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_replay_conversation FROM chatbotrecorder_replay_conversation";
    private static final String SQL_QUERY_SELECTALL_IDREPLAY = "SELECT id_replay_conversation, user_msg, bot_response, status, id_replay FROM chatbotrecorder_replay_conversation WHERE id_replay = ? ORDER BY id_replay_conversation";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( ReplayConversation replayConversation, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, replayConversation.getUserMsg( ) );
            daoUtil.setString( nIndex++, replayConversation.getBotResponse( ) );
            daoUtil.setInt( nIndex++, replayConversation.getStatus( ) );
            daoUtil.setInt( nIndex++, replayConversation.getIdReplay( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                replayConversation.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReplayConversation load( int nKey, Plugin plugin )
    {
        ReplayConversation replayConversation = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                replayConversation = new ReplayConversation( );
                int nIndex = 1;
                replayConversation.setId( daoUtil.getInt( nIndex++ ) );
                replayConversation.setUserMsg( daoUtil.getString( nIndex++ ) );
                replayConversation.setBotResponse( daoUtil.getString( nIndex++ ) );
                replayConversation.setStatus( daoUtil.getInt( nIndex++ ) );
                replayConversation.setIdReplay( daoUtil.getInt( nIndex++ ) );
            }
        }
        return replayConversation;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ReplayConversation> selectReplayConversationsListByReplay( int nIdReplay, Plugin plugin )
    {
        List<ReplayConversation> replayConversationList = new ArrayList<ReplayConversation>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_IDREPLAY, plugin ) )
        {
            daoUtil.setInt( 1, nIdReplay );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                ReplayConversation replayConversation = new ReplayConversation( );
                int nIndex = 1;
                replayConversation.setId( daoUtil.getInt( nIndex++ ) );
                replayConversation.setUserMsg( daoUtil.getString( nIndex++ ) );
                replayConversation.setBotResponse( daoUtil.getString( nIndex++ ) );
                replayConversation.setStatus( daoUtil.getInt( nIndex++ ) );
                replayConversation.setIdReplay( daoUtil.getInt( nIndex++ ) );
                replayConversationList.add( replayConversation );
            }
        }
        return replayConversationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( ReplayConversation replayConversation, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, replayConversation.getId( ) );
            daoUtil.setString( nIndex++, replayConversation.getUserMsg( ) );
            daoUtil.setString( nIndex++, replayConversation.getBotResponse( ) );
            daoUtil.setInt( nIndex++, replayConversation.getStatus( ) );
            daoUtil.setInt( nIndex++, replayConversation.getIdReplay( ) );
            daoUtil.setInt( nIndex, replayConversation.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ReplayConversation> selectReplayConversationsList( Plugin plugin )
    {
        List<ReplayConversation> replayConversationList = new ArrayList<ReplayConversation>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                ReplayConversation replayConversation = new ReplayConversation( );
                int nIndex = 1;
                replayConversation.setId( daoUtil.getInt( nIndex++ ) );
                replayConversation.setUserMsg( daoUtil.getString( nIndex++ ) );
                replayConversation.setBotResponse( daoUtil.getString( nIndex++ ) );
                replayConversation.setStatus( daoUtil.getInt( nIndex++ ) );
                replayConversation.setIdReplay( daoUtil.getInt( nIndex++ ) );
                replayConversationList.add( replayConversation );
            }
        }
        return replayConversationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdReplayConversationsList( Plugin plugin )
    {
        List<Integer> replayConversationList = new ArrayList<Integer>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                replayConversationList.add( daoUtil.getInt( 1 ) );
            }
        }
        return replayConversationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectReplayConversationsReferenceList( Plugin plugin )
    {
        ReferenceList replayConversationList = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                replayConversationList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return replayConversationList;
    }
}
