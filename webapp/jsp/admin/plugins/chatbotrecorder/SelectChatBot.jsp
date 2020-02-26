<jsp:useBean id="chatbotrecordermanagerChatBot" scope="session" class="fr.paris.lutece.plugins.chatbotrecorder.web.ChatBotJspBean" />
<% String strContent = chatbotrecordermanagerChatBot.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
