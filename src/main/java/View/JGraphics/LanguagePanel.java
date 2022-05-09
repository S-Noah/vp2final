package View.JGraphics;

import Model.Rep;
import com.mycompany.vp2final.Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * This class is a JPanel that renders a Rep's Languages. It creates a bar with the corresponding colors and their even proportions. Color codes the languages with their names.
 */
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
        // PIterating Variables.
        g.setFont(new Font("Courier", Font.PLAIN, 12)); 
        int i = 0;
        int x = 0;
        int y = 0;
        boolean isLeft = true;
        // Dimension Variables.
        int textH = g.getFontMetrics().getHeight();
        int halfTextH = textH / 2;
        int halfW = this.getWidth()/2;
        int textY = textH*2;
        int lastLabelX = 0;
        
        for(Rep.Language l : langs){
            if(i > 3){ // Limits to 4 languages.
                break;
            }
            String hexColor = Main.langColors.get(l.getName()); // Gets the color from the Language name color map.
            if(hexColor != null){ // If it is a listed color.
                // Fill part of the total bar.
                Color color = Color.decode(hexColor);
                int w = (int)(this.getWidth() * l.getPercent());
                g.setColor(color);
                g.fillRect(x, y, w, barHeight);
                
                int textW = g.getFontMetrics().stringWidth(l.toString());
                if(isLeft){ // If there is room on the left. Draw the color code component there.
                    g.fillRect(0, textY, boxDiam, boxDiam);
                    g.setColor(Color.WHITE);
                    g.drawString(l.toString(), boxDiam + 5, textY + halfTextH);
                    lastLabelX = textW + boxDiam*2; // Update last label so it can draw the next.
                }
                else{
                    if(lastLabelX >= halfW){
                        if(lastLabelX + boxDiam + 5 >= this.getWidth()){ // If there is room for a color code to the right. Draw the color code component there.
                            textY += textH + 5;
                            g.fillRect(0, textY, boxDiam, boxDiam);
                            g.setColor(Color.WHITE);
                            g.drawString(l.toString(), boxDiam + 5, textY + halfTextH);
                        }
                        g.fillRect(lastLabelX, textY, boxDiam, boxDiam);
                        g.setColor(Color.WHITE);
                        g.drawString(l.toString(), lastLabelX + boxDiam + 5, textY + halfTextH);
                    }
                    else{ // Drop down to a new row, Draw the color code component there.
                        g.fillRect(lastLabelX, textY, boxDiam, boxDiam);
                        g.setColor(Color.WHITE);
                        g.drawString(l.toString(), lastLabelX + boxDiam + 5, textY + halfTextH);
                        textY += textH + 5;
                    }
                }
                // Update iterating variables.
                x += w;
                isLeft = !isLeft;
                i++;
            }
        }
    }
}
