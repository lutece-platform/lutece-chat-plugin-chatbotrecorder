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
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Conversation features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageConversations.jsp", controllerPath = "jsp/admin/plugins/chatbotrecorder/", right = "CHATBOTRECORDER_MANAGEMENT" )
public class ConversationJspBean extends AbstractChatBotRecorderManagerJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = 3657775867175751855L;
    // Templates
    private static final String TEMPLATE_MANAGE_CONVERSATIONS = "/admin/plugins/chatbotrecorder/manage_conversations.html";
    private static final String TEMPLATE_CREATE_CONVERSATION = "/admin/plugins/chatbotrecorder/create_conversation.html";
    private static final String TEMPLATE_MODIFY_CONVERSATION = "/admin/plugins/chatbotrecorder/modify_conversation.html";
    // Parameters
    private static final String PARAMETER_ID_CONVERSATION = "id";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CONVERSATIONS = "chatbotrecorder.manage_conversations.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CONVERSATION = "chatbotrecorder.modify_conversation.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CONVERSATION = "chatbotrecorder.create_conversation.pageTitle";
    // Markers
    private static final String MARK_CONVERSATION_LIST = "conversation_list";
    private static final String MARK_CONVERSATION = "conversation";
    private static final String JSP_MANAGE_CONVERSATIONS = "jsp/admin/plugins/chatbotrecorder/ManageConversations.jsp";
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CONVERSATION = "chatbotrecorder.message.confirmRemoveConversation";
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "chatbotrecorder.model.entity.conversation.attribute.";
    // Views
    private static final String VIEW_MANAGE_CONVERSATIONS = "manageConversations";
    private static final String VIEW_CREATE_CONVERSATION = "createConversation";
    private static final String VIEW_MODIFY_CONVERSATION = "modifyConversation";
    // Actions
    private static final String ACTION_CREATE_CONVERSATION = "createConversation";
    private static final String ACTION_MODIFY_CONVERSATION = "modifyConversation";
    private static final String ACTION_REMOVE_CONVERSATION = "removeConversation";
    private static final String ACTION_CONFIRM_REMOVE_CONVERSATION = "confirmRemoveConversation";
    // Infos
    private static final String INFO_CONVERSATION_CREATED = "chatbotrecorder.info.conversation.created";
    private static final String INFO_CONVERSATION_UPDATED = "chatbotrecorder.info.conversation.updated";
    private static final String INFO_CONVERSATION_REMOVED = "chatbotrecorder.info.conversation.removed";
    // Session variable to store working values
    private Conversation _conversation;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CONVERSATIONS, defaultView = true )
    public String getManageConversations( HttpServletRequest request )
    {
        _conversation = null;
        List<Conversation> listConversations = ConversationHome.getConversationsList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CONVERSATION_LIST, listConversations, JSP_MANAGE_CONVERSATIONS );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CONVERSATIONS, TEMPLATE_MANAGE_CONVERSATIONS, model );
    }

    /**
     * Returns the form to create a conversation
     *
     * @param request
     *            The Http request
     * @return the html code of the conversation form
     */
    @View( VIEW_CREATE_CONVERSATION )
    public String getCreateConversation( HttpServletRequest request )
    {
        _conversation = ( _conversation != null ) ? _conversation : new Conversation( );
        Map<String, Object> model = getModel( );
        model.put( MARK_CONVERSATION, _conversation );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_CONVERSATION, TEMPLATE_CREATE_CONVERSATION, model );
    }

    /**
     * Process the data capture form of a new conversation
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CONVERSATION )
    public String doCreateConversation( HttpServletRequest request )
    {
        populate( _conversation, request, request.getLocale( ) );
        // Check constraints
        if ( !validateBean( _conversation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CONVERSATION );
        }
        ConversationHome.create( _conversation );
        addInfo( INFO_CONVERSATION_CREATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_CONVERSATIONS );
    }

    /**
     * Manages the removal form of a conversation whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CONVERSATION )
    public String getConfirmRemoveConversation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONVERSATION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CONVERSATION ) );
        url.addParameter( PARAMETER_ID_CONVERSATION, nId );
        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CONVERSATION, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a conversation
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage conversations
     */
    @Action( ACTION_REMOVE_CONVERSATION )
    public String doRemoveConversation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONVERSATION ) );
        ConversationHome.remove( nId );
        addInfo( INFO_CONVERSATION_REMOVED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_CONVERSATIONS );
    }

    /**
     * Returns the form to update info about a conversation
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CONVERSATION )
    public String getModifyConversation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONVERSATION ) );
        if ( _conversation == null || ( _conversation.getId( ) != nId ) )
        {
            _conversation = ConversationHome.findByPrimaryKey( nId );
        }
        Map<String, Object> model = getModel( );
        model.put( MARK_CONVERSATION, _conversation );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CONVERSATION, TEMPLATE_MODIFY_CONVERSATION, model );
    }

    /**
     * Process the change form of a conversation
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CONVERSATION )
    public String doModifyConversation( HttpServletRequest request )
    {
        populate( _conversation, request, request.getLocale( ) );
        // Check constraints
        if ( !validateBean( _conversation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CONVERSATION, PARAMETER_ID_CONVERSATION, _conversation.getId( ) );
        }
        ConversationHome.update( _conversation );
        addInfo( INFO_CONVERSATION_UPDATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_CONVERSATIONS );
    }
}
