package com.nari.taskmanager.soap;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

@Service
public class I6000SoapService
{
  private static Logger logger = Logger.getLogger(I6000SoapService.class);
  
  public String getKPIValue(String param)
  {
    MessageContext mc = MessageContext.getCurrentMessageContext();
    HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
    logger.info("receive I6000 request from ip:  " + request.getRemoteAddr());
    logger.info("I6000 param : " + param );
    logger.info("I6000 input : " + request );
    
    Document resultDoc = DocumentHelper.createDocument();
    Element returnNode = resultDoc.addElement("return");
    
    //计算时间
    Date startTime = (Date) request.getSession().getServletContext().getAttribute("startTime");
    Long time = Calendar.getInstance().getTimeInMillis() - startTime.getTime();
    long runTime = time/1000; 
    
    try {
		Document paramDoc = DocumentHelper.parseText(param);
		
		Element info = paramDoc.getRootElement();
		
		List<Element> corp = info.elements("CorporationCode");
		String[] corpList = corp.get(0).getText().split(",");
		
		List<Element> api = info.elements("api");
		
		returnNode.addElement("status").addText("success");
		returnNode.addElement("message").addText("成功");
		
		
		for(int i=0; i<corpList.length; i++)
		{
			Element Corporation = returnNode.addElement("Corporation").addAttribute("id", corpList[i]);
			for(int j=0; j<api.size(); j++)
			{
				if(api.get(j).attributeValue("name").equals("BusinessSystemRunningTime"))
				{
					Element apiNode = Corporation.addElement("api").addAttribute("name", "BusinessSystemRunningTime");
					apiNode.addElement("value").addText(String.valueOf(runTime));
				}
				else
				{
					Element apiNode = Corporation.addElement("api").addAttribute("name", api.get(j).attributeValue("name"));
					apiNode.addElement("value").addText("NoData");					
				}
			}
		}
	} catch (DocumentException e) {
		e.printStackTrace();
		returnNode.addElement("status").addText("failure");
		returnNode.addElement("message").addText(e.getMessage());
		returnNode.addElement("reason").addText("xml解析错误");
	}
    logger.info("return : " + resultDoc.asXML());

//    String ttttt = resultDoc.asXML();
    
    return resultDoc.asXML();
  }
}
