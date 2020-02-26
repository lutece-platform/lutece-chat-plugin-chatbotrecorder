<jsp:useBean id="chatbotrecordermanagerReplay" scope="session" class="fr.paris.lutece.plugins.chatbotrecorder.web.ReplayJspBean" />
<% String strContent = chatbotrecordermanagerReplay.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
