/* Banana-Chat - The first Open Source Knuddels Emulator
 * Copyright (C) 2011  Flav <http://banana-coding.com>
 *
 * Diese Datei unterliegt dem Copyright von Banana-Coding und
 * darf verändert, aber weder in andere Projekte eingefügt noch
 * reproduziert werden.
 *
 * Der Emulator dient - sofern der Client nicht aus Eigenproduktion
 * stammt - nur zu Lernzwecken, das Hosten des originalen Clients
 * ist untersagt und wird der Knuddels GmbH gemeldet.
 */

package knuddels;

import handler.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author Flav
 */
public class SessionHandler extends Thread {
    private Socket socket;

    public SessionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Client client = new Client(socket);

        try {
            InputStream in = socket.getInputStream();
            byte type = (byte) in.read();

            if (type == 0x00) {
                while (true) {
                    byte[] buffer = Protocol.decode(in);
                    String[] tokens = new String(buffer, "UTF8").split("\u0000");
                    String opcode = tokens[0];

                    if (opcode.equals(ReceiveOpcode.EXCEPTION.getValue())) {
                    } else if (opcode.equals(ReceiveOpcode.DISCONNECT.getValue())) {
                    } else if (opcode.equals(ReceiveOpcode.CHAT.getValue())) {
                        ChatHandler.handle(tokens, client);
                    } else if (opcode.equals(ReceiveOpcode.PING.getValue())) {
                        PingHandler.handle(tokens, client);
                    } else if (opcode.equals(ReceiveOpcode.POLL.getValue())) {
                        PollHandler.handle(tokens, client);
                    } else if (opcode.equals(ReceiveOpcode.LINK_CLICKED.getValue())) {
                    } else if (opcode.equals(ReceiveOpcode.JOIN_CHANNEL.getValue())) {
                        JoinChannelHandler.handle(tokens, client);
                    } else if (opcode.equals(ReceiveOpcode.Q_TOKEN.getValue())) {
                    } else if (opcode.equals(ReceiveOpcode.REQUEST_USER_LIST.getValue())) {
                    } else if (opcode.equals(ReceiveOpcode.HANDSHAKE.getValue())) {
                        HandshakeHandler.handle(tokens, client);
                    } else if (opcode.equals(ReceiveOpcode.REQUEST_HELP.getValue())) {
                        RequestHelpHandler.handle(tokens, client);
                    } else if (opcode.equals(ReceiveOpcode.LEAVE_CHANNEL.getValue())) {
                        LeaveChannelHandler.handle(tokens, client);
                    } else if (opcode.equals(ReceiveOpcode.WHOIS.getValue())) {
                    } else {
                        System.out.println(String.format("Unhandled opcode: %s", opcode));
                    }
                }
            } else if (type == 0x02) {
                // registration
            }
        } catch (IOException e) {
        } finally {
            client.disconnect();
        }
    }
}