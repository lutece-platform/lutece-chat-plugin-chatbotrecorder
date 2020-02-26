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
package fr.paris.lutece.plugins.chatbotrecorder.web;

import fr.paris.lutece.plugins.chatbotrecorder.business.Conversation;
import fr.paris.lutece.plugins.chatbotrecorder.business.ConversationHome;
import fr.paris.lutece.plugins.chatbotrecorder.business.Replay;
import fr.paris.lutece.plugins.chatbotrecorder.business.ReplayConversation;
import fr.paris.lutece.plugins.chatbotrecorder.business.ReplayConversationHome;
import fr.paris.lutece.plugins.chatbotrecorder.business.ReplayHome;
import fr.paris.lutece.plugins.chatbotrecorder.business.Result;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage ReplayConversation features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageReplayConversations.jsp", controllerPath = "jsp/admin/plugins/chatbotrecorder/", right = "CHATBOTRECORDER_MANAGEMENT" )
public class ReplayConversationJspBean extends AbstractChatBotRecorderManagerJspBean
{
    /**
         *
         */
    private static final long serialVersionUID = -7336013287821962432L;
    // Templates
    private static final String TEMPLATE_MANAGE_REPLAYCONVERSATIONS = "/admin/plugins/chatbotrecorder/manage_replayconversations.html";
    private static final String TEMPLATE_CREATE_REPLAYCONVERSATION = "/admin/plugins/chatbotrecorder/create_replayconversation.html";
    private static final String TEMPLATE_MODIFY_REPLAYCONVERSATION = "/admin/plugins/chatbotrecorder/modify_replayconversation.html";
    private static final String TEMPLATE_SHOW_REPLAYCONVERSATION = "/admin/plugins/chatbotrecorder/show_replayconversation.html";
    private static final String TEMPLATE_SHOW_REPLAYCONVERSATION_PREVIEW = "/admin/plugins/chatbotrecorder/show_replayconversation_preview.html";
    // Parameters
    private static final String PARAMETER_ID_REPLAYCONVERSATION = "id";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_REPLAYCONVERSATIONS = "chatbotrecorder.manage_replayconversations.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_REPLAYCONVERSATION = "chatbotrecorder.modify_replayconversation.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_REPLAYCONVERSATION = "chatbotrecorder.create_replayconversation.pageTitle";
    // Markers
    private static final String MARK_REPLAYCONVERSATION_LIST = "replayconversation_list";
    private static final String MARK_REPLAYCONVERSATION = "replayconversation";
    private static final String MARK_REPLAY = "replay";
    private static final String MARK_CONVERSATION_LIST = "conversation_list";
    private static final String JSP_MANAGE_REPLAYCONVERSATIONS = "jsp/admin/plugins/chatbotrecorder/ManageReplayConversations.jsp";
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_REPLAYCONVERSATION = "chatbotrecorder.message.confirmRemoveReplayConversation";
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "chatbotrecorder.model.entity.replayconversation.attribute.";
    // Views
    private static final String VIEW_MANAGE_REPLAYCONVERSATIONS = "manageReplayConversations";
    private static final String VIEW_CREATE_REPLAYCONVERSATION = "createReplayConversation";
    private static final String VIEW_MODIFY_REPLAYCONVERSATION = "modifyReplayConversation";
    private static final String VIEW_SHOW_REPLAYCONVERSATION = "showReplayConversation";
    // Actions
    private static final String ACTION_CREATE_REPLAYCONVERSATION = "createReplayConversation";
    private static final String ACTION_MODIFY_REPLAYCONVERSATION = "modifyReplayConversation";
    private static final String ACTION_REMOVE_REPLAYCONVERSATION = "removeReplayConversation";
    private static final String ACTION_CONFIRM_REMOVE_REPLAYCONVERSATION = "confirmRemoveReplayConversation";
    // Infos
    private static final String INFO_REPLAYCONVERSATION_CREATED = "chatbotrecorder.info.replayconversation.created";
    private static final String INFO_REPLAYCONVERSATION_UPDATED = "chatbotrecorder.info.replayconversation.updated";
    private static final String INFO_REPLAYCONVERSATION_REMOVED = "chatbotrecorder.info.replayconversation.removed";
    // Session variable to store working values
    private ReplayConversation _replayconversation;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_REPLAYCONVERSATIONS, defaultView = true )
    public String getManageReplayConversations( HttpServletRequest request )
    {
        _replayconversation = null;
        List<ReplayConversation> listReplayConversations = ReplayConversationHome.getReplayConversationsList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_REPLAYCONVERSATION_LIST, listReplayConversations, JSP_MANAGE_REPLAYCONVERSATIONS );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_REPLAYCONVERSATIONS, TEMPLATE_MANAGE_REPLAYCONVERSATIONS, model );
    }

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_SHOW_REPLAYCONVERSATION )
    public String getShowReplayConversation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPLAYCONVERSATION ) );
        Replay _replay = ReplayHome.findByPrimaryKey( nId );
        String _template;
        if ( request.getParameter( "preview" ) != null )
        {
            _template = TEMPLATE_SHOW_REPLAYCONVERSATION_PREVIEW;
        }
        else
        {
            _template = TEMPLATE_SHOW_REPLAYCONVERSATION;
        }
        List<ReplayConversation> listReplayConversations = ReplayConversationHome.getReplayConversationsListByReplay( nId );
        // Scenario scenario = ScenarioHome.findByPrimaryKey( _replay.getIdScenario() );
        // int version = scenario.getVersion();
        List<Conversation> listScenarioConversations = ConversationHome.getConversationsByVersion( _replay.getIdScenario( ), _replay.getVersion( ) );
        List<Object> test = compareConversations( listScenarioConversations, listReplayConversations );
        Map<String, Object> model = getModel( );
        model.put( MARK_REPLAYCONVERSATION_LIST, listReplayConversations );
        model.put( MARK_REPLAY, _replay );
        model.put( MARK_CONVERSATION_LIST, listScenarioConversations );
        model.put( "req", test );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_REPLAYCONVERSATION, _template, model );
    }

    /**
     * Returns the form to create a replayconversation
     *
     * @param request
     *            The Http request
     * @return the html code of the replayconversation form
     */
    @View( VIEW_CREATE_REPLAYCONVERSATION )
    public String getCreateReplayConversation( HttpServletRequest request )
    {
        _replayconversation = ( _replayconversation != null ) ? _replayconversation : new ReplayConversation( );
        Map<String, Object> model = getModel( );
        model.put( MARK_REPLAYCONVERSATION, _replayconversation );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_REPLAYCONVERSATION, TEMPLATE_CREATE_REPLAYCONVERSATION, model );
    }

    /**
     * Process the data capture form of a new replayconversation
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_REPLAYCONVERSATION )
    public String doCreateReplayConversation( HttpServletRequest request )
    {
        populate( _replayconversation, request, request.getLocale( ) );
        // Check constraints
        if ( !validateBean( _replayconversation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_REPLAYCONVERSATION );
        }
        ReplayConversationHome.create( _replayconversation );
        addInfo( INFO_REPLAYCONVERSATION_CREATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_REPLAYCONVERSATIONS );
    }

    /**
     * Manages the removal form of a replayconversation whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_REPLAYCONVERSATION )
    public String getConfirmRemoveReplayConversation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPLAYCONVERSATION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_REPLAYCONVERSATION ) );
        url.addParameter( PARAMETER_ID_REPLAYCONVERSATION, nId );
        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_REPLAYCONVERSATION, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a replayconversation
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage replayconversations
     */
    @Action( ACTION_REMOVE_REPLAYCONVERSATION )
    public String doRemoveReplayConversation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPLAYCONVERSATION ) );
        ReplayConversationHome.remove( nId );
        addInfo( INFO_REPLAYCONVERSATION_REMOVED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_REPLAYCONVERSATIONS );
    }

    /**
     * Returns the form to update info about a replayconversation
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_REPLAYCONVERSATION )
    public String getModifyReplayConversation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPLAYCONVERSATION ) );
        if ( _replayconversation == null || ( _replayconversation.getId( ) != nId ) )
        {
            _replayconversation = ReplayConversationHome.findByPrimaryKey( nId );
        }
        Map<String, Object> model = getModel( );
        model.put( MARK_REPLAYCONVERSATION, _replayconversation );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_REPLAYCONVERSATION, TEMPLATE_MODIFY_REPLAYCONVERSATION, model );
    }

    /**
     * Process the change form of a replayconversation
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_REPLAYCONVERSATION )
    public String doModifyReplayConversation( HttpServletRequest request )
    {
        populate( _replayconversation, request, request.getLocale( ) );
        // Check constraints
        if ( !validateBean( _replayconversation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_REPLAYCONVERSATION, PARAMETER_ID_REPLAYCONVERSATION, _replayconversation.getId( ) );
        }
        ReplayConversationHome.update( _replayconversation );
        addInfo( INFO_REPLAYCONVERSATION_UPDATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_REPLAYCONVERSATIONS );
    }

    private List<Object> compareConversations( List<Conversation> conversation, List<ReplayConversation> replayConversation )
    {
        int maxsize = 0;
        int minsize = 0;
        int conversationSize = conversation.size( );
        int replayConversationSize = replayConversation.size( );
        List<Object> compare = new ArrayList<>( );
        if ( conversationSize > replayConversationSize )
        {
            maxsize = conversationSize;
            minsize = replayConversationSize;
        }
        else
        {
            maxsize = replayConversationSize;
            minsize = conversationSize;
        }
        for ( int i = 0; i < maxsize; i++ )
        {
            Result result = new Result( );
            if ( i <= minsize - 1 )
            {
                int j = i + 1;
                if ( conversation.get( i ).getUserMsg( ) != null )
                {
                    result.setQuestion( conversation.get( i ).getUserMsg( ) );
                }
                if ( conversation.size( ) > j && conversation.get( j ).getBotResponse( ) != null )
                {
                    result.setReponse( conversation.get( j ).getBotResponse( ) );
                }
                if ( replayConversation.size( ) > j && replayConversation.get( j ).getBotResponse( ) != null )
                {
                    result.setCurrentResponse( replayConversation.get( j ).getBotResponse( ) );
                }
                if ( result.getReponse( ) != null && result.getCurrentResponse( ) != null )
                {
                    if ( !result.getReponse( ).equals( result.getCurrentResponse( ) ) )
                    {
                        result.setStatus( 1 );
                    }
                }
                else
                {
                    result.setStatus( 1 );
                }
                if ( result.getQuestion( ) != null )
                {
                    compare.add( result );
                }
            }
        }
        return compare;
    }
}
