import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MM
 * @version 1.0
 */

@MessageDriven(
        mappedName = "jms/MyQueue",
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue"
                ),
                @ActivationConfigProperty(
                        propertyName = "acknowledgeMode",
                        propertyValue = "Auto-acknowledge"
                )
        }
)
public class MdbBean implements MessageListener {

    private static final String INCREMENT_PATTERN =
            "(increment)/([+-]?\\d*\\.?\\d+)";
    private static final Integer ALBUM = 111111;
    private static final String JMS_TOPIC = "jms/MyTopic";
    private static final String JMS_FACTORY = "jms/MyConnectionFactory";

    private State state = State.STOP;
    private Double counter = 0.0;
    private Double errors = 0.0;

    /**
     * @param message message from jms
     */
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage messageContent = (TextMessage) message;
            try {
                handleAction(
                        messageContent.getText()
                                .trim()
                                .toLowerCase()
                );
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * @param s string from message
     * @throws JMSException when problem with jms
     * @throws NamingException when problem with jndi
     */
    private void handleAction(String s)
            throws JMSException, NamingException {
        switch (s){
            case "start":
                startAction();
                break;
            case "stop":
                stopAction();
                break;
            case "counter":
                returnCounterState();
                break;
            case "error":
                returnErrorState();
                break;
            case "increment":
                increment(1);
                break;
            default:
                handleDefaultAction(s);
                break;
        }
    }

    /**
     * @throws NamingException jndi problem
     * @throws JMSException jms problem
     */
    private void returnCounterState()
            throws NamingException, JMSException {
        sendMessage(counter);
    }

    /**
     * @throws NamingException jndi problem
     * @throws JMSException jms problem
     */
    private void returnErrorState()
            throws NamingException, JMSException {
        sendMessage(errors);
    }

    /**
     * starting counting
     */
    private void startAction() {
        if (state == State.STOP)
            state = State.COUNTING;
        else
            errors++;
    }

    /**
     * stop counting
     */
    private void stopAction() {
        if (state == State.COUNTING)
            state = State.STOP;
        else
            errors++;
    }

    /**
     * @param s string from message
     */
    private void handleDefaultAction(String s) {
        if (s.matches(INCREMENT_PATTERN)){
            Pattern pattern = Pattern.compile(INCREMENT_PATTERN);
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                increment(Integer.parseInt(matcher.group(2)));
            }
        } else{
            errors++;
        }
    }

    /**
     * @param i value to increment
     */
    private void increment(Integer i) {
        if (state == State.COUNTING)
            counter += i;
        else
            errors++;
    }

    /**
     * @param value error or counter
     * @throws NamingException jndi problem
     * @throws JMSException jms problem
     */
    private void sendMessage(Double value)
            throws JMSException, NamingException {
        TopicConnection connection = getConnection();
        String message = ALBUM + "/" + value;
        publishMessage(connection, message);
    }

    /**
     * @param connection topic connection
     * @param message message to sent
     * @throws NamingException jndi problem
     * @throws JMSException jms problem
     */
    private void publishMessage(TopicConnection connection,
                                String message)
            throws JMSException, NamingException {
        TopicSession session = connection.createTopicSession(false,
                Session.AUTO_ACKNOWLEDGE);
        Topic topic = getTopic();
        TopicPublisher publisher
                = session.createPublisher(topic);
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(message);
        publisher.publish(textMessage);
        connection.close();
    }

    /**
     * @throws NamingException jndi problem
     * @return topic
     */
    private Topic getTopic() throws NamingException {
        InitialContext ctx = new InitialContext();
        return (Topic) ctx.lookup(JMS_TOPIC);
    }

    /**
     * @throws NamingException jndi problem
     * @throws JMSException jms problem
     */
    private TopicConnection getConnection()
            throws NamingException, JMSException {
        InitialContext ctx = new InitialContext();
        TopicConnectionFactory factory = getFactoryConnection(ctx);
        TopicConnection connection = factory.createTopicConnection();
        connection.start();

        return connection;
    }

    /**
     * @param ctx context
     * @throws NamingException jndi problem
     */
    private TopicConnectionFactory getFactoryConnection(InitialContext ctx)
            throws NamingException {
        return (TopicConnectionFactory) ctx.lookup(JMS_FACTORY);
    }

    /**
     * Enum for handle state
     */
    private enum State{
        COUNTING, STOP
    }
}
