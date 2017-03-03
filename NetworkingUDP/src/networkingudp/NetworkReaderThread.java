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
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.imageio.ImageIO;

class ChatMessageReceived implements Runnable
{

    private final String message;

    ChatMessageReceived(String message)
    {
        this.message = message;
    }

    @Override
    public void run()
    {
        Globals.chatHistoryTextArea.appendText(String.format("Remote: %s\n", message));
    }
}

class QuitCommandReceived implements Runnable
{

    @Override
    public void run()
    {
        
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Lost Connection");
            alert.setHeaderText(null);
            alert.setContentText("The other person left");

            alert.showAndWait();
            Globals.socket.close();
            System.exit(0);
        
    }
}

class ImageCommandReceived implements Runnable
{
    byte[] data;
    int size;

    public ImageCommandReceived(int size, byte [] data)
    {
        this.size = size;
        this.data = data;
    }

    @Override
    public void run()
    {
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            BufferedImage bi = ImageIO.read(bais);
            Globals.imageRemoteView.setImage(SwingFXUtils.toFXImage(bi, null));
        } catch (IOException ex)
        {
            Logger.getLogger(ImageCommandReceived.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
}

// This runnable will wait for network input and post
// work orders on the Application thread when network
// input is received
class NetworkInputProcessor implements Runnable
{

    @Override
    public void run()
    {
        try
        {

            while (true)
            {
                DatagramPacket recvPack = new DatagramPacket(Globals.buffer, Globals.buffer.length);
                Globals.socket.receive(recvPack);
                String protocol = "";
                String data = new String(recvPack.getData(), 0, recvPack.getLength());
                Scanner read = new Scanner(data);
                protocol = read.next();
                switch (protocol)
                {
                    case "photo":
                        int size = read.nextInt();
                        byte [] image = new byte[size];
                        int offset = (protocol + " " + size + " ").getBytes().length;
                        System.arraycopy(recvPack.getData(), offset, image, 0, image.length);
                        new Thread(new ImageCommandReceived(0, image)).start();
                        break;
                    case "chat":
                        new Thread(new ChatMessageReceived(read.nextLine())).start();
                        break;
                    case "quit":
                        Platform.runLater(new QuitCommandReceived());
                        break;
                }
            }

        } catch (IOException ex)
        {
            Logger.getLogger(NetworkInputProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
