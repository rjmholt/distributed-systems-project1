package client;

import common.client.*;

/**
 * Static library that takes a string and parses it to produce
 * a ClientMessage object to be serialised to JSON and sent to the
 * server.
 * @author rob
 *
 */
public class InputToJSONParser
{
	// The prefix denoting a command, rather than a message
    private static final String COMM_PREFIX = "#";
    
    // Enumeration of the possible ClientMessage types
    public enum ClientMessageType {
        IDENTITYCHANGE,
        JOIN,
        WHO,
        LIST,
        CREATEROOM,
        KICK,
        DELETE,
        MESSAGE,
        QUIT
    }

    /**
     * Parse a line of text from the user into a ClientMessage (to be later
     * serialised into JSON and sent to the server).
     * 
     * @param line	line of text entered by the client user
     * @return	a subclassed object of ClientMessage to send to the server
     * @throws InvalidCommandException
     */
    public static ClientMessage lineToMessage(String line) throws InvalidCommandException
    {
    	// If the line does not begin with the command prefix, it is an ordinary message
        if(!line.startsWith(COMM_PREFIX)) {
            return message(line);
        }
        // Otherwise it is a command, and needs to be parsed as such
        else {
        	// Split the string on whitespace and take the first argument as the command
            String[] txtArgs = line.substring(COMM_PREFIX.length()).split("\\s+");
            String cmd = txtArgs[0];
            
            // Try to match the command string with the list of known commands
            ClientMessageType cmdType = null;
            try
            {	
            	// If the command is not in the right format, throw an exception
                validateCommand(cmd);
                // If the command is not in the list of supported commands, throw an exception
                cmdType = ClientMessageType.valueOf(cmd.toUpperCase());
            }
            catch (IllegalArgumentException|InvalidCommandException e)
            {
            	// If an exception is thrown, simply send the message as an ordinary message
                return message(line);
            }
            
            // Switch on the command to verify it and perform the necessary action
            switch (cmdType) {
                case IDENTITYCHANGE:
                    validateIdentityChange(txtArgs);
                    return identityChange(txtArgs[1]);
                case JOIN:
                    validateJoin(txtArgs);
                    return join(txtArgs[1]);
                case WHO:
                    validateWho(txtArgs);
                    return who(txtArgs[1]);
                case LIST:
                    validateList(txtArgs);
                    return list();
                case CREATEROOM:
                    validateCreateRoom(txtArgs);
                    return createRoom(txtArgs[1]);
                case KICK:
                    validateKick(txtArgs);
                    return kick(txtArgs[3], txtArgs[2], txtArgs[1]);
                case DELETE:
                    validateDelete(txtArgs);
                    return delete(txtArgs[1]);
                case QUIT:
                    validateQuit(txtArgs);
                    return quit();
                default:
                	// This should be unreachable, but default clauses are always good
                    throw new InvalidCommandException("Command switch defaulted...");
            }
        }
    }

    private static void validateCommand(String cmd) throws InvalidCommandException
    {
        if(!cmd.matches("\\p{javaLowerCase}+")) {
            throw new InvalidCommandException("Commands must be all lower case");
        }
    }

    private static void validateIdentityChange(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 2) {
           throw new InvalidCommandException("identitychange takes one argument");
        } 
    }

    private static void validateJoin(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 2) {
            throw new InvalidCommandException("join takes one argument");
        }
    }

    private static void validateWho(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 2) {
            throw new InvalidCommandException("who takes one argument");
        }
    }

    private static void validateList(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 1) {
            throw new InvalidCommandException("list takes no arguments");
        }
    }

    private static void validateCreateRoom(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 2) {
            throw new InvalidCommandException("createroom takes one argument");
        }
    }

    private static void validateKick(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 4) {
           throw new InvalidCommandException("kick takes four arguments");
        }
       
        if(!txtArgs[2].matches("\\p{Digit}+")) {
            throw new InvalidCommandException("The second argument of kick must be a " +
                    "positive integer (e.g. 3600)");
        }
    }

    private static void validateDelete(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 2) {
            throw new InvalidCommandException("delete takes one argument");
        }
    }

    private static void validateQuit(String[] txtArgs) throws InvalidCommandException
    {
        if(txtArgs.length != 1) {
            throw new InvalidCommandException("quit takes no arguments");
        }
    }

    private static ClientMessage message(String content)
    {
        ClientMessage obj = new MessageCM(content);

        return obj;
    }

    private static ClientMessage identityChange(String newIdent)
    {
        ClientMessage obj = new IdentChangeCM(newIdent);

        return obj;
    }

    private static ClientMessage join(String roomID)
    {
        ClientMessage obj = new JoinCM(roomID);

        return obj;
    }

    private static ClientMessage who(String roomID)
    {
        ClientMessage obj = new WhoCM(roomID);

        return obj;
    }

    private static ClientMessage list()
    {
        ClientMessage obj = new ListCM();

        return obj;
    }

    private static ClientMessage createRoom(String roomID)
    {
        ClientMessage obj = new CreateRoomCM(roomID);

        return obj;
    }

    private static ClientMessage kick(String roomID, String timeStr, String ident)
    {
        ClientMessage obj = new KickCM(roomID, Integer.parseInt(timeStr), ident);

        return obj;
    }

    private static ClientMessage delete(String roomID)
    {
        ClientMessage obj = new DeleteCM(roomID);

        return obj;
    }

    private static ClientMessage quit()
    {
        ClientMessage obj = new QuitCM();

        return obj;
    }
}
