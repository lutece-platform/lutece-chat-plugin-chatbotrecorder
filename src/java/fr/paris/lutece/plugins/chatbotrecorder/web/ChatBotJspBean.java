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

import fr.paris.lutece.plugins.chatbot.business.BotDescription;
import fr.paris.lutece.plugins.chatbotrecorder.business.ChatBotApp;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage ReplayConversation features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "SelectChatBot.jsp", controllerPath = "jsp/admin/plugins/chatbotrecorder/", right = "CHATBOTRECORDER_MANAGEMENT" )
public class ChatBotJspBean extends AbstractChatBotRecorderManagerJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = -7502571955673334641L;
    // Templates
    private static final String TEMPLATE_SELECT_CHATBOT = "/admin/plugins/chatbotrecorder/select_chatbot.html";
    // Properties
    private static final String PROPERTY_PAGE_TITLE_SELECT_CHATBOT = "chatbotrecorder.select_chatbot.pageTitle";
    // Views
    private static final String VIEW_SELECT_CHATBOT = "selectChatbot";
    // Markers
    private static final String MARK_BOTS_LIST = "bots_list";

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_SELECT_CHATBOT, defaultView = true )
    public String getSelectChatBot( HttpServletRequest request )
    {
        List<BotDescription> listBots = ChatBotApp.getBotsDescription( );
        Map<String, Object> model = getModel( );
        model.put( MARK_BOTS_LIST, listBots );
        return getPage( PROPERTY_PAGE_TITLE_SELECT_CHATBOT, TEMPLATE_SELECT_CHATBOT, model );
    }
}
