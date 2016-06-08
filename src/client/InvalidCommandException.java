package client;

/**
 * Exception to raise when the user enters a command which
 * is too invalid to be parsed meaningfully (less invalid commands are
 * sent to the server to be rejected - as per the specification).
 * @author rob
 *
 */
public class InvalidCommandException extends Exception
{

	private static final long serialVersionUID = -862190285158300719L;

	public InvalidCommandException(String message)
    {
        super(message);
    }
}
