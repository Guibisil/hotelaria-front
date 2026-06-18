package views.dashboard;
import javax.swing.*;
import java.awt.*;

import java.util.HashMap;

public class Checks {

    public void showPaymentModal() {
        JFrame jfModal = new JFrame();
        JPanel jpValue = new JPanel();
        JPanel jpInfo = new JPanel();
        JLabel jlCheck = new JLabel();
        JButton btnPay = new JButton("Realizar Pagamento");

        jlCheck.setFont(new Font("Arial", Font.BOLD, 18));

        buildGuestInfo(jpInfo, jlCheck, jpValue);

        setupModalLayout(jfModal, jpValue, jpInfo, jlCheck, btnPay);

        jfModal.setSize(300, 350);
        jfModal.setLocationRelativeTo(null);
        jfModal.setVisible(true);
    }

    public void buildGuestInfo(JPanel jpInfo, JLabel check, JPanel jpValue) {
        HashMap<String, String> infoHospede = new HashMap<>();
        populateMockData(infoHospede);

        check.setText("Realizar Check-in");

        JLabel jlNome = new JLabel(infoHospede.get("nome"));
        JLabel jlQuarto = new JLabel(infoHospede.get("quarto"));
        JLabel jlDataCheckOut = new JLabel(infoHospede.get("check_out"));
        JLabel jlDataCheckIn = new JLabel(infoHospede.get("check_in"));

        jpInfo.add(new JLabel("Hóspede"));
        jpInfo.add(jlNome);
        jpInfo.add(new JLabel("Quarto"));
        jpInfo.add(jlQuarto);
        jpInfo.add(new JLabel("Entrda"));
        jpInfo.add(jlDataCheckIn);
        jpInfo.add(new JLabel("Saída"));
        jpInfo.add(jlDataCheckOut);

        jpInfo.setLayout(new GridLayout(4, 2, 10, 10));
        jpInfo.setBackground(Color.WHITE);

        JLabel jlValor = new JLabel(infoHospede.get("valor_final"));

        jpValue.add(new JLabel("Valor total:"));
        jpValue.add(jlValor);

        jpValue.setLayout(new GridLayout(1, 2, 10, 10));
        jpValue.setBackground(Color.WHITE);
    }

    public void populateMockData (HashMap<String, String> infoHospede) {
        infoHospede.put("id", "12");
        infoHospede.put("valor_final", "2000");
        infoHospede.put("check_out", "01/11/2026");
        infoHospede.put("quarto", "101");
        infoHospede.put("check_in", "01/01/2016");
        infoHospede.put("nome", "Maria");
    }

    public void setupModalLayout (JFrame jfModal, JPanel jpValue, JPanel jpInfo, JLabel jlCheck, JButton btnPay) {
        jfModal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        gbc.weighty = 0;
        jfModal.add(jlCheck, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        jfModal.add(new JLabel("Confirme os dados e pagamento"), gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;
        jfModal.add(jpInfo, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0;
        jfModal.add(new JLabel("Pagamento"), gbc);

        gbc.gridy = 4;
        gbc.weighty = 0;
        jfModal.add(jpValue, gbc);

        gbc.gridy = 5;
        gbc.weighty = 0;
        jfModal.add(btnPay, gbc);
    }
}
