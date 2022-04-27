package View.JGraphics;

import Model.Rep;
import com.mycompany.vp2final.Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LanguagePanel extends JPanel {
    private static int barHeight = 10;
    private static int boxDiam = 10;
    private ArrayList<Rep.Language> langs;
    public LanguagePanel(){
        super();
        langs = new ArrayList<>();
    }
    public void reset(){
        langs.clear();
        changeLangs(langs);
    }
    public void changeLangs(ArrayList<Rep.Language> langs){
        this.langs = langs;
        repaint();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("Courier", Font.PLAIN, 14)); 
        
        int x = 0;
        int y = 0;
        int textH = g.getFontMetrics().getHeight();
        int halfTextH = textH / 2;
        
        int halfW = this.getWidth()/2;
        int textY = textH*2;
        int lastLabelX = 0;
        boolean isLeft = true;
        for(Rep.Language l : langs){
            String hexColor = Main.langColors.get(l.getName());
            Color color = Color.decode(hexColor);
            int textW = g.getFontMetrics().stringWidth(l.toString());
            
            int w = (int)(this.getWidth() * l.getPercent()) + 1;
            g.setColor(color);
            g.fillRect(x, y, w, barHeight);
            if(isLeft){
                g.fillRect(0, textY, boxDiam, boxDiam);
                g.setColor(Color.WHITE);
                g.drawString(l.toString(), boxDiam + 5, textY + halfTextH);
                lastLabelX = textW + boxDiam*2;
            }
            else{
                //System.out.println(halfW + ", " + lastLabelX);
                if(lastLabelX >= halfW){
                    if(lastLabelX + boxDiam + 5 >= this.getWidth()){
                        textY += textH + 5;
                        g.fillRect(0, textY, boxDiam, boxDiam);
                        g.setColor(Color.WHITE);
                        g.drawString(l.toString(), boxDiam + 5, textY + halfTextH);
                    }
                    g.fillRect(lastLabelX, textY, boxDiam, boxDiam);
                    g.setColor(Color.WHITE);
                    g.drawString(l.toString(), lastLabelX + boxDiam + 5, textY + halfTextH);
                }
                else{
                    g.fillRect(lastLabelX, textY, boxDiam, boxDiam);
                    g.setColor(Color.WHITE);
                    g.drawString(l.toString(), lastLabelX + boxDiam + 5, textY + halfTextH);
                    textY += textH + 5;
                }
            }
            
            x += w;
            isLeft = !isLeft;
        }
    }
}
