
package networkingudp;
//@author Kyle Loveless, Gerardo Paleo, Matthew OBzera
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;




// The SendChatMessageHandler needs access to  the 
// ChatMessageTextField and the ChatHistoryTextArea
class SendChatMessageHandler implements EventHandler<ActionEvent>
{    
    @Override
    public void handle(ActionEvent event)
    {
        try
        {
            String message = Globals.chatMessageTextField.getText();
            Globals.chatMessageTextField.clear();
            Globals.chatHistoryTextArea.appendText(String.format("Me: %s\n", message));
            
            // Send to the other side
            byte[] sendBuffer = ("chat " + message).getBytes();      
            DatagramPacket sendPack = new DatagramPacket(sendBuffer, 0, sendBuffer.length);
            Globals.socket.send(sendPack);
        } catch (IOException ex)
        {
            Logger.getLogger(SendChatMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}