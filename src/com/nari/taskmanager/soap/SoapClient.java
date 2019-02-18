package com.nari.taskmanager.soap;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

public class SoapClient
{
  private static EndpointReference targetEPR = new EndpointReference("https://127.0.0.1/RtManageCon/services/I6000?wsdl");
    
  public static void sendKPIParam(String param)
  {
	    try
	    {
	      Options options = new Options();
	      options.setTo(targetEPR);
	      options.setAction("urn:getKPIValue");
	      ServiceClient sender = new ServiceClient();
	      sender.setOptions(options);
	      
	      OMElement methodSH = setKPIParam(param);


	      OMElement localOMElement1 = sender.sendReceive(methodSH);
	    }
	    catch (Exception axisFault)
	    {
	      axisFault.printStackTrace();
	    }	  
  }
  
  public static OMElement setKPIParam(String param)
  {
    OMFactory fac = OMAbstractFactory.getOMFactory();
    
    OMNamespace omNs = fac.createOMNamespace("http://soap.taskmanager.nari.com", "");
    
    OMElement methodSH = fac.createOMElement("getKPIValue", omNs);
    
    OMElement paramNode = fac.createOMElement("param", omNs);
    
    paramNode.addChild(fac.createOMText(paramNode, param));
    
    methodSH.addChild(paramNode);
    methodSH.build();
    return methodSH;
  }
  
  public static void main(String[] args)
  {

	String paramD = "<info><CorporationCode>001,002,003….</CorporationCode><Time>时间值</Time><api name=\"指标名称1\"></api><api name=\"BusinessSystemRunningTime\"></api></info>";
	
	sendKPIParam(paramD);
  }

  
}
