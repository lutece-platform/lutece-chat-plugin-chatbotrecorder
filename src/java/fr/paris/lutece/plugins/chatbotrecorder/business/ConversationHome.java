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

import fr.paris.lutece.plugins.chatbot.business.Post;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Conversation objects
 */
public final class ConversationHome
{
    // Static variable pointed at the DAO instance
    private static IConversationDAO _dao = SpringContextService.getBean( "chatbotrecorder.conversationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "chatbotrecorder" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ConversationHome( )
    {
    }

    /**
     * Create an instance of the conversation class
     * 
     * @param conversation
     *            The instance of the Conversation which contains the informations to store
     * @return The instance of conversation which has been created with its primary key.
     */
    public static Conversation create( Conversation conversation )
    {
        _dao.insert( conversation, _plugin );
        return conversation;
    }

    /**
     * Create an instance of the conversation class
     * 
     * @param conversation
     *            The instance of the Conversation which contains the informations to store
     * @return The instance of conversation which has been created with its primary key.
     */
    public static void createFromListPost( List<Post> listPosts, int idScenario )
    {
        for ( Post conversation : listPosts )
        {
            String content = conversation.getContent( );
            if ( content != null && !content.isEmpty( ) )
            {
                Conversation _conversation = new Conversation( );
                _conversation.setIdScenario( idScenario );
                if ( conversation.getAuthor( ) == 1 )
                {
                    // user
                    _conversation.setUserMsg( content );
                }
                else
                {
                    _conversation.setBotResponse( content );
                }
                create( _conversation );
            }
        }
    }

    /**
     * Load the data of all the referenceItem objects and returns them as a list
     * 
     * @return the list which contains the data of all the referenceItem objects
     */
    public static ReferenceList getVersionsList( int nIdReference )
    {
        return _dao.selectVersionsList( nIdReference, _plugin );
    }

    /**
     * Create an instance of the conversation class
     * 
     * @param conversation
     *            The instance of the Conversation which contains the informations to store
     * @return The instance of conversation which has been created with its primary key.
     */
    public static void createFromReplayConversion( List<ReplayConversation> listReplayConversation, int idScenario )
    {
        for ( ReplayConversation conversation : listReplayConversation )
        {
            String content = conversation.getBotResponse( ) + conversation.getUserMsg( );
            if ( content != null && !content.isEmpty( ) )
            {
                Conversation _conversation = new Conversation( );
                _conversation.setIdScenario( idScenario );
                _conversation.setVersion( conversation.getIdReplay( ) );
                if ( conversation.getUserMsg( ) != null )
                {
                    // user
                    _conversation.setUserMsg( conversation.getUserMsg( ) );
                }
                else
                {
                    _conversation.setBotResponse( conversation.getBotResponse( ) );
                }
                create( _conversation );
            }
        }
    }

    /**
     * Update of the conversation which is specified in parameter
     * 
     * @param conversation
     *            The instance of the Conversation which contains the data to store
     * @return The instance of the conversation which has been updated
     */
    public static Conversation update( Conversation conversation )
    {
        _dao.store( conversation, _plugin );
        return conversation;
    }

    /**
     * Remove the conversation whose identifier is specified in parameter
     * 
     * @param nKey
     *            The conversation Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a conversation whose identifier is specified in parameter
     * 
     * @param nKey
     *            The conversation primary key
     * @return an instance of Conversation
     */
    public static Conversation findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the conversation objects and returns them as a list
     * 
     * @return the list which contains the data of all the conversation objects
     */
    public static List<Conversation> getConversationsList( )
    {
        return _dao.selectConversationsList( _plugin );
    }

    /**
     * Load the id of all the conversation objects and returns them as a list
     * 
     * @return the list which contains the id of all the conversation objects
     */
    public static List<Integer> getIdConversationsList( )
    {
        return _dao.selectIdConversationsList( _plugin );
    }

    /**
     * Load the data of all the conversation objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the conversation objects
     */
    public static ReferenceList getConversationsReferenceList( )
    {
        return _dao.selectConversationsReferenceList( _plugin );
    }

    /**
     * Load the data of all the conversation objects of scenario and returns them as a referenceList
     * 
     * @param idScenario
     *            The id of a scenario
     * @return the referenceList which contains the data of all the conversation objects
     */
    public static List<Conversation> getConversationsReferenceListByScenario( int idScenario )
    {
        return _dao.selectConversationsReferenceListByScenario( idScenario, _plugin );
    }

    /**
     * Load the data of all the conversation objects of scenario and returns them as a referenceList
     * 
     * @param idScenario
     *            The id of a scenario
     * @return the referenceList which contains the data of all the conversation objects
     */
    public static List<Conversation> getConversationsByVersion( int idScenario, int idVersion )
    {
        return _dao.selectConversationsByVersion( idScenario, idVersion, _plugin );
    }
}
