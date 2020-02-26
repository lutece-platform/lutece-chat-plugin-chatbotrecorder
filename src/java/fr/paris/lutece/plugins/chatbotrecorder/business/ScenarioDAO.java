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
 * This class provides Data Access methods for Scenario objects
 */
public final class ScenarioDAO implements IScenarioDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_scenario, name, description, key_chatbot, last_run, status, version FROM chatbotrecorder_scenario WHERE id_scenario = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO chatbotrecorder_scenario ( name, description, key_chatbot, last_run, status, version ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM chatbotrecorder_scenario WHERE id_scenario = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE chatbotrecorder_scenario SET id_scenario = ?, name = ?, description = ?, key_chatbot = ?, last_run = ?, status = ?, version = ? WHERE id_scenario = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_scenario, name, description, key_chatbot, last_run, status, version FROM chatbotrecorder_scenario";
    private static final String SQL_QUERY_SELECTALL_BOTKEY = "SELECT id_scenario, name, description, key_chatbot, last_run, status, version FROM chatbotrecorder_scenario WHERE key_chatbot = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_scenario FROM chatbotrecorder_scenario";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Scenario scenario, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, scenario.getName( ) );
            daoUtil.setString( nIndex++, scenario.getDescription( ) );
            daoUtil.setString( nIndex++, scenario.getChatBotKey( ) );
            daoUtil.setTimestamp( nIndex++, scenario.getLastRun( ) );
            daoUtil.setInt( nIndex++, scenario.getStatus( ) );
            daoUtil.setInt( nIndex++, scenario.getVersion( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                scenario.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Scenario load( int nKey, Plugin plugin )
    {
        Scenario scenario = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                scenario = new Scenario( );
                int nIndex = 1;
                scenario.setId( daoUtil.getInt( nIndex++ ) );
                scenario.setName( daoUtil.getString( nIndex++ ) );
                scenario.setDescription( daoUtil.getString( nIndex++ ) );
                scenario.setChatBotKey( daoUtil.getString( nIndex++ ) );
                scenario.setLastRun( daoUtil.getTimestamp( nIndex++ ) );
                scenario.setStatus( daoUtil.getInt( nIndex++ ) );
                scenario.setVersion( daoUtil.getInt( nIndex++ ) );
            }
        }
        return scenario;
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
    public void store( Scenario scenario, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, scenario.getId( ) );
            daoUtil.setString( nIndex++, scenario.getName( ) );
            daoUtil.setString( nIndex++, scenario.getDescription( ) );
            daoUtil.setString( nIndex++, scenario.getChatBotKey( ) );
            daoUtil.setTimestamp( nIndex++, scenario.getLastRun( ) );
            daoUtil.setInt( nIndex++, scenario.getStatus( ) );
            daoUtil.setInt( nIndex++, scenario.getVersion( ) );
            daoUtil.setInt( nIndex, scenario.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Scenario> selectScenariosList( Plugin plugin )
    {
        List<Scenario> scenarioList = new ArrayList<Scenario>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Scenario scenario = new Scenario( );
                int nIndex = 1;
                scenario.setId( daoUtil.getInt( nIndex++ ) );
                scenario.setName( daoUtil.getString( nIndex++ ) );
                scenario.setDescription( daoUtil.getString( nIndex++ ) );
                scenario.setChatBotKey( daoUtil.getString( nIndex++ ) );
                scenario.setLastRun( daoUtil.getTimestamp( nIndex++ ) );
                scenario.setStatus( daoUtil.getInt( nIndex++ ) );
                scenario.setVersion( daoUtil.getInt( nIndex++ ) );
                scenarioList.add( scenario );
            }
        }
        return scenarioList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Scenario> selectScenariosListByBotKey( String botKey, Plugin plugin )
    {
        List<Scenario> scenarioList = new ArrayList<Scenario>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BOTKEY, plugin ) )
        {
            daoUtil.setString( 1, botKey );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Scenario scenario = new Scenario( );
                int nIndex = 1;
                scenario.setId( daoUtil.getInt( nIndex++ ) );
                scenario.setName( daoUtil.getString( nIndex++ ) );
                scenario.setDescription( daoUtil.getString( nIndex++ ) );
                scenario.setChatBotKey( daoUtil.getString( nIndex++ ) );
                scenario.setLastRun( daoUtil.getTimestamp( nIndex++ ) );
                scenario.setStatus( daoUtil.getInt( nIndex++ ) );
                scenario.setVersion( daoUtil.getInt( nIndex++ ) );
                scenarioList.add( scenario );
            }
        }
        return scenarioList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdScenariosList( Plugin plugin )
    {
        List<Integer> scenarioList = new ArrayList<Integer>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                scenarioList.add( daoUtil.getInt( 1 ) );
            }
        }
        return scenarioList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectScenariosReferenceList( Plugin plugin )
    {
        ReferenceList scenarioList = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                scenarioList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return scenarioList;
    }
}
