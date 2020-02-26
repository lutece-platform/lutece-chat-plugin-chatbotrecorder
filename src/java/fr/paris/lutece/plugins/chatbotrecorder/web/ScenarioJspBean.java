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

import fr.paris.lutece.plugins.chatbot.business.Post;
import fr.paris.lutece.plugins.chatbot.service.BotService;
import fr.paris.lutece.plugins.chatbot.service.ChatService;
import fr.paris.lutece.plugins.chatbot.service.InvalidBotKeyException;
import fr.paris.lutece.plugins.chatbot.service.bot.ChatBot;
import fr.paris.lutece.plugins.chatbotrecorder.business.ChatBotApp;
import fr.paris.lutece.plugins.chatbotrecorder.business.Conversation;
import fr.paris.lutece.plugins.chatbotrecorder.business.ConversationHome;
import fr.paris.lutece.plugins.chatbotrecorder.business.Replay;
import fr.paris.lutece.plugins.chatbotrecorder.business.ReplayConversation;
import fr.paris.lutece.plugins.chatbotrecorder.business.ReplayConversationHome;
import fr.paris.lutece.plugins.chatbotrecorder.business.ReplayHome;
import fr.paris.lutece.plugins.chatbotrecorder.business.Scenario;
import fr.paris.lutece.plugins.chatbotrecorder.business.ScenarioHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Scenario features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageScenarios.jsp", controllerPath = "jsp/admin/plugins/chatbotrecorder/", right = "CHATBOTRECORDER_MANAGEMENT" )
public class ScenarioJspBean extends AbstractChatBotRecorderManagerJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = 7452782435326954707L;
    // Templates
    private static final String TEMPLATE_MANAGE_SCENARIOS = "/admin/plugins/chatbotrecorder/manage_scenarios.html";
    private static final String TEMPLATE_CREATE_SCENARIO = "/admin/plugins/chatbotrecorder/create_scenario.html";
    private static final String TEMPLATE_MODIFY_SCENARIO = "/admin/plugins/chatbotrecorder/modify_scenario.html";
    // Parameters
    private static final String PARAMETER_ID_SCENARIO = "id";
    private static final String PARAMETER_KEY_BOT = "bot";
    private static final String PARAMETER_RESPONSE = "response";
    private static final String PARAMETER_LANGUAGE = "lang";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_SCENARIOS = "chatbotrecorder.manage_scenarios.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_SCENARIO = "chatbotrecorder.modify_scenario.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_SCENARIO = "chatbotrecorder.create_scenario.pageTitle";
    // Markers
    private static final String MARK_SCENARIO_LIST = "scenario_list";
    private static final String MARK_SCENARIO = "scenario";
    private static final String MARK_POSTS_LIST = "posts_list";
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_SCENARIO = "chatbotrecorder.message.confirmRemoveScenario";
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "chatbotrecorder.model.entity.scenario.attribute.";
    // Views
    private static final String VIEW_MANAGE_SCENARIOS = "manageScenarios";
    private static final String VIEW_CREATE_SCENARIO = "createScenario";
    private static final String VIEW_MODIFY_SCENARIO = "modifyScenario";
    // Actions
    private static final String ACTION_CREATE_SCENARIO = "createScenario";
    private static final String ACTION_MODIFY_SCENARIO = "modifyScenario";
    private static final String ACTION_REMOVE_SCENARIO = "removeScenario";
    private static final String ACTION_CONFIRM_REMOVE_SCENARIO = "confirmRemoveScenario";
    private static final String ACTION_RUN_SCENARIO = "runScenario";
    private static final String ACTION_ADD_VERSION = "addVersion";
    // Infos
    private static final String INFO_SCENARIO_CREATED = "chatbotrecorder.info.scenario.created";
    private static final String INFO_SCENARIO_UPDATED = "chatbotrecorder.info.scenario.updated";
    private static final String INFO_SCENARIO_REMOVED = "chatbotrecorder.info.scenario.removed";
    // Session variable to store working values
    private Scenario _scenario;
    private String botKey;
    private ReplayConversation _replayConversation;
    private String _strBotKey;
    private Locale _locale;
    private String _strConversationId;
    private ChatBot _bot;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_SCENARIOS, defaultView = true )
    public String getManageScenarios( HttpServletRequest request )
    {
        _scenario = null;
        if ( request.getParameter( PARAMETER_KEY_BOT ) != null )
        {
            botKey = request.getParameter( PARAMETER_KEY_BOT );
        }
        List<Scenario> listScenarios = new ArrayList<>( );
        if ( botKey != null && botKey.length( ) > 0 )
        {
            listScenarios = ScenarioHome.getScenariosListByBotKey( botKey );
        }
        else
        {
            addError( "please select a bot first." );
        }
        Map<String, Object> model = getModel( );
        ChatBot bot = BotService.getBot( botKey );
        model.put( "bot", bot.getName( getLocale( ) ) );
        model.put( MARK_SCENARIO_LIST, listScenarios );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_SCENARIOS, TEMPLATE_MANAGE_SCENARIOS, model );
    }

    /**
     * Returns the form to create a scenario
     *
     * @param request
     *            The Http request
     * @return the html code of the scenario form
     */
    @View( VIEW_CREATE_SCENARIO )
    public String getCreateScenario( HttpServletRequest request )
    {
        _scenario = ( _scenario != null ) ? _scenario : new Scenario( );
        _scenario.setChatBotKey( botKey );
        String reset = request.getParameter( "view_createScenario" );
        if ( ( reset != null && reset.equals( "resetSession" ) ) || !botKey.equals( _strBotKey ) )
        {
            initSessionParameters( request, botKey );
        }
        List<Post> listPosts = ChatService.getConversation( _strConversationId, _bot, request.getLocale( ) );
        Map<String, Object> model = getModel( );
        model.put( MARK_SCENARIO, _scenario );
        model.put( MARK_POSTS_LIST, listPosts );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_SCENARIO, TEMPLATE_CREATE_SCENARIO, model );
    }

    /**
     * Process the data capture form of a new scenario
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_SCENARIO )
    public String doCreateScenario( HttpServletRequest request )
    {
        populate( _scenario, request, request.getLocale( ) );
        List<Post> listPosts = new ArrayList<>( );
        String action = request.getParameter( "action_createScenario" );
        if ( action.equals( "chatBot" ) )
        {
            // chatbot send and reload;
            String strMessage = request.getParameter( PARAMETER_RESPONSE );
            try
            {
                ChatService.processMessage( request, _strConversationId, strMessage, _strBotKey, _locale );
            }
            catch( InvalidBotKeyException e )
            {
                e.printStackTrace( );
            }
            return redirectView( request, VIEW_CREATE_SCENARIO );
        }
        else
        {
            // check chatBot;
            listPosts = ChatService.getConversation( _strConversationId, _bot, request.getLocale( ) );
            if ( listPosts.size( ) < 2 )
            {
                // no conversation to save;
                return redirectView( request, VIEW_CREATE_SCENARIO );
            }
        }
        // Check constraints
        if ( !validateBean( _scenario, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_SCENARIO );
        }
        ScenarioHome.create( _scenario );
        ConversationHome.createFromListPost( listPosts, _scenario.getId( ) );
        addInfo( INFO_SCENARIO_CREATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_SCENARIOS );
    }

    /**
     * Process the data capture form of a new replayconversation
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_ADD_VERSION )
    public String doAddVersion( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( "id" ) );
        Replay replay = ReplayHome.findByPrimaryKey( nId );
        int nIdScenario = replay.getIdScenario( );
        Scenario scenario = ScenarioHome.findByPrimaryKey( nIdScenario );
        String botKey = scenario.getChatBotKey( );
        scenario.setVersion( nId );
        scenario.setStatus( 0 );
        ScenarioHome.update( scenario );
        List<ReplayConversation> listReplayConversations = ReplayConversationHome.getReplayConversationsListByReplay( nId );
        List<Conversation> conversationsList = ConversationHome.getConversationsByVersion( nIdScenario, nId );
        if ( conversationsList.size( ) < 1 )
        {
            ConversationHome.createFromReplayConversion( listReplayConversations, nIdScenario );
        }
        addInfo( "L'enregristrement #" + nId + " est désormais la conversation de référence du scénario «<i>" + scenario.getName( ) + "</i>»." );
        request.setAttribute( "bot", botKey );
        return redirectView( request, VIEW_MANAGE_SCENARIOS );
    }

    /**
     * Manages the removal form of a scenario whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_RUN_SCENARIO )
    public String doRunScenario( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SCENARIO ) );
        if ( _scenario == null || ( _scenario.getId( ) != nId ) )
        {
            _scenario = ScenarioHome.findByPrimaryKey( nId );
        }
        List<Conversation> conversation = ConversationHome.getConversationsByVersion( nId, _scenario.getVersion( ) );
        List<ReplayConversation> replayConversation = replayConversation( request, conversation );
        int nbrErrors = compareConversations( conversation, replayConversation );
        Timestamp timestamp = new Timestamp( Calendar.getInstance( ).getTime( ).getTime( ) );
        Scenario scenario = ScenarioHome.findByPrimaryKey( nId );
        Replay replay = new Replay( );
        // status.
        if ( nbrErrors == 0 )
        {
            addInfo( "Le scénario «<i>" + scenario.getName( ) + "</i>» a été exécuté avec succès." );
            scenario.setStatus( 0 );
            replay.setStatus( 0 );
        }
        else
        {
            addError( "Le scénario «<i>" + scenario.getName( ) + "</i>» comporte <strong>" + nbrErrors + "</strong> erreur(s)." );
            scenario.setStatus( 1 );
            replay.setStatus( 1 );
        }
        // update scenario
        scenario.setLastRun( timestamp );
        ScenarioHome.update( scenario );
        // save replay
        replay.setNbrErr( nbrErrors );
        replay.setIdScenario( nId );
        replay.setLastRun( timestamp );
        replay.setVersion( scenario.getVersion( ) );
        ReplayHome.create( replay );
        // save replay conversation
        ReplayConversationHome.create( replayConversation, replay.getId( ) );
        return redirectView( request, VIEW_MANAGE_SCENARIOS );
    }

    /**
     * Manages the removal form of a scenario whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_SCENARIO )
    public String getConfirmRemoveScenario( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SCENARIO ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_SCENARIO ) );
        url.addParameter( PARAMETER_ID_SCENARIO, nId );
        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_SCENARIO, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a scenario
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage scenarios
     */
    @Action( ACTION_REMOVE_SCENARIO )
    public String doRemoveScenario( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SCENARIO ) );
        ScenarioHome.remove( nId );
        addInfo( INFO_SCENARIO_REMOVED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_SCENARIOS );
    }

    /**
     * Returns the form to update info about a scenario
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_SCENARIO )
    public String getModifyScenario( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SCENARIO ) );
        if ( _scenario == null || ( _scenario.getId( ) != nId ) )
        {
            _scenario = ScenarioHome.findByPrimaryKey( nId );
        }
        ReferenceList versions = ConversationHome.getVersionsList( nId );
        Map<String, Object> model = getModel( );
        model.put( MARK_SCENARIO, _scenario );
        model.put( "list_versions", versions );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_SCENARIO, TEMPLATE_MODIFY_SCENARIO, model );
    }

    /**
     * Process the change form of a scenario
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_SCENARIO )
    public String doModifyScenario( HttpServletRequest request )
    {
        populate( _scenario, request, request.getLocale( ) );
        // Check constraints
        if ( !validateBean( _scenario, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_SCENARIO, PARAMETER_ID_SCENARIO, _scenario.getId( ) );
        }
        ScenarioHome.update( _scenario );
        addInfo( INFO_SCENARIO_UPDATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_SCENARIOS );
    }

    // private method
    /**
     * Get request information for the bot language
     * 
     * @param request
     *            The request
     * @return The locale
     */
    private Locale getBotLocale( HttpServletRequest request )
    {
        String strLanguage = request.getParameter( PARAMETER_LANGUAGE );
        if ( strLanguage != null )
        {
            return new Locale( strLanguage );
        }
        return LocaleService.getDefault( );
    }

    /**
     * Initialize session datas
     * 
     * @param request
     *            The request
     * @param strBotKey
     *            The bot key
     */
    private void initSessionParameters( HttpServletRequest request, String strBotKey )
    {
        _strBotKey = strBotKey;
        _bot = BotService.getBot( _strBotKey );
        _locale = getBotLocale( request );
        _strConversationId = ChatBotApp.getNewConversationId( );
    }

    /**
     * Create a replay conversation from a conversation.
     * 
     * @param request
     *            The request
     * @param conversationList
     *            The list of a Scenario conversation
     * @param strBotKey
     *            The bot key
     */
    private List<ReplayConversation> replayConversation( HttpServletRequest request, List<Conversation> conversationList )
    {
        List<ReplayConversation> replayConversationList = new ArrayList<>( );
        initSessionParameters( request, botKey );
        for ( Conversation conversation : conversationList )
        {
            if ( conversation.getUserMsg( ) != null )
            {
                String strMessage = conversation.getUserMsg( );
                try
                {
                    ChatService.processMessage( request, _strConversationId, strMessage, _strBotKey, _locale );
                }
                catch( InvalidBotKeyException e )
                {
                    AppLogService.error( e );
                }
            }
        }
        List<Post> listPosts = new ArrayList<>( );
        listPosts = ChatService.getConversation( _strConversationId, _bot, request.getLocale( ) );
        for ( Post post : listPosts )
        {
            String content = post.getContent( );
            if ( content != null && !content.isEmpty( ) )
            {
                _replayConversation = new ReplayConversation( );
                if ( post.getAuthor( ) == 1 )
                {
                    _replayConversation.setUserMsg( content );
                }
                else
                {
                    _replayConversation.setBotResponse( content );
                }
                replayConversationList.add( _replayConversation );
            }
        }
        return replayConversationList;
    }

    /**
     * Compare two lists of conversations
     * 
     * @param conversation
     *            Conversation
     * @param replayConversation
     *            ReplayConversation
     * 
     */
    private int compareConversations( List<Conversation> conversation, List<ReplayConversation> replayConversation )
    {
        int nbrErrors = 0;
        int maxsize = 0;
        int minsize = 0;
        int conversationSize = conversation.size( );
        int replayConversationSize = replayConversation.size( );
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
            if ( i <= minsize - 1 )
            {
                if ( conversation.get( i ).getUserMsg( ) != null && replayConversation.get( i ).getUserMsg( ) != null
                        && !conversation.get( i ).getUserMsg( ).equals( replayConversation.get( i ).getUserMsg( ) ) )
                {
                    replayConversation.get( i ).setStatus( 1 );
                    nbrErrors++;
                }
                else
                    if ( conversation.get( i ).getBotResponse( ) != null && replayConversation.get( i ).getBotResponse( ) != null
                            && !conversation.get( i ).getBotResponse( ).equals( replayConversation.get( i ).getBotResponse( ) ) )
                    {
                        replayConversation.get( i ).setStatus( 1 );
                        nbrErrors++;
                    }
                    else
                        if ( conversation.get( i ).getUserMsg( ) == null && replayConversation.get( i ).getUserMsg( ) != null )
                        {
                            replayConversation.get( i ).setStatus( 1 );
                            nbrErrors++;
                        }
            }
            else
            {
                if ( i <= replayConversationSize - 1 )
                {
                    replayConversation.get( i ).setStatus( 1 );
                }
                nbrErrors++;
            }
        }
        return nbrErrors;
    }
}
