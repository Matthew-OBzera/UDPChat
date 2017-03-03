package networkingudp;
/**
  _____                    _  _       _     ___  _
 |_   _|___  __ _  _ __   | \| | ___ | |_  / __|| |_   ___  _ _  ___
   | | / -_)/ _` || '  \  | .` |/ _ \|  _| \__ \| ' \ / _ \| '_|/ -_)
   |_| \___|\__,_||_|_|_| |_|\_|\___/ \__| |___/|_||_|\___/|_|  \___|

 Kyle Loveless, Gerardo Paleo, Matthew OBzera

 */

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

class ClientMenuItemHandler implements EventHandler<ActionEvent>
{

    @Override
    public void handle(ActionEvent event)
    {
        // Use a TextInputDialog to get the IP address of the server
        TextInputDialog IPAddressDlg = new TextInputDialog("localhost");
        IPAddressDlg.setTitle("Server IP Address");
        IPAddressDlg.setHeaderText(null);
        IPAddressDlg.setContentText("Enter IP Address of the Server Machine");
        String IpAddress = IPAddressDlg.showAndWait()
                .orElseThrow(
                        () -> new RuntimeException("Bad input"));
        TextInputDialog RemotePortNumberDlg = new TextInputDialog("");
        RemotePortNumberDlg.setTitle("Remote Port Number");
        RemotePortNumberDlg.setHeaderText(null);
        RemotePortNumberDlg.setContentText("Enter the remote port number");
        String RemotePortNumber = RemotePortNumberDlg.showAndWait()
                .orElseThrow(
                        () -> new RuntimeException("Bad input"));
        TextInputDialog LocalPortNumberDlg = new TextInputDialog("");
        LocalPortNumberDlg.setTitle("Local Port Number");
        LocalPortNumberDlg.setHeaderText(null);
        LocalPortNumberDlg.setContentText("Enter the local port number");
        String localPortNumber = LocalPortNumberDlg.showAndWait()
                .orElseThrow(
                        () -> new RuntimeException("Bad input"));

        // Connect to the server
        try
        {
            Globals.socket = new DatagramSocket(Integer.parseInt(localPortNumber));
            Globals.socket.connect(InetAddress.getByName(IpAddress), Integer.parseInt(RemotePortNumber));
            Globals.buffer = new byte[100000];
            Thread networkThread = new Thread(new NetworkInputProcessor());
            networkThread.start();

        } catch (IOException ex)
        {
            Logger.getLogger(ClientMenuItemHandler.class.getName())
                    .log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}

class QuitMenuItemHandler implements EventHandler<ActionEvent>
{

    @Override
    public void handle(ActionEvent event)
    {
        try
        {
            byte[] sendBuffer = ("quit").getBytes();
            DatagramPacket sendPack = new DatagramPacket(sendBuffer, 0, sendBuffer.length);
            Globals.socket.send(sendPack);
            Globals.socket.close();
            System.exit(0);
        } catch (IOException ex)
        {
            Logger.getLogger(ClientMenuItemHandler.class.getName())
                    .log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}

class ImageMenuItemHandler implements EventHandler<ActionEvent>
{

    @Override
    public void handle(ActionEvent event)
    {
        JFileChooser fc = new JFileChooser();
        int option = fc.showOpenDialog(null);
        File file;
        if (option == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                file = fc.getSelectedFile();
                BufferedImage bi = ImageIO.read(file);
                Globals.imageLocalView.setImage(SwingFXUtils.toFXImage(bi, null));
                String fileName = file.getName();
                String [] fileExtension = fileName.split("\\.");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bi, fileExtension[(fileExtension.length-1)], baos);
                byte[] imageArray = baos.toByteArray();
                byte[] tempBuffer = ("photo " + imageArray.length + " ").getBytes();
                byte[] sendBuffer = new byte[tempBuffer.length+imageArray.length];
                System.arraycopy(tempBuffer, 0, sendBuffer, 0, tempBuffer.length);
                System.arraycopy(imageArray, 0, sendBuffer, tempBuffer.length, imageArray.length);
                DatagramPacket sendPack = new DatagramPacket(sendBuffer, 0, sendBuffer.length);
                Globals.socket.send(sendPack);
            } catch (IOException ex)
            {
                Logger.getLogger(ImageMenuItemHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
