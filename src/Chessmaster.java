import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chessmaster extends Applet implements ActionListener
{Board brain;JButton b1,b2,b3;
    public void init()
    {super.init();
        b1=new JButton("START NEW GAME");
        b2=new JButton("OFFER DRAW");
        b3=new JButton("RESIGN");
        b1.setBounds(800,150,170,40);
        b2.setBounds(800,250,170,40);
        b3.setBounds(800,350,170,40);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        try
        {Thread.sleep(100);
            add(b1);
            add(b2);
            add(b3);}
        catch(Exception a){}
        brain = new Board (this);
        setBackground (Color.black);
        setLayout (new BorderLayout (10,10));
        add ("Center", brain);
    }
    public void actionPerformed(ActionEvent e)
    {if(e.getSource()==b1)
    {brain.newgame();}
        if(e.getSource()==b2)
        {infoBox("Game is a draw!","Game Status");
            remove(brain);
            remove(b1);
            remove(b2);
            remove(b3);}
        if(e.getSource()==b3)
        {infoBox("Chessmaster Wins!","Game Status");
            remove(brain);
            remove(b1);
            remove(b2);
            remove(b3);}}
    public void infoBox(String infoMessage, String location)
    {JOptionPane.showMessageDialog(null, infoMessage,location, JOptionPane.INFORMATION_MESSAGE);
    }
    public Insets insets () {
        return new Insets (10,140,10,10);
    }
}
