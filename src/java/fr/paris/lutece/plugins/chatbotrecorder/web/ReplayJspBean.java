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

import fr.paris.lutece.plugins.chatbotrecorder.business.Replay;
import fr.paris.lutece.plugins.chatbotrecorder.business.ReplayHome;
import fr.paris.lutece.plugins.chatbotrecorder.business.Scenario;
import fr.paris.lutece.plugins.chatbotrecorder.business.ScenarioHome;
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
 * This class provides the user interface to manage Replay features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageReplays.jsp", controllerPath = "jsp/admin/plugins/chatbotrecorder/", right = "CHATBOTRECORDER_MANAGEMENT" )
public class ReplayJspBean extends AbstractChatBotRecorderManagerJspBean
{
    /**
     *
     */
    private static final long serialVersionUID = 6837330654847475123L;
    // Templates
    private static final String TEMPLATE_MANAGE_REPLAYS = "/admin/plugins/chatbotrecorder/manage_replays.html";
    private static final String TEMPLATE_CREATE_REPLAY = "/admin/plugins/chatbotrecorder/create_replay.html";
    private static final String TEMPLATE_MODIFY_REPLAY = "/admin/plugins/chatbotrecorder/modify_replay.html";
    // Parameters
    private static final String PARAMETER_ID_REPLAY = "id";
    private static final String PARAMETER_ID_SCENARIO = "id";
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_REPLAYS = "chatbotrecorder.manage_replays.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_REPLAY = "chatbotrecorder.modify_replay.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_REPLAY = "chatbotrecorder.create_replay.pageTitle";
    // Markers
    private static final String MARK_REPLAY_LIST = "replay_list";
    private static final String MARK_REPLAY = "replay";
    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_REPLAY = "chatbotrecorder.message.confirmRemoveReplay";
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "chatbotrecorder.model.entity.replay.attribute.";
    // Views
    private static final String VIEW_MANAGE_REPLAYS = "manageReplays";
    private static final String VIEW_CREATE_REPLAY = "createReplay";
    private static final String VIEW_MODIFY_REPLAY = "modifyReplay";
    private static final String VIEW_SHOW_REPLAY = "showReplay";
    // Actions
    private static final String ACTION_CREATE_REPLAY = "createReplay";
    private static final String ACTION_MODIFY_REPLAY = "modifyReplay";
    private static final String ACTION_REMOVE_REPLAY = "removeReplay";
    private static final String ACTION_CONFIRM_REMOVE_REPLAY = "confirmRemoveReplay";
    // Infos
    private static final String INFO_REPLAY_CREATED = "chatbotrecorder.info.replay.created";
    private static final String INFO_REPLAY_UPDATED = "chatbotrecorder.info.replay.updated";
    private static final String INFO_REPLAY_REMOVED = "chatbotrecorder.info.replay.removed";
    // Session variable to store working values
    private Replay _replay;
    private String idScenario;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_SHOW_REPLAY, defaultView = true )
    public String getManageReplays( HttpServletRequest request )
    {
        _replay = null;
        String _idScenario = request.getParameter( PARAMETER_ID_SCENARIO );
        if ( _idScenario != null )
        {
            idScenario = _idScenario;
        }
        List<Replay> listReplays = ReplayHome.getReplaysListByScenario( Integer.parseInt( idScenario ) );
        Map<String, Object> model = getModel( );
        Scenario scenario = ScenarioHome.findByPrimaryKey( Integer.parseInt( idScenario ) );
        model.put( "scenario", scenario.getName( ) );
        model.put( MARK_REPLAY_LIST, listReplays );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_REPLAYS, TEMPLATE_MANAGE_REPLAYS, model );
    }

    /**
     * Returns the form to create a replay
     *
     * @param request
     *            The Http request
     * @return the html code of the replay form
     */
    @View( VIEW_CREATE_REPLAY )
    public String getShowReplay( HttpServletRequest request )
    {
        _replay = ( _replay != null ) ? _replay : new Replay( );
        Map<String, Object> model = getModel( );
        model.put( MARK_REPLAY, _replay );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_REPLAY, TEMPLATE_CREATE_REPLAY, model );
    }

    /**
     * Returns the form to create a replay
     *
     * @param request
     *            The Http request
     * @return the html code of the replay form
     */
    @View( VIEW_CREATE_REPLAY )
    public String getCreateReplay( HttpServletRequest request )
    {
        _replay = ( _replay != null ) ? _replay : new Replay( );
        Map<String, Object> model = getModel( );
        model.put( MARK_REPLAY, _replay );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_REPLAY, TEMPLATE_CREATE_REPLAY, model );
    }

    /**
     * Process the data capture form of a new replay
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_REPLAY )
    public String doCreateReplay( HttpServletRequest request )
    {
        populate( _replay, request, request.getLocale( ) );
        // Check constraints
        if ( !validateBean( _replay, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_REPLAY );
        }
        // ReplayHome.create( _replay );
        addInfo( INFO_REPLAY_CREATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_REPLAYS );
    }

    /**
     * Manages the removal form of a replay whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_REPLAY )
    public String getConfirmRemoveReplay( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPLAY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_REPLAY ) );
        url.addParameter( PARAMETER_ID_REPLAY, nId );
        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_REPLAY, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );
        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a replay
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage replays
     */
    @Action( ACTION_REMOVE_REPLAY )
    public String doRemoveReplay( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPLAY ) );
        ReplayHome.remove( nId );
        addInfo( INFO_REPLAY_REMOVED, getLocale( ) );
        return redirectView( request, VIEW_SHOW_REPLAY );
    }

    /**
     * Returns the form to update info about a replay
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_REPLAY )
    public String getModifyReplay( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_REPLAY ) );
        if ( _replay == null || ( _replay.getId( ) != nId ) )
        {
            _replay = ReplayHome.findByPrimaryKey( nId );
        }
        Map<String, Object> model = getModel( );
        model.put( MARK_REPLAY, _replay );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_REPLAY, TEMPLATE_MODIFY_REPLAY, model );
    }

    /**
     * Process the change form of a replay
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_REPLAY )
    public String doModifyReplay( HttpServletRequest request )
    {
        populate( _replay, request, request.getLocale( ) );
        // Check constraints
        if ( !validateBean( _replay, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_REPLAY, PARAMETER_ID_REPLAY, _replay.getId( ) );
        }
        ReplayHome.update( _replay );
        addInfo( INFO_REPLAY_UPDATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_REPLAYS );
    }
}
