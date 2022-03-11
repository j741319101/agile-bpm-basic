package com.dstz.sys.api.jms.model.msg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.model.SysIdentity;
/**
 * 通知消息的DTO
 * @author Jeff
 *
 */
public class NotifyMessage implements Serializable {
    private static final long serialVersionUID = 8026259230005823226L;
    private String subject;
    private String htmlContent;
    private String textContent;
    private IUser sender;
    private String tag;
    private List<SysIdentity> receivers;
    private Map<String, Object> extendVars = new HashMap();

    public NotifyMessage(String subject, String htmlContent, IUser sender, List<SysIdentity> receivers) {
        this.subject = subject;
        this.sender = sender;
        this.receivers = receivers;
        this.htmlContent = htmlContent;
    }

    public NotifyMessage(String subject, String htmlContent, IUser sender, List<SysIdentity> receivers, String tag, Map<String, Object> extendVars) {
        this.subject = subject;
        this.sender = sender;
        this.receivers = receivers;
        this.htmlContent = htmlContent;
        this.tag = tag;
        this.extendVars = extendVars;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public IUser getSender() {
        return this.sender;
    }

    public void setSender(IUser sender) {
        this.sender = sender;
    }

    public List<SysIdentity> getReceivers() {
        return this.receivers;
    }

    public void setReceivers(List<SysIdentity> receivers) {
        this.receivers = receivers;
    }

    public Map<String, Object> getExtendVars() {
        return this.extendVars;
    }

    public void setExtendVars(Map<String, Object> extendVars) {
        this.extendVars = extendVars;
    }

    public String getHtmlContent() {
        return this.htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getTextContent() {
        return this.textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
