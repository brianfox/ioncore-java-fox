package net.ooici.ion.cc.message.stack.mailbox;

import net.ooici.ion.cc.ContainerException;

@SuppressWarnings("serial")
public class MailboxException extends ContainerException {

	public MailboxException(String string) {
		super(string);
	}

	public MailboxException(String string, Exception e) {
		super(string, e);
	}
	

}
