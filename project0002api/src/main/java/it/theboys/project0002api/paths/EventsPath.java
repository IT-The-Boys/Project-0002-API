package it.theboys.project0002api.paths;


import java.io.IOException;
import java.lang.System.Logger;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import com.google.gson.JsonArray;

import org.apache.http.cookie.Cookie;
import org.springframework.web.servlet.function.ServerRequest.Headers;
import org.xnio.ChannelListener;

import io.undertow.server.protocol.framed.AbstractFramedChannel;
import io.undertow.util.Cookies;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.QueuedMessage;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.singletons.PreparingShutdown;
import it.theboys.project0002api.singletons.Sessions;

public class EventsPath implements WebSocketConnectionCallback {
    /**
     * An amount of milliseconds to wait after being notified that the user has at least one message
     * to deliver, before we actually deliver messages. This will allow multiple messages that arrive
     * in close proximity to each other to actually be delivered in the same client request.
     */
    private static final int WAIT_FOR_MORE_DELAY = 100;

    /**
     * The maximum number of messages which will be returned to a client during a single poll
     * operation.
     */
    private static final int MAX_MESSAGES_PER_POLL = 20;
    private static final Logger logger = Logger.getLogger(EventsPath.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<User, EventsSender.EventTask> tasks = new HashMap<>();

    private static Map<String, Cookie> getRequestCookies(WebSocketHttpExchange exchange) {
        return Cookies.parseRequestCookies(200, false, exchange.getRequestHeaders().get(Headers.COOKIE_STRING));
    }

    private static void sendConnectionError(WebSocketHttpExchange exchange, WebSocketChannel channel, JsonWrapper error) {
        WebSockets.sendText(error.toString(), channel, null);
        exchange.endExchange();
    }

    @Override
    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
        try {
            Cookie sid = getRequestCookies(exchange).get("PYX-Session");

            User user;
            if (sid == null || (user = Sessions.get().getUser(sid.getValue())) == null) {
                sendConnectionError(exchange, channel, new JsonWrapper(Consts.ErrorCode.NOT_REGISTERED));
            } else if (!user.isValid()) {
                sendConnectionError(exchange, channel, new JsonWrapper(Consts.ErrorCode.SESSION_EXPIRED));
            } else {
                if (user.getEventsSender() == null) user.establishedEventsConnection(new EventsSender(user, channel));
                else user.getEventsSender().addChannel(channel);

                channel.getCloseSetter().set((ChannelListener<AbstractFramedChannel>) newChannel -> {
                    if (user.getEventsSender() != null)
                        user.getEventsSender().removeChannel((WebSocketChannel) newChannel);
                });
            }
        } catch (Throwable ex) {
            logger.error("Failed handling incoming connection.", ex);
            throw ex;
        }
    }

    /**
     * Class for handling outgoing event messages, does not receive anything
     */
    public class EventsSender {
        private final User user;
        private final List<WebSocketChannel> channels;
        private final PriorityBlockingQueue<QueuedMessage> messages = new PriorityBlockingQueue<>();

        EventsSender(User user, WebSocketChannel channel) {
            this.user = user;
            this.channels = new ArrayList<>();
            addChannel(channel);
        }

        void addChannel(WebSocketChannel channel) {
            synchronized (channels) {
                channels.add(channel);
            }
        }

        private void removeChannel(WebSocketChannel channel) {
            synchronized (channels) {
                channels.remove(channel);
            }
        }

        public void enqueue(QueuedMessage message) {
            synchronized (messages) {
                messages.add(message);
            }

            EventTask current = tasks.get(user);
            if (current == null) {
                current = new EventTask();
                synchronized (tasks) {
                    tasks.put(user, current);
                }
                executorService.execute(current);
            }
        }

        private void handleIoException(IOException ex, WebSocketChannel channel) {
            if (ex instanceof ClosedChannelException) {
                removeChannel(channel);
            }
        }

        private class EventTask implements Runnable {

            @Override
            public void run() {
                try {
                    Thread.sleep(WAIT_FOR_MORE_DELAY);
                } catch (InterruptedException ignored) {
                }

                synchronized (tasks) {
                    tasks.remove(user);
                    if (tasks.isEmpty()) PreparingShutdown.get().allEventsSent();
                }

                ArrayList<QueuedMessage> toSend = new ArrayList<>(MAX_MESSAGES_PER_POLL);
                synchronized (messages) {
                    messages.drainTo(toSend, MAX_MESSAGES_PER_POLL);
                }

                JsonArray array = new JsonArray(toSend.size());
                for (QueuedMessage message : toSend)
                    array.add(message.getData().obj());

                for (WebSocketChannel channel : channels.toArray(new WebSocketChannel[0])) {
                    try {
                        WebSockets.sendTextBlocking(new JsonWrapper(Consts.GeneralKeys.EVENTS, array).obj().toString(), channel);
                        user.userReceivedEvents();
                    } catch (IOException ex) {
                        handleIoException(ex, channel);
                    }
                }
            }
        }
    }