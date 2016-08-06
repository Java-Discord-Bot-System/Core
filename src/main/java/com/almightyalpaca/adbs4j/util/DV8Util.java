package com.almightyalpaca.adbs4j.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.function.Consumer;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;

public class DV8Util {

	public static void contactDV8(final JDA api, final Object object, final Consumer<Message> callback) {
		final User dv8 = api.getUserById("107562988810027008");
		if (dv8 == null) {
			throw new UnsupportedOperationException("This method is unsupported when the account cannot see DV8's account");
		}
		if (dv8.equals(api.getSelfInfo())) {
			throw new UnsupportedOperationException("Nice try m8, you can't contact yourself!");
		}
		if (object instanceof File) {
			dv8.getPrivateChannel().sendFileAsync((File) object, null, callback);
		} else if (object instanceof Message) {
			dv8.getPrivateChannel().sendMessageAsync((Message) object, callback);
		} else {
			dv8.getPrivateChannel().sendMessageAsync(new MessageBuilder().appendString(Objects.toString(object)).build(), callback);
		}
	}

	public static void contactDV8(final JDA api, final Throwable throwable, final Consumer<Message> callback) {
		final StringWriter writer = new StringWriter();
		throwable.printStackTrace(new PrintWriter(writer));
		DV8Util.contactDV8(api, new MessageBuilder().appendString(writer.toString()).build(), callback);
	}
}
