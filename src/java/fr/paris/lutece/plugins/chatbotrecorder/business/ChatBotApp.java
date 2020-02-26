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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import fr.paris.lutece.plugins.chatbot.business.BotDescription;
import fr.paris.lutece.plugins.chatbot.service.BotService;
import fr.paris.lutece.plugins.chatbot.service.bot.ChatBot;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.url.UrlItem;

public final class ChatBotApp
{
    // parameters
    private static final String PARAMETER_BOT = "bot";
    private static final String PARAMETER_LANGUAGE = "lang";
    // url
    private static final String URL_BOT = "jsp/admin/plugins/chatbotrecorder/ManageScenarios.jsp";

    /**
     * Gets the list of bots
     * 
     * @return The list of bots
     */
    public static List<BotDescription> getBotsDescription( )
    {
        List<BotDescription> list = new ArrayList<BotDescription>( );
        for ( ChatBot bot : BotService.getBots( ) )
        {
            List<String> listLanguages = bot.getAvailableLanguages( );
            if ( listLanguages != null )
            {
                for ( String strLanguage : listLanguages )
                {
                    BotDescription botDescription = new BotDescription( );
                    Locale locale = new Locale( strLanguage );
                    botDescription.setName( bot.getName( locale ) );
                    botDescription.setDescription( bot.getDescription( locale ) );
                    botDescription.setLanguage( locale.getDisplayLanguage( ) );
                    botDescription.setAvatarUrl( bot.getAvatarUrl( ) );
                    UrlItem url = new UrlItem( URL_BOT );
                    url.addParameter( PARAMETER_BOT, bot.getKey( ) );
                    url.addParameter( PARAMETER_LANGUAGE, strLanguage );
                    botDescription.setUrl( url.getUrl( ) );
                    list.add( botDescription );
                }
            }
            else
            {
                BotDescription botDescription = new BotDescription( );
                Locale locale = LocaleService.getDefault( );
                botDescription.setName( bot.getName( locale ) );
                botDescription.setDescription( bot.getDescription( locale ) );
                botDescription.setLanguage( locale.getDisplayLanguage( ) );
                botDescription.setAvatarUrl( bot.getAvatarUrl( ) );
                UrlItem url = new UrlItem( URL_BOT );
                url.addParameter( PARAMETER_BOT, bot.getKey( ) );
                url.addParameter( PARAMETER_LANGUAGE, locale.getLanguage( ) );
                botDescription.setUrl( url.getUrl( ) );
                list.add( botDescription );
            }
        }
        return list;
    }

    /**
     * Generate a Conversation ID
     * 
     * @return The ID
     */
    public static String getNewConversationId( )
    {
        return UUID.randomUUID( ).toString( );
    }
}
