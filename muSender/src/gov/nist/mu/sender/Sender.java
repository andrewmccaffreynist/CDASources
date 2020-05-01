/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nist.mu.sender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

/**
 * @author mccaffrey
 */
public class Sender {

    public static final int REST = 1;
    public static final int SOAP = 2;
    public static String pathToSampleDirectory = "/home/mccaffrey/hitsp/cvsfiles/hitspValidation/mu/samples";

    public static void main(String[] args) throws ParseException, UnknownHostException, IOException {

        org.apache.commons.cli.Options cliOptions = Sender.setCliOptions();
        CommandLineParser parser = new GnuParser();
        CommandLine line = parser.parse(cliOptions, args);

        HelpFormatter formatter = new HelpFormatter();
        if(line.hasOption("help") || args.length == 0) {
            formatter.printHelp("java -jar Sender.jar", cliOptions);

            System.out.println("\nDO SAMPLE COMMAND LINE RUN HERE");

            return;
        }

        String hostname = null;
        if(line.hasOption("hostname"))
            hostname = line.getOptionValue("hostname");

        String port = null;
        if(line.hasOption("port"))
            port = line.getOptionValue("port");

        String path = null;
        if(line.hasOption("path"))
            path = line.getOptionValue("path");

        String protocolString = null;
        if(line.hasOption("protocol"))
            protocolString = line.getOptionValue("protocol");

        int protocol = 0;
        if("soap".equalsIgnoreCase(protocolString))
            protocol = Sender.SOAP;
        else
            protocol = Sender.REST;


        Sender.send(hostname, port, path, protocol);


        /*
        String pathToSampleDirectory = "/home/mccaffrey/hitsp/cvsfiles/hitspValidation/mu/samples";

        String hostnameOrIp = "localhost";
        String port = "4444";
        String path = "";
        String sampleString = Sender.fetchFile(pathToSampleDirectory);

        // System.out.println(sampleString);

        int protocol = Sender.REST;
        // int protocol = Sender.SOAP;

        switch(protocol) {
        case REST:
        Sender.sendRest(hostnameOrIp,port,path,sampleString);
        break;

        case SOAP:
        Sender.sendSoap(hostnameOrIp,port,path,sampleString);
        break;
        }
         */
    }

    public static void send(String hostname, String port, String path, int protocol) throws UnknownHostException, IOException {

        String sampleString = Sender.fetchFile(Sender.pathToSampleDirectory);
        switch(protocol) {
            case REST:
                Sender.sendRest(hostname, port, path, sampleString);
                break;

            case SOAP:
                Sender.sendSoap(hostname, port, path, sampleString);
                break;
        }

    }

    public static void sendRest(String hostname, String port, String path, String message) throws UnknownHostException, IOException {

        StringBuffer sb = new StringBuffer();

        sb.append("POST /" + path + " HTTP/1.1\n");
        sb.append("Content-Type: application/xml; charset=utf-8\n");
        sb.append("Content-Length: " + message.length() + '\n');
        sb.append('\n');
        sb.append(message);

        Socket destination = null;

        destination = new Socket(hostname, Integer.parseInt(port));
        PrintWriter out = new PrintWriter(destination.getOutputStream(), true);
        out.print(sb.toString());
        out.flush();
        out.close();
        destination.close();

    }

    public static void sendSoap(String hostname, String port, String path, String message) throws UnknownHostException, IOException {

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\"?>\n");
        sb.append("<soap:Envelope ");
       // sb.append("xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\" ");
        sb.append("xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" ");
        sb.append("soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\">\n");
        sb.append("<soap:Body>");
        sb.append(message);
        sb.append("</soap:Body>\n");
        sb.append("</soap:Envelope>");

        Sender.sendRest(hostname, port, path, sb.toString());
    }

    public static String fetchFile(String pathToSampleDirectory) {
        File sampleDirectory = new File(pathToSampleDirectory);
        File[] fileList = sampleDirectory.listFiles();
        int random = (int) (Math.random() * fileList.length);
        File sampleFile = fileList[random];
        String sampleString = null;
        try {
            sampleString = Sender.readFile(sampleFile);
        } catch(FileNotFoundException ex) {
            // TODO
        } catch(IOException ex) {
            // TODO
        }
        return sampleString;
    }

    public static String readFile(File file) throws FileNotFoundException, IOException {
        BufferedReader input = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder xml = new StringBuilder();
        while((line = input.readLine()) != null)
            xml.append(line);
        return xml.toString();
    }

    private static org.apache.commons.cli.Options setCliOptions() throws IllegalArgumentException {
        Option help = new Option("help", "display this message");

        Option hostname = OptionBuilder.withArgName("hostname").hasArg().withDescription("destination hostname/ip").create("hostname");
        Option port = OptionBuilder.withArgName("port").hasArg().withDescription("destination port").create("port");
        Option path = OptionBuilder.withArgName("path").hasArg().withDescription("destination path").create("path");
        Option protocol = OptionBuilder.withArgName("protocol").hasArg().withDescription("protocol (SOAP or REST)").create("protocol");

        org.apache.commons.cli.Options cliOptions = new org.apache.commons.cli.Options();

        cliOptions.addOption(help);
        cliOptions.addOption(hostname);
        cliOptions.addOption(port);
        cliOptions.addOption(path);
        cliOptions.addOption(protocol);

        return cliOptions;
    }
}
