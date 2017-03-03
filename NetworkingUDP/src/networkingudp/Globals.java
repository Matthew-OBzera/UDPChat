
package networkingudp;

/**
  _____                    _  _       _     ___  _
 |_   _|___  __ _  _ __   | \| | ___ | |_  / __|| |_   ___  _ _  ___
   | | / -_)/ _` || '  \  | .` |/ _ \|  _| \__ \| ' \ / _ \| '_|/ -_)
   |_| \___|\__,_||_|_|_| |_|\_|\___/ \__| |___/|_||_|\___/|_|  \___|

 Kyle Loveless, Gerardo Paleo, Matthew OBzera

 */
import java.net.DatagramSocket;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
public class Globals
{       
    // These are used for network communication   
    static DatagramSocket socket;
    static byte[] buffer;
    // Gui components that have to be globally accesible
    static TextArea chatHistoryTextArea;
    static TextField chatMessageTextField;
    static ImageView imageLocalView;
    static ImageView imageRemoteView;
}
