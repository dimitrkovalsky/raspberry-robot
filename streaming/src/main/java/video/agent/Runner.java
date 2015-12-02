package video.agent;

import java.io.Console;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Dmytro_Kovalskyi on 02.12.2015.
 */
public class Runner {
    public static void main(String[] args) throws IOException {
        try {

            if(args.length < 2) {
                System.err.println(
                    "Incorrect arguments : " + Arrays.toString(args) + "\n"
                        + "Use 2 args 1: -s (server) or c (client) \t 2: ip for connection");
                System.exit(1);
            }
            String host = args[1];
            switch(args[0].toLowerCase()) {
                case "-c":
                    StreamClient client = new StreamClient();
                    client.run(host);
                    break;
                case "-s":
                    System.out.println("Trying to create StreamServer ");
                    StreamServer server = new StreamServer();
                    server.run(host);
                    break;
                default:
                    System.err.println("Unrecognized argument : " + args[0]);
                    System.exit(1);
            }
            System.out.println("Application ran");
            Console c = System.console();
            if (c != null) {
                // printf-like arguments
                c.format("\nPress ENTER to proceed.\n");
                c.readLine();
            }
            Thread.sleep(1000000);
        } catch(Exception e){
            System.err.println("ERROR : " + e.getMessage());
        }
    }
}
