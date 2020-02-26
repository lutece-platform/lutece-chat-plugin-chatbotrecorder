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
 * This class provides Data Access methods for Conversation objects
 */
public final class ConversationDAO implements IConversationDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_conversation, user_msg, bot_response, id_scenario, version FROM chatbotrecorder_conversation WHERE id_conversation = ?";
    private static final String SQL_QUERY_SELECTALL_VERSION = "SELECT id_conversation, user_msg, bot_response, id_scenario, version FROM chatbotrecorder_conversation WHERE id_scenario = ? AND version = ? ORDER BY id_conversation ASC";
    private static final String SQL_QUERY_INSERT = "INSERT INTO chatbotrecorder_conversation ( user_msg, bot_response, id_scenario, version ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM chatbotrecorder_conversation WHERE id_conversation = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE chatbotrecorder_conversation SET id_conversation = ?, user_msg = ?, bot_response = ?, id_scenario = ?, version = ? WHERE id_conversation = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_conversation, user_msg, bot_response, id_scenario, version FROM chatbotrecorder_conversation";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_conversation FROM chatbotrecorder_conversation";
    private static final String SQL_QUERY_SELECTALL_IDSCENARIO = "SELECT id_conversation, user_msg, bot_response, id_scenario, version FROM chatbotrecorder_conversation WHERE id_scenario = ? ORDER BY id_conversation ASC";
    private static final String SQL_QUERY_SELECT_VERSIONS = "SELECT DISTINCT version FROM chatbotrecorder_conversation WHERE id_scenario = ? ORDER BY id_conversation ASC";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Conversation conversation, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, conversation.getUserMsg( ) );
            daoUtil.setString( nIndex++, conversation.getBotResponse( ) );
            daoUtil.setInt( nIndex++, conversation.getIdScenario( ) );
            daoUtil.setInt( nIndex++, conversation.getVersion( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                conversation.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Conversation load( int nKey, Plugin plugin )
    {
        Conversation conversation = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                conversation = new Conversation( );
                int nIndex = 1;
                conversation.setId( daoUtil.getInt( nIndex++ ) );
                conversation.setUserMsg( daoUtil.getString( nIndex++ ) );
                conversation.setBotResponse( daoUtil.getString( nIndex++ ) );
                conversation.setIdScenario( daoUtil.getInt( nIndex++ ) );
                conversation.setVersion( daoUtil.getInt( nIndex++ ) );
            }
        }
        return conversation;
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
    public void store( Conversation conversation, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, conversation.getId( ) );
            daoUtil.setString( nIndex++, conversation.getUserMsg( ) );
            daoUtil.setString( nIndex++, conversation.getBotResponse( ) );
            daoUtil.setInt( nIndex++, conversation.getIdScenario( ) );
            daoUtil.setInt( nIndex++, conversation.getVersion( ) );
            daoUtil.setInt( nIndex, conversation.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Conversation> selectConversationsList( Plugin plugin )
    {
        List<Conversation> conversationList = new ArrayList<Conversation>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Conversation conversation = new Conversation( );
                int nIndex = 1;
                conversation.setId( daoUtil.getInt( nIndex++ ) );
                conversation.setUserMsg( daoUtil.getString( nIndex++ ) );
                conversation.setBotResponse( daoUtil.getString( nIndex++ ) );
                conversation.setIdScenario( daoUtil.getInt( nIndex++ ) );
                conversation.setVersion( daoUtil.getInt( nIndex++ ) );
                conversationList.add( conversation );
            }
        }
        return conversationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdConversationsList( Plugin plugin )
    {
        List<Integer> conversationList = new ArrayList<Integer>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                conversationList.add( daoUtil.getInt( 1 ) );
            }
        }
        return conversationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectConversationsReferenceList( Plugin plugin )
    {
        ReferenceList conversationList = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                conversationList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return conversationList;
    }

    @Override
    public List<Conversation> selectConversationsReferenceListByScenario( int idScenario, Plugin plugin )
    {
        List<Conversation> conversationList = new ArrayList<Conversation>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_IDSCENARIO, plugin ) )
        {
            daoUtil.setInt( 1, idScenario );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Conversation conversation = new Conversation( );
                int nIndex = 1;
                conversation.setId( daoUtil.getInt( nIndex++ ) );
                conversation.setUserMsg( daoUtil.getString( nIndex++ ) );
                conversation.setBotResponse( daoUtil.getString( nIndex++ ) );
                conversation.setIdScenario( daoUtil.getInt( nIndex++ ) );
                conversation.setVersion( daoUtil.getInt( nIndex++ ) );
                conversationList.add( conversation );
            }
        }
        return conversationList;
    }

    @Override
    public List<Conversation> selectConversationsByVersion( int idScenario, int idVersion, Plugin plugin )
    {
        List<Conversation> conversationList = new ArrayList<Conversation>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_VERSION, plugin ) )
        {
            daoUtil.setInt( 1, idScenario );
            daoUtil.setInt( 2, idVersion );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Conversation conversation = new Conversation( );
                int nIndex = 1;
                conversation.setId( daoUtil.getInt( nIndex++ ) );
                conversation.setUserMsg( daoUtil.getString( nIndex++ ) );
                conversation.setBotResponse( daoUtil.getString( nIndex++ ) );
                conversation.setIdScenario( daoUtil.getInt( nIndex++ ) );
                conversation.setVersion( daoUtil.getInt( nIndex++ ) );
                conversationList.add( conversation );
            }
        }
        return conversationList;
    }

    @Override
    public ReferenceList selectVersionsList( int idScenario, Plugin plugin )
    {
        ReferenceList refList = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_VERSIONS, plugin ) )
        {
            daoUtil.setInt( 1, idScenario );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                int nIndex = 1;
                String version = String.valueOf( daoUtil.getInt( nIndex++ ) );
                if ( version.equals( "0" ) )
                {
                    refList.addItem( String.valueOf( 0 ), "Version initale	" );
                }
                else
                {
                    refList.addItem( version, "Enregristrement #" + version );
                }
            }
        }
        return refList;
    }
}
