package views.reservas;

import DTO.HospedeDTO;
import DTO.QuartoDTO;
import DTO.ReservaDTO;
import components.*;
import config.DIContainer;
import controllers.NovaReservaController;
import theme.DesignTokens.ColorPalette;
import theme.DesignTokens.Spacing;
import views.hospedes.TelaNovoHospede;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TelaNovaReserva extends DsModal {

    private final NovaReservaController controller;
    private final CardLayout cardLayout;
    private final JPanel wizardPanel;
    
    // Passos
    private static final String PASSO_1 = "HOSPEDE";
    private static final String PASSO_2 = "PERIODO";
    private static final String PASSO_3 = "QUARTO";
    private static final String PASSO_4 = "CONFIRMACAO";

    // Componentes Passo 1
    private JPanel painelListaHospedes;
    private DsTextField txtBuscaHospede;
    private List<HospedeDTO> todosHospedesCache = new ArrayList<>();
    private Long hospedeSelecionadoId;
    private String hospedeSelecionadoNome;

    // Componentes Passo 2 (Periodo)
    private DsFormattedTextField txtDataEntrada;
    private DsFormattedTextField txtDataSaida;
    private DsLabel lblResumoDias;
    private DsLabel lblQuartosDisponiveis;
    private javax.swing.Timer debounceBuscaQuartos;
    
    // Componentes Passo 3 (Quarto)
    private DsTable tabelaQuartos;
    private DefaultTableModel modeloQuartos;
    private Integer quartoSelecionadoId;
    private String quartoSelecionadoNome;
    private double quartoSelecionadoDiaria = 0.0;
    private DsTitleLabel lblValorEstimado;
    private List<QuartoDTO> quartosDisponiveisCache = new ArrayList<>();

    // Componentes Passo 4
    private DsLabel lblResumoHospede;
    private DsLabel lblResumoQuarto;
    private DsLabel lblResumoPeriodo;
    private DsLabel lblResumoTotal;

    public TelaNovaReserva(JFrame parent, NovaReservaController controller) {
        super("Nova Reserva", 600, 600);
        this.controller = controller;

        setModalLayout(new BorderLayout());

        cardLayout = new CardLayout();
        wizardPanel = new JPanel(cardLayout);
        wizardPanel.setBackground(ColorPalette.BACKGROUND);

        initPasso1();
        initPasso2();
        initPasso3();
        initPasso4();

        addComponent(wizardPanel, BorderLayout.CENTER);

        carregarHospedes(null);
    }

    private JPanel criarPainelNavegacao(String anterior, String proximo, boolean isUltimo) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, Spacing.MD, Spacing.MD));
        painel.setBackground(ColorPalette.BACKGROUND);

        if (anterior != null) {
            DsButton btnVoltar = new DsButton("Voltar", DsButton.ButtonType.SECONDARY);
            btnVoltar.addActionListener(e -> cardLayout.show(wizardPanel, anterior));
            painel.add(btnVoltar);
        }

        DsButton btnProximo = new DsButton(isUltimo ? "Finalizar Reserva" : "Continuar", DsButton.ButtonType.PRIMARY);
        btnProximo.addActionListener(e -> {
            if (isUltimo) {
                finalizarReserva();
            } else {
                if (validarPasso(proximo)) {
                    if (proximo.equals(PASSO_4)) {
                        atualizarResumo();
                    }
                    cardLayout.show(wizardPanel, proximo);
                }
            }
        });
        painel.add(btnProximo);

        return painel;
    }

    private void initPasso1() {
        JPanel painel = new JPanel(new BorderLayout(Spacing.MD, Spacing.MD));
        painel.setBackground(ColorPalette.BACKGROUND);
        painel.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBackground(ColorPalette.BACKGROUND);
        
        DsLabel lblSubtitle = new DsLabel("Crie uma nova reserva de um quarto para um hóspede.");
        lblSubtitle.setBorder(new EmptyBorder(0, 0, Spacing.MD, 0));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlHeader.add(lblSubtitle);
        
        DsTitleLabel lblNovo = new DsTitleLabel("Cadastre um novo hóspede");
        lblNovo.setBorder(new EmptyBorder(0, 0, Spacing.SM, 0));
        lblNovo.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlHeader.add(lblNovo);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBtn.setBackground(ColorPalette.BACKGROUND);
        pnlBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        DsButton btnCadastrar = new DsButton("Cadastrar hóspede", DsButton.ButtonType.PRIMARY);
        btnCadastrar.addActionListener(e -> {
            TelaNovoHospede modal = new TelaNovoHospede(
                DIContainer.getInstance().getHospedeController(),
                () -> carregarHospedes(() -> cardLayout.show(wizardPanel, PASSO_2))
            );
            modal.setVisible(true);
        });
        pnlBtn.add(btnCadastrar);
        pnlHeader.add(pnlBtn);
        
        DsTitleLabel lblOu = new DsTitleLabel("Ou selecione um hóspede já cadastrado");
        lblOu.setBorder(new EmptyBorder(Spacing.LG, 0, Spacing.SM, 0));
        lblOu.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlHeader.add(lblOu);

        txtBuscaHospede = new DsTextField();
        txtBuscaHospede.setText("Busque por hóspedes");
        txtBuscaHospede.setForeground(ColorPalette.TEXT_SECONDARY);
        txtBuscaHospede.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtBuscaHospede.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtBuscaHospede.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtBuscaHospede.getText().equals("Busque por hóspedes")) {
                    txtBuscaHospede.setText("");
                    txtBuscaHospede.setForeground(ColorPalette.TEXT_PRIMARY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtBuscaHospede.getText().isEmpty()) {
                    txtBuscaHospede.setForeground(ColorPalette.TEXT_SECONDARY);
                    txtBuscaHospede.setText("Busque por hóspedes");
                }
            }
        });
        txtBuscaHospede.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { atualizarListaHospedesUI(); }
            public void removeUpdate(DocumentEvent e) { atualizarListaHospedesUI(); }
            public void changedUpdate(DocumentEvent e) { atualizarListaHospedesUI(); }
        });

        pnlHeader.add(txtBuscaHospede);
        
        painel.add(pnlHeader, BorderLayout.NORTH);

        painelListaHospedes = new JPanel();
        painelListaHospedes.setLayout(new BoxLayout(painelListaHospedes, BoxLayout.Y_AXIS));
        painelListaHospedes.setBackground(ColorPalette.BACKGROUND);

        JScrollPane scroll = new JScrollPane(painelListaHospedes);
        scroll.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, 0, 0, 0));
        scroll.getViewport().setBackground(ColorPalette.BACKGROUND);
        painel.add(scroll, BorderLayout.CENTER);

        painel.add(criarPainelNavegacao(null, PASSO_2, false), BorderLayout.SOUTH);
        wizardPanel.add(painel, PASSO_1);
    }

    private void atualizarListaHospedesUI() {
        painelListaHospedes.removeAll();
        String query = txtBuscaHospede.getText().toLowerCase();
        if (query.equals("busque por hóspedes")) query = "";

        for (HospedeDTO h : todosHospedesCache) {
            if (query.isEmpty() || h.getName().toLowerCase().contains(query)) {
                painelListaHospedes.add(criarGuestCard(h));
            }
        }
        painelListaHospedes.revalidate();
        painelListaHospedes.repaint();
    }

    private JPanel criarGuestCard(HospedeDTO h) {
        JPanel card = new JPanel(new BorderLayout(Spacing.MD, 0));
        boolean isSelected = h.getId().equals(hospedeSelecionadoId);
        Color bg = isSelected ? ColorPalette.SECONDARY : ColorPalette.BACKGROUND;
        card.setBackground(bg);
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_VARIANT),
            BorderFactory.createEmptyBorder(Spacing.MD, Spacing.SM, Spacing.MD, Spacing.SM)
        ));

        JLabel lblAvatar = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("src/resources/avatar.png");
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            lblAvatar.setText(" \uD83D\uDC64 ");
            lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            lblAvatar.setForeground(ColorPalette.TEXT_SECONDARY);
        }
        lblAvatar.setBorder(new EmptyBorder(0, Spacing.SM, 0, Spacing.MD));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(bg);
        infoPanel.setOpaque(false);
        
        DsLabel lblNome = new DsLabel(h.getName());
        lblNome.setFont(theme.DesignTokens.Typography.TITLE_FONT);
        if (isSelected) lblNome.setForeground(ColorPalette.ON_SECONDARY);
        
        String cpfMasked = h.getCpf();
        if(cpfMasked != null && cpfMasked.length() >= 11) {
            cpfMasked = cpfMasked.substring(0, 4) + "***.***-" + cpfMasked.substring(cpfMasked.length()-2);
        }
        
        DsLabel lblDetalhes = new DsLabel("CPF: " + cpfMasked + "    E-mail: " + h.getEmail());
        if (isSelected) lblDetalhes.setForeground(ColorPalette.ON_SECONDARY);
        else lblDetalhes.setForeground(ColorPalette.TEXT_SECONDARY);
        lblDetalhes.setFont(theme.DesignTokens.Typography.SMALL_FONT);

        infoPanel.add(lblNome);
        infoPanel.add(lblDetalhes);

        JLabel lblSeta = new JLabel(" > ");
        lblSeta.setForeground(ColorPalette.TEXT_SECONDARY);

        card.add(lblAvatar, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(lblSeta, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hospedeSelecionadoId = h.getId();
                hospedeSelecionadoNome = h.getName();
                atualizarListaHospedesUI();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if(!h.getId().equals(hospedeSelecionadoId)) {
                    card.setBackground(ColorPalette.SURFACE);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if(!h.getId().equals(hospedeSelecionadoId)) {
                    card.setBackground(ColorPalette.BACKGROUND);
                }
            }
        });
        
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        return card;
    }

    private void initPasso2() {
        JPanel painel = new JPanel(new BorderLayout(Spacing.MD, Spacing.MD));
        painel.setBackground(ColorPalette.BACKGROUND);
        painel.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        painel.add(new DsTitleLabel("Passo 2: Período da estadia"), BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(2, 2, Spacing.MD, Spacing.MD));
        pnlForm.setBackground(ColorPalette.BACKGROUND);

        try {
            txtDataEntrada = new DsFormattedTextField(new javax.swing.text.MaskFormatter("##/##/####"));
            txtDataSaida = new DsFormattedTextField(new javax.swing.text.MaskFormatter("##/##/####"));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtDataEntrada.setText(hoje.format(fmt));
        txtDataSaida.setText(hoje.plusDays(1).format(fmt));

        pnlForm.add(new DsLabel("Data de Entrada (DD/MM/AAAA):"));
        pnlForm.add(txtDataEntrada);
        pnlForm.add(new DsLabel("Data de Saída (DD/MM/AAAA):"));
        pnlForm.add(txtDataSaida);

        lblResumoDias = new DsLabel("Dias selecionados: 1");
        lblQuartosDisponiveis = new DsLabel("Quartos disponíveis: Verificando...");
        JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, 0, 0, 0));
        pnlInfo.add(lblResumoDias);
        pnlInfo.add(lblQuartosDisponiveis);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setOpaque(false);
        pnlCenter.add(pnlForm, BorderLayout.NORTH);
        pnlCenter.add(pnlInfo, BorderLayout.CENTER);

        painel.add(pnlCenter, BorderLayout.CENTER);
        painel.add(criarPainelNavegacao(PASSO_1, PASSO_3, false), BorderLayout.SOUTH);
        wizardPanel.add(painel, PASSO_2);

        debounceBuscaQuartos = new javax.swing.Timer(500, e -> recalcularDisponibilidade());
        debounceBuscaQuartos.setRepeats(false);

        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { debounceBuscaQuartos.restart(); }
            public void removeUpdate(DocumentEvent e) { debounceBuscaQuartos.restart(); }
            public void changedUpdate(DocumentEvent e) { debounceBuscaQuartos.restart(); }
        };
        txtDataEntrada.getDocument().addDocumentListener(docListener);
        txtDataSaida.getDocument().addDocumentListener(docListener);
        recalcularDisponibilidade();
    }

    private void recalcularDisponibilidade() {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate entrada = LocalDate.parse(txtDataEntrada.getText(), fmt);
            LocalDate saida = LocalDate.parse(txtDataSaida.getText(), fmt);
            if (saida.isBefore(entrada) || saida.isEqual(entrada)) {
                lblResumoDias.setText("Datas inválidas (saída <= entrada)");
                lblQuartosDisponiveis.setText("Quartos disponíveis: 0");
                return;
            }
            long dias = ChronoUnit.DAYS.between(entrada, saida);
            lblResumoDias.setText("Dias selecionados: " + dias);
            lblQuartosDisponiveis.setText("Quartos disponíveis: Verificando...");
            controller.buscarQuartosDisponiveis(entrada, saida).thenAccept(quartos -> SwingUtilities.invokeLater(() -> {
                lblQuartosDisponiveis.setText("Quartos disponíveis: " + quartos.size());
            }));
        } catch (Exception ex) {
            lblResumoDias.setText("Data inválida ou incompleta");
            lblQuartosDisponiveis.setText("Quartos disponíveis: 0");
        }
    }

    private void initPasso3() {
        JPanel painel = new JPanel(new BorderLayout(Spacing.MD, Spacing.MD));
        painel.setBackground(ColorPalette.BACKGROUND);
        painel.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        painel.add(new DsTitleLabel("Passo 3: Selecione o quarto"), BorderLayout.NORTH);

        modeloQuartos = new DefaultTableModel(new Object[]{"Número", "Camas", "Status", "Diária"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaQuartos = new DsTable(modeloQuartos);
        
        JScrollPane scroll = new JScrollPane(tabelaQuartos);
        
        lblValorEstimado = new DsTitleLabel("Valor estimado: R$ 0,00");
        lblValorEstimado.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, 0, 0, 0));
        
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setOpaque(false);
        pnlCenter.add(scroll, BorderLayout.CENTER);
        pnlCenter.add(lblValorEstimado, BorderLayout.SOUTH);
        
        painel.add(pnlCenter, BorderLayout.CENTER);

        tabelaQuartos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabelaQuartos.getSelectedRow();
                if (row != -1) {
                    try {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate entrada = LocalDate.parse(txtDataEntrada.getText(), fmt);
                        LocalDate saida = LocalDate.parse(txtDataSaida.getText(), fmt);
                        long dias = ChronoUnit.DAYS.between(entrada, saida);
                        
                        String selectedNumber = (String) modeloQuartos.getValueAt(row, 0);
                        QuartoDTO q = quartosDisponiveisCache.stream().filter(quarto -> quarto.getNumber().equals(selectedNumber)).findFirst().orElse(null);
                        
                        if (q != null) {
                            quartoSelecionadoDiaria = q.getBaseDailyRate();
                            lblValorEstimado.setText(String.format("Valor estimado: R$ %.2f", dias * quartoSelecionadoDiaria));
                        }
                    } catch(Exception ex) {}
                } else {
                    quartoSelecionadoDiaria = 0.0;
                    lblValorEstimado.setText("Valor estimado: R$ 0,00");
                }
            }
        });

        painel.add(criarPainelNavegacao(PASSO_2, PASSO_4, false), BorderLayout.SOUTH);
        wizardPanel.add(painel, PASSO_3);
    }

    private void initPasso4() {
        JPanel painel = new JPanel(new BorderLayout(Spacing.MD, Spacing.MD));
        painel.setBackground(ColorPalette.BACKGROUND);
        painel.setBorder(BorderFactory.createEmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD));

        painel.add(new DsTitleLabel("Passo 4: Confirmação e Finalização"), BorderLayout.NORTH);

        JPanel pnlResumo = new JPanel(new GridLayout(4, 1, Spacing.SM, Spacing.SM));
        pnlResumo.setBackground(ColorPalette.BACKGROUND);

        lblResumoHospede = new DsLabel("");
        lblResumoQuarto = new DsLabel("");
        lblResumoPeriodo = new DsLabel("");
        lblResumoTotal = new DsLabel("");

        pnlResumo.add(lblResumoHospede);
        pnlResumo.add(lblResumoQuarto);
        pnlResumo.add(lblResumoPeriodo);
        pnlResumo.add(lblResumoTotal);

        painel.add(pnlResumo, BorderLayout.CENTER);
        painel.add(criarPainelNavegacao(PASSO_3, null, true), BorderLayout.SOUTH);
        wizardPanel.add(painel, PASSO_4);
    }

    private boolean validarPasso(String proximoPasso) {
        if (proximoPasso.equals(PASSO_2)) {
            if (hospedeSelecionadoId == null) {
                DsDialog.showWarning(this, "Selecione um hóspede.", "Aviso");
                return false;
            }
        } else if (proximoPasso.equals(PASSO_3)) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate entrada = LocalDate.parse(txtDataEntrada.getText(), fmt);
                LocalDate saida = LocalDate.parse(txtDataSaida.getText(), fmt);
                if (saida.isBefore(entrada) || saida.isEqual(entrada)) {
                    DsDialog.showWarning(this, "Data de saída deve ser maior que entrada.", "Aviso");
                    return false;
                }
                
                // Fetch dynamically available rooms for the selected period
                carregarQuartosDisponiveis(entrada, saida);
                
            } catch (DateTimeParseException ex) {
                DsDialog.showWarning(this, "Datas inválidas. Use DD/MM/AAAA.", "Aviso");
                return false;
            }
        } else if (proximoPasso.equals(PASSO_4)) {
            int row = tabelaQuartos.getSelectedRow();
            if (row == -1) {
                DsDialog.showWarning(this, "Selecione um quarto.", "Aviso");
                return false;
            }
            String selectedNumber = (String) modeloQuartos.getValueAt(row, 0);
            QuartoDTO q = quartosDisponiveisCache.stream().filter(quarto -> quarto.getNumber().equals(selectedNumber)).findFirst().orElse(null);
            if (q != null) {
                quartoSelecionadoId = q.getId().intValue();
                quartoSelecionadoNome = q.getNumber();
            }
        }
        return true;
    }

    private void atualizarResumo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate entrada = LocalDate.parse(txtDataEntrada.getText(), fmt);
        LocalDate saida = LocalDate.parse(txtDataSaida.getText(), fmt);
        long dias = ChronoUnit.DAYS.between(entrada, saida);

        lblResumoHospede.setText("Hóspede: " + hospedeSelecionadoNome);
        lblResumoQuarto.setText("Quarto: " + quartoSelecionadoNome);
        lblResumoPeriodo.setText(String.format("Período: %s a %s (%d dias)", txtDataEntrada.getText(), txtDataSaida.getText(), dias));
        lblResumoTotal.setText(String.format("Total: R$ %.2f", dias * quartoSelecionadoDiaria));
    }

    private void finalizarReserva() {
        DateTimeFormatter fmtLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fmtApi = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate entrada = LocalDate.parse(txtDataEntrada.getText(), fmtLocal);
        LocalDate saida = LocalDate.parse(txtDataSaida.getText(), fmtLocal);

        ReservaDTO dto = new ReservaDTO();
        dto.setGuestId(hospedeSelecionadoId);
        dto.setRoomId(quartoSelecionadoId);
        dto.setCheckinDate(entrada.format(fmtApi));
        dto.setCheckoutDate(saida.format(fmtApi));
        dto.setStatus("confirmed");

        controller.salvarReserva(dto).thenRun(() -> SwingUtilities.invokeLater(() -> {
            DsDialog.showSuccess(this, "Reserva criada com sucesso!");
            dispose();
        })).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> DsDialog.showError(this, "Erro ao criar reserva: " + ex.getMessage(), "Erro"));
            return null;
        });
    }

    private void carregarHospedes(Runnable onLoaded) {
        controller.buscarHospedes().thenAccept(hospedes -> SwingUtilities.invokeLater(() -> {
            todosHospedesCache = hospedes;
            
            if (onLoaded != null && !hospedes.isEmpty()) {
                HospedeDTO ultimo = hospedes.get(hospedes.size() - 1);
                hospedeSelecionadoId = ultimo.getId();
                hospedeSelecionadoNome = ultimo.getName();
            }
            
            atualizarListaHospedesUI();
            
            if (onLoaded != null) {
                onLoaded.run();
            }
        }));
    }

    private void carregarQuartosDisponiveis(LocalDate entrada, LocalDate saida) {
        controller.buscarQuartosDisponiveis(entrada, saida).thenAccept(quartos -> SwingUtilities.invokeLater(() -> {
            quartosDisponiveisCache = quartos;
            modeloQuartos.setRowCount(0);
            for (QuartoDTO q : quartos) {
                modeloQuartos.addRow(new Object[]{q.getNumber(), q.getBedCount(), "AVAILABLE", String.format("R$ %.2f", q.getBaseDailyRate())});
            }
            if (quartos.isEmpty()) {
                // Warning optional, handled by empty table
            }
        })).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> DsDialog.showError(this, "Erro ao buscar quartos disponíveis: " + ex.getMessage(), "Erro"));
            return null;
        });
    }
}
