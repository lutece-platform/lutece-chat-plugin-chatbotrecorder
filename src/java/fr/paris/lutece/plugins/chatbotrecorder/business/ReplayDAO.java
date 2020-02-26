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
 * This class provides Data Access methods for Replay objects
 */
public final class ReplayDAO implements IReplayDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_replay, last_run, status, nbr_err, id_scenario, version FROM chatbotrecorder_replay WHERE id_replay = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO chatbotrecorder_replay ( last_run, status, nbr_err, id_scenario, version ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM chatbotrecorder_replay WHERE id_replay = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE chatbotrecorder_replay SET id_replay = ?, last_run = ?, status = ?, nbr_err = ?, id_scenario = ?, version = ? WHERE id_replay = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_replay, last_run, status, nbr_err, id_scenario, version FROM chatbotrecorder_replay";
    private static final String SQL_QUERY_SELECTALL_IDSCENARIO = "SELECT id_replay, last_run, status, nbr_err, id_scenario, version FROM chatbotrecorder_replay WHERE id_scenario = ? ORDER BY id_replay DESC";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_replay FROM chatbotrecorder_replay";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Replay replay, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setTimestamp( nIndex++, replay.getLastRun( ) );
            daoUtil.setInt( nIndex++, replay.getStatus( ) );
            daoUtil.setInt( nIndex++, replay.getNbrErr( ) );
            daoUtil.setInt( nIndex++, replay.getIdScenario( ) );
            daoUtil.setInt( nIndex++, replay.getVersion( ) );
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                replay.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Replay load( int nKey, Plugin plugin )
    {
        Replay replay = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                replay = new Replay( );
                int nIndex = 1;
                replay.setId( daoUtil.getInt( nIndex++ ) );
                replay.setLastRun( daoUtil.getTimestamp( nIndex++ ) );
                replay.setStatus( daoUtil.getInt( nIndex++ ) );
                replay.setNbrErr( daoUtil.getInt( nIndex++ ) );
                replay.setIdScenario( daoUtil.getInt( nIndex++ ) );
                replay.setVersion( daoUtil.getInt( nIndex++ ) );
            }
        }
        return replay;
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
    public void store( Replay replay, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, replay.getId( ) );
            daoUtil.setTimestamp( nIndex++, replay.getLastRun( ) );
            daoUtil.setInt( nIndex++, replay.getStatus( ) );
            daoUtil.setInt( nIndex++, replay.getNbrErr( ) );
            daoUtil.setInt( nIndex++, replay.getIdScenario( ) );
            daoUtil.setInt( nIndex++, replay.getVersion( ) );
            daoUtil.setInt( nIndex, replay.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Replay> selectReplaysList( Plugin plugin )
    {
        List<Replay> replayList = new ArrayList<Replay>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Replay replay = new Replay( );
                int nIndex = 1;
                replay.setId( daoUtil.getInt( nIndex++ ) );
                replay.setLastRun( daoUtil.getTimestamp( nIndex++ ) );
                replay.setStatus( daoUtil.getInt( nIndex++ ) );
                replay.setNbrErr( daoUtil.getInt( nIndex++ ) );
                replay.setIdScenario( daoUtil.getInt( nIndex++ ) );
                replay.setVersion( daoUtil.getInt( nIndex++ ) );
                replayList.add( replay );
            }
        }
        return replayList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdReplaysList( Plugin plugin )
    {
        List<Integer> replayList = new ArrayList<Integer>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                replayList.add( daoUtil.getInt( 1 ) );
            }
        }
        return replayList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectReplaysReferenceList( Plugin plugin )
    {
        ReferenceList replayList = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                replayList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return replayList;
    }

    @Override
    public List<Replay> selectReplaysListByScenario( int nIdScenario, Plugin plugin )
    {
        List<Replay> replayList = new ArrayList<Replay>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_IDSCENARIO, plugin ) )
        {
            daoUtil.setInt( 1, nIdScenario );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                Replay replay = new Replay( );
                int nIndex = 1;
                replay.setId( daoUtil.getInt( nIndex++ ) );
                replay.setLastRun( daoUtil.getTimestamp( nIndex++ ) );
                replay.setStatus( daoUtil.getInt( nIndex++ ) );
                replay.setNbrErr( daoUtil.getInt( nIndex++ ) );
                replay.setIdScenario( daoUtil.getInt( nIndex++ ) );
                replay.setVersion( daoUtil.getInt( nIndex++ ) );
                replayList.add( replay );
            }
        }
        return replayList;
    }
}
