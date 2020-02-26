<jsp:useBean id="chatbotrecordermanagerReplayConversation" scope="session" class="fr.paris.lutece.plugins.chatbotrecorder.web.ReplayConversationJspBean" />
<% String strContent = chatbotrecordermanagerReplayConversation.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
