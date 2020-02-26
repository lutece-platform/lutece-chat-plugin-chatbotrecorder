<jsp:useBean id="chatbotrecordermanagerScenario" scope="session" class="fr.paris.lutece.plugins.chatbotrecorder.web.ScenarioJspBean" />
<% String strContent = chatbotrecordermanagerScenario.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
