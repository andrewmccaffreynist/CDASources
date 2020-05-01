<%@page contentType="text/xml"%><%@page pageEncoding="UTF-8"%><jsp:useBean id="validation" scope="session" class="gov.nist.mu.validation.ValidationHelper" /><% 

   gov.nist.healthcare.mu.core.stat.StatTimer statTimer = new gov.nist.healthcare.mu.core.stat.StatTimer(gov.nist.healthcare.mu.core.stat.StatTimer.TestContext.TEST_CONTEXT_FREE, "N/A");
   statTimer.start();

%><%= validation.processRequestXml(request) %><% statTimer.endSuccess(); %>
