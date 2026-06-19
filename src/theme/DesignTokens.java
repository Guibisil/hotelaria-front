package theme;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;

public class DesignTokens {

    public static class ColorPalette {
        // Modernized Blue Palette (Tailwind Slate/Blue inspired)
        public static final Color PRIMARY = new Color(10, 102, 194); // Premium blue
        public static final Color PRIMARY_HOVER = new Color(0, 86, 168);
        public static final Color ON_PRIMARY = Color.WHITE;

        public static final Color SECONDARY = new Color(100, 116, 139); // Slate 500
        public static final Color SECONDARY_HOVER = new Color(71, 85, 105); // Slate 600
        public static final Color ON_SECONDARY = Color.WHITE;

        public static final Color DANGER = new Color(239, 68, 68); // Red 500
        public static final Color DANGER_HOVER = new Color(220, 38, 38); // Red 600
        public static final Color ON_DANGER = Color.WHITE;

        public static final Color SUCCESS = new Color(34, 197, 94); // Green 500
        public static final Color SUCCESS_HOVER = new Color(22, 163, 74); // Green 600
        public static final Color ON_SUCCESS = Color.WHITE;

        public static final Color WARNING = new Color(245, 158, 11); // Amber 500
        public static final Color WARNING_HOVER = new Color(217, 119, 6); // Amber 600
        public static final Color ON_WARNING = Color.WHITE;

        public static final Color BACKGROUND = new Color(248, 250, 252); // Slate 50
        public static final Color SURFACE = Color.WHITE;
        public static final Color SURFACE_VARIANT = new Color(241, 245, 249); // Slate 100
        
        public static final Color TEXT_PRIMARY = new Color(15, 23, 42); // Slate 900
        public static final Color TEXT_SECONDARY = new Color(71, 85, 105); // Slate 600
        
        public static final Color BORDER = new Color(203, 213, 225); // Slate 300
        public static final Color BORDER_VARIANT = new Color(226, 232, 240); // Slate 200

        public static final Color TIMELINE_SELECTED = new Color(219, 234, 254); // Blue 100
        public static final Color TIMELINE_CELL_PRIMARY = PRIMARY;
        public static final Color TIMELINE_HEADER_BG = SURFACE_VARIANT;
        
        public static final Color SIDEBAR_BG = new Color(15, 23, 42); // Slate 900
        public static final Color SIDEBAR_ITEM_HOVER = new Color(30, 41, 59); // Slate 800
        public static final Color SIDEBAR_ITEM_ACTIVE = new Color(51, 65, 85); // Slate 700
        public static final Color SIDEBAR_TEXT = new Color(248, 250, 252); // Slate 50
        public static final Color SIDEBAR_TEXT_MUTED = new Color(148, 163, 184); // Slate 400
    }

    public static class Typography {
        private static Font archivoRegular = new Font("SansSerif", Font.PLAIN, 14);
        private static Font archivoBold = new Font("SansSerif", Font.BOLD, 14);

        static {
            try {
                InputStream regularStream = DesignTokens.class.getResourceAsStream("/fonts/Archivo-Regular.ttf");
                if (regularStream != null) {
                    archivoRegular = Font.createFont(Font.TRUETYPE_FONT, regularStream);
                }
                
                InputStream boldStream = DesignTokens.class.getResourceAsStream("/fonts/Archivo-Bold.ttf");
                if (boldStream != null) {
                    archivoBold = Font.createFont(Font.TRUETYPE_FONT, boldStream);
                }
            } catch (Exception e) {
                System.err.println("Aviso: Nao foi possivel carregar a fonte Archivo. Usando fallback.");
                e.printStackTrace();
            }
        }

        public static final Font TITLE_FONT = archivoBold.deriveFont(24f);
        public static final Font SUBTITLE_FONT = archivoBold.deriveFont(18f);
        public static final Font BUTTON_FONT = archivoBold.deriveFont(14f);
        public static final Font BODY_FONT = archivoRegular.deriveFont(14f);
        public static final Font SMALL_FONT = archivoRegular.deriveFont(12f);
    }

    public static class Spacing {
        public static final int XS = 4;
        public static final int SM = 8;
        public static final int MD = 16;
        public static final int LG = 24;
        public static final int XL = 32;
        public static final int XXL = 48;
    }
    
    public static class Radius {
        public static final int SMALL = 4;
        public static final int MEDIUM = 8;
        public static final int LARGE = 12;
    }
}
