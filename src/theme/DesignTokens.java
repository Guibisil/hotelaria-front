package theme;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;

public class DesignTokens {

    public static class ColorPalette {
        // Material Design 3 inspired colors
        public static final Color PRIMARY = new Color(0, 97, 164); // M3 Blue Primary
        public static final Color PRIMARY_HOVER = new Color(26, 115, 232); // Lighter for hover
        public static final Color ON_PRIMARY = Color.WHITE;

        public static final Color SECONDARY = new Color(83, 95, 112); // M3 Secondary
        public static final Color SECONDARY_HOVER = new Color(103, 116, 134);
        public static final Color ON_SECONDARY = Color.WHITE;

        public static final Color DANGER = new Color(186, 26, 26); // M3 Error
        public static final Color DANGER_HOVER = new Color(208, 43, 43);
        public static final Color ON_DANGER = Color.WHITE;

        public static final Color SUCCESS = new Color(20, 108, 46); // M3 Success equivalent
        public static final Color SUCCESS_HOVER = new Color(30, 133, 61);
        public static final Color ON_SUCCESS = Color.WHITE;

        public static final Color BACKGROUND = new Color(253, 251, 255); // M3 Surface/Background
        public static final Color SURFACE = Color.WHITE;
        public static final Color SURFACE_VARIANT = new Color(223, 226, 235); // M3 Surface Variant
        
        public static final Color TEXT_PRIMARY = new Color(26, 28, 30); // M3 On-Surface
        public static final Color TEXT_SECONDARY = new Color(67, 71, 78); // M3 On-Surface Variant
        
        public static final Color BORDER = new Color(115, 119, 127); // M3 Outline
        public static final Color BORDER_VARIANT = new Color(195, 198, 207); // M3 Outline Variant

        public static final Color TIMELINE_SELECTED = new Color(206, 229, 255); // Secondary Container
        public static final Color TIMELINE_CELL_PRIMARY = new Color(0, 97, 164);
        public static final Color TIMELINE_HEADER_BG = new Color(241, 244, 249);
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

        public static final Font TITLE_FONT = archivoBold.deriveFont(22f);
        public static final Font BUTTON_FONT = archivoBold.deriveFont(13f);
        public static final Font BODY_FONT = archivoRegular.deriveFont(14f);
        public static final Font SMALL_FONT = archivoRegular.deriveFont(12f);
    }

    public static class Spacing {
        public static final int XS = 4;
        public static final int SM = 8;
        public static final int MD = 16;
        public static final int LG = 24;
        public static final int XL = 32;
    }
}
