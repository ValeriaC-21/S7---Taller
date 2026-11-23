package ec.udla.zodiaco.vista;

import ec.udla.zodiaco.modelo.Caballero;
import ec.udla.zodiaco.modelo.Rango;
import ec.udla.zodiaco.servicio.CalculadoraFinanciera;
import ec.udla.zodiaco.servicio.GestorCaballeros;
import ec.udla.zodiaco.servicio.Informe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Optional;

public class VentanaSantuario extends JFrame {
    private final GestorCaballeros gestor;

    // Componentes enlazados con VentanaSantuario.form (IntelliJ Swing UI Designer)
    private JPanel panelRaiz;
    private JPanel panelEncabezado;
    private JLabel lblTituloBanner;
    private JLabel lblSubtituloBanner;
    private JTabbedPane pestanas;
    private JPanel tabRegistro;
    private JPanel tabListado;
    private JPanel tabInforme;

    private JTextField txtId;
    private JTextField txtNombre;
    private JComboBox<Rango> cmbRango;
    private JTextField txtConstelacion;
    private JSpinner spnPoder;
    private JTextField txtMision;
    private JSpinner spnDificultad;
    private JTextField txtRecompensa;

    private JLabel lblEscudo;
    private JLabel lblRangoPreview;
    private JLabel lblCalculoPreview;

    private JButton btnBuscarId;
    private JButton btnLimpiar;
    private JButton btnModificar;
    private JButton btnRegistrar;

    private JTable tblCaballeros;
    private JLabel lblResumenListado;
    private JButton btnEditarSeleccionado;

    private JComboBox<CalculadoraFinanciera.MetodoImpuesto> cmbMetodoImpuesto;
    private JButton btnGenerarInforme;
    private JTable tblInforme;
    private JLabel lblTotalesInforme;

    private JLabel lblEstado;

    private DefaultTableModel modeloListado;
    private DefaultTableModel modeloInforme;
    private IconoRango iconoRango;

    public VentanaSantuario(GestorCaballeros gestor) {
        this.gestor = gestor;
        if (panelRaiz == null) {
            inicializarComponentesManual();
        }
        configurarUI();
        configurarEventos();
        refrescarTodo();
    }

    /**
     * Fallback automático en caso de compilar con javac fuera de IntelliJ
     * (cuando el bytecode del .form no fue instrumentado por el IDE).
     */
    private void inicializarComponentesManual() {
        panelRaiz = new JPanel(new BorderLayout(0, 6));
        panelRaiz.setBorder(new EmptyBorder(0, 0, 6, 0));

        panelEncabezado = new JPanel(new GridLayout(2, 1, 0, 2));
        panelEncabezado.setBackground(new Color(25, 36, 70));
        panelEncabezado.setBorder(new EmptyBorder(12, 16, 12, 16));
        lblTituloBanner = new JLabel("SANTUARIO DE ATENA · Rol de Misiones");
        lblTituloBanner.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTituloBanner.setForeground(new Color(237, 191, 73));
        lblSubtituloBanner = new JLabel("Registro de caballeros, misiones, aporte al Fondo del Santuario e impuesto del Reino");
        lblSubtituloBanner.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtituloBanner.setForeground(new Color(224, 224, 224));
        panelEncabezado.add(lblTituloBanner);
        panelEncabezado.add(lblSubtituloBanner);
        panelRaiz.add(panelEncabezado, BorderLayout.NORTH);

        pestanas = new JTabbedPane();

        // Tab 1: Registro / Modificar
        tabRegistro = new JPanel(new BorderLayout(12, 10));
        tabRegistro.setBorder(new EmptyBorder(12, 14, 10, 14));
        JPanel pnlForm = new JPanel(new GridLayout(8, 2, 10, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Datos del caballero y de su misión"));
        txtId = new JTextField();
        txtNombre = new JTextField();
        cmbRango = new JComboBox<>();
        txtConstelacion = new JTextField();
        spnPoder = new JSpinner();
        txtMision = new JTextField();
        spnDificultad = new JSpinner();
        txtRecompensa = new JTextField();

        pnlForm.add(new JLabel("ID del caballero *")); pnlForm.add(txtId);
        pnlForm.add(new JLabel("Nombre *")); pnlForm.add(txtNombre);
        pnlForm.add(new JLabel("Rango *")); pnlForm.add(cmbRango);
        pnlForm.add(new JLabel("Constelación *")); pnlForm.add(txtConstelacion);
        pnlForm.add(new JLabel("Nivel de poder (1-10) *")); pnlForm.add(spnPoder);
        pnlForm.add(new JLabel("Misión asignada *")); pnlForm.add(txtMision);
        pnlForm.add(new JLabel("Dificultad de la misión (1-5) *")); pnlForm.add(spnDificultad);
        pnlForm.add(new JLabel("Recompensa (monedas doradas) *")); pnlForm.add(txtRecompensa);

        JPanel pnlPreview = new JPanel(new BorderLayout(8, 10));
        pnlPreview.setBorder(BorderFactory.createTitledBorder("Vista previa"));
        pnlPreview.setPreferredSize(new Dimension(260, 0));
        lblEscudo = new JLabel("", SwingConstants.CENTER);
        lblRangoPreview = new JLabel("Rango: Bronce");
        lblCalculoPreview = new JLabel("<html>Ingrese la recompensa para ver el aporte, el impuesto y la neta.</html>");
        JPanel pnlPreviewCentro = new JPanel(new BorderLayout(0, 8));
        pnlPreviewCentro.setBorder(new EmptyBorder(8, 10, 8, 10));
        pnlPreviewCentro.add(lblRangoPreview, BorderLayout.NORTH);
        pnlPreviewCentro.add(lblCalculoPreview, BorderLayout.CENTER);
        pnlPreview.add(lblEscudo, BorderLayout.NORTH);
        pnlPreview.add(pnlPreviewCentro, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
        btnBuscarId = new JButton("Buscar por ID");
        btnLimpiar = new JButton("Limpiar");
        btnModificar = new JButton("Modificar");
        btnRegistrar = new JButton("Registrar");
        pnlBotones.add(btnBuscarId);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnModificar);
        pnlBotones.add(btnRegistrar);

        tabRegistro.add(pnlForm, BorderLayout.CENTER);
        tabRegistro.add(pnlPreview, BorderLayout.EAST);
        tabRegistro.add(pnlBotones, BorderLayout.SOUTH);

        // Tab 2: Listado
        tabListado = new JPanel(new BorderLayout(8, 8));
        tabListado.setBorder(new EmptyBorder(10, 12, 10, 12));
        tblCaballeros = new JTable();
        tabListado.add(new JScrollPane(tblCaballeros), BorderLayout.CENTER);
        JPanel pnlPieListado = new JPanel(new BorderLayout());
        lblResumenListado = new JLabel("0 caballero(s) registrado(s)");
        btnEditarSeleccionado = new JButton("Editar seleccionado");
        pnlPieListado.add(lblResumenListado, BorderLayout.WEST);
        pnlPieListado.add(btnEditarSeleccionado, BorderLayout.EAST);
        tabListado.add(pnlPieListado, BorderLayout.SOUTH);

        // Tab 3: Informe
        tabInforme = new JPanel(new BorderLayout(8, 8));
        tabInforme.setBorder(new EmptyBorder(10, 12, 10, 12));
        JPanel pnlSupInforme = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        pnlSupInforme.add(new JLabel("Cálculo del impuesto:"));
        cmbMetodoImpuesto = new JComboBox<>();
        btnGenerarInforme = new JButton("Generar informe");
        pnlSupInforme.add(cmbMetodoImpuesto);
        pnlSupInforme.add(btnGenerarInforme);
        tblInforme = new JTable();
        lblTotalesInforme = new JLabel("TOTALES  Recompensas: 0.00 | Aportes: 0.00 | Impuestos: 0.00 | Neto: 0.00");
        tabInforme.add(pnlSupInforme, BorderLayout.NORTH);
        tabInforme.add(new JScrollPane(tblInforme), BorderLayout.CENTER);
        tabInforme.add(lblTotalesInforme, BorderLayout.SOUTH);

        pestanas.addTab("1. Registrar / Modificar", tabRegistro);
        pestanas.addTab("2. Listado de caballeros", tabListado);
        pestanas.addTab("3. Informe financiero", tabInforme);
        panelRaiz.add(pestanas, BorderLayout.CENTER);

        lblEstado = new JLabel(" ");
        lblEstado.setBorder(new EmptyBorder(0, 12, 0, 12));
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelRaiz.add(lblEstado, BorderLayout.SOUTH);
    }

    private void configurarUI() {
        setTitle("Santuario de Atena - Rol de Misiones (Java 21 + IntelliJ Swing UI Designer)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panelRaiz);
        setSize(960, 640);
        setLocationRelativeTo(null);

        // Barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCargarEjemplo = new JMenuItem("Cargar datos de ejemplo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemCargarEjemplo.addActionListener(e -> {
            int n = gestor.cargarDatosDeEjemplo();
            refrescarTodo();
            mostrarEstado("✔  Se cargaron " + n + " caballero(s) de ejemplo. Total: " + gestor.cantidad() + ".", false);
        });
        itemSalir.addActionListener(e -> dispose());
        menuArchivo.add(itemCargarEjemplo);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // Modelos de controles
        cmbRango.setModel(new DefaultComboBoxModel<>(Rango.values()));
        spnPoder.setModel(new SpinnerNumberModel(5, 1, 10, 1));
        spnDificultad.setModel(new SpinnerNumberModel(3, 1, 5, 1));
        cmbMetodoImpuesto.setModel(new DefaultComboBoxModel<>(CalculadoraFinanciera.MetodoImpuesto.values()));

        iconoRango = new IconoRango(Rango.BRONCE, 84);
        lblEscudo.setIcon(iconoRango);

        // Tabla Listado
        modeloListado = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Rango", "Constelación", "Poder", "Misión asignada", "Dific.", "Recompensa"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblCaballeros.setModel(modeloListado);
        tblCaballeros.setRowHeight(24);
        tblCaballeros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCaballeros.setAutoCreateRowSorter(true);

        // Tabla Informe
        modeloInforme = new DefaultTableModel(
            new String[]{"Nombre", "Rango", "Constelación", "Poder", "Misión asignada", "Recompensa", "Aporte Fondo", "Impuesto Reino", "Neta a recibir"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblInforme.setModel(modeloInforme);
        tblInforme.setRowHeight(24);

        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tblCaballeros.getColumnModel().getColumn(7).setCellRenderer(derecha);
        for (int col = 5; col <= 8; col++) {
            tblInforme.getColumnModel().getColumn(col).setCellRenderer(derecha);
        }
    }

    private void configurarEventos() {
        cmbRango.addActionListener(e -> actualizarVistaPrevia());
        txtRecompensa.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { actualizarVistaPrevia(); }
            @Override public void removeUpdate(DocumentEvent e) { actualizarVistaPrevia(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizarVistaPrevia(); }
        });

        btnRegistrar.addActionListener(e -> accionRegistrar());
        btnModificar.addActionListener(e -> accionModificar());
        btnBuscarId.addActionListener(e -> accionBuscar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEditarSeleccionado.addActionListener(e -> accionEditarSeleccionado());
        btnGenerarInforme.addActionListener(e -> refrescarInforme());
        cmbMetodoImpuesto.addActionListener(e -> {
            gestor.getCalculadora().setMetodo((CalculadoraFinanciera.MetodoImpuesto) cmbMetodoImpuesto.getSelectedItem());
            refrescarInforme();
            actualizarVistaPrevia();
        });
    }

    private int leerId() {
        String raw = txtId.getText().trim();
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("El ID debe ser un número entero positivo.");
        }
        try {
            int id = Integer.parseInt(raw);
            if (id <= 0) throw new NumberFormatException();
            return id;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El ID debe ser un número entero positivo.");
        }
    }

    private BigDecimal leerRecompensa() {
        String raw = txtRecompensa.getText().trim().replace(",", "");
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("La recompensa debe ser un número (ej.: 150000).");
        }
        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La recompensa debe ser un número (ej.: 150000).");
        }
    }

    private void accionRegistrar() {
        try {
            int id = leerId();
            BigDecimal rec = leerRecompensa();
            Caballero c = gestor.registrar(
                id, txtNombre.getText(), (Rango) cmbRango.getSelectedItem(),
                txtConstelacion.getText(), (int) spnPoder.getValue(),
                txtMision.getText(), (int) spnDificultad.getValue(), rec
            );
            refrescarTodo();
            mostrarEstado("✔  Caballero registrado: " + c.getNombre() + " (ID " + c.getId() + ").", false);
        } catch (Exception ex) {
            mostrarEstado("✖  " + ex.getMessage(), true);
        }
    }

    private void accionModificar() {
        try {
            int id = leerId();
            BigDecimal rec = leerRecompensa();
            Caballero c = gestor.modificar(
                id, txtNombre.getText(), (Rango) cmbRango.getSelectedItem(),
                txtConstelacion.getText(), (int) spnPoder.getValue(),
                txtMision.getText(), (int) spnDificultad.getValue(), rec
            );
            refrescarTodo();
            mostrarEstado("✔  Datos modificados: " + c.getNombre() + " (ID " + c.getId() + ").", false);
        } catch (Exception ex) {
            mostrarEstado("✖  " + ex.getMessage(), true);
        }
    }

    private void accionBuscar() {
        try {
            int id = leerId();
            Optional<Caballero> opt = gestor.buscar(id);
            if (opt.isEmpty()) {
                mostrarEstado("✖  No existe un caballero con el ID " + id + ".", true);
                return;
            }
            cargarEnFormulario(opt.get());
            mostrarEstado("✔  Caballero cargado. Edite los campos y pulse «Modificar».", false);
        } catch (Exception ex) {
            mostrarEstado("✖  " + ex.getMessage(), true);
        }
    }

    private void accionEditarSeleccionado() {
        int filaVista = tblCaballeros.getSelectedRow();
        if (filaVista < 0) {
            mostrarEstado("✖  Seleccione un caballero de la tabla para editar.", true);
            return;
        }
        int filaModelo = tblCaballeros.convertRowIndexToModel(filaVista);
        int id = (int) modeloListado.getValueAt(filaModelo, 0);
        gestor.buscar(id).ifPresent(c -> {
            cargarEnFormulario(c);
            pestanas.setSelectedIndex(0);
            mostrarEstado("✔  Editando caballero ID " + c.getId() + " (" + c.getNombre() + ").", false);
        });
    }

    private void cargarEnFormulario(Caballero c) {
        txtId.setText(String.valueOf(c.getId()));
        txtNombre.setText(c.getNombre());
        cmbRango.setSelectedItem(c.getRango());
        txtConstelacion.setText(c.getConstelacion());
        spnPoder.setValue(c.getNivelPoder());
        txtMision.setText(c.getMision());
        spnDificultad.setValue(c.getNivelDificultad());
        txtRecompensa.setText(c.getRecompensa().stripTrailingZeros().toPlainString());
        actualizarVistaPrevia();
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        cmbRango.setSelectedItem(Rango.BRONCE);
        txtConstelacion.setText("");
        spnPoder.setValue(5);
        txtMision.setText("");
        spnDificultad.setValue(3);
        txtRecompensa.setText("");
        actualizarVistaPrevia();
        mostrarEstado(" ", false);
    }

    private void actualizarVistaPrevia() {
        Rango r = (Rango) cmbRango.getSelectedItem();
        if (r == null) r = Rango.BRONCE;
        iconoRango.setRango(r);
        lblEscudo.repaint();
        lblRangoPreview.setText("<html><b>Rango:</b> " + r + "</html>");

        try {
            BigDecimal rec = leerRecompensa();
            if (rec.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException();
            CalculadoraFinanciera.Resultado res = gestor.getCalculadora().calcular(rec);
            lblCalculoPreview.setText(
                "<html>Recompensa: <b>" + Formato.moneda(res.recompensa()) + "</b><br>" +
                "Aporte al Fondo (10%): " + Formato.moneda(res.aporte()) + "<br>" +
                "Impuesto del Reino: " + Formato.moneda(res.impuesto()) + "<hr>" +
                "<b>Recompensa neta: " + Formato.moneda(res.neta()) + "</b></html>"
            );
        } catch (Exception ex) {
            lblCalculoPreview.setText("<html><font color='gray'>Ingrese la recompensa para ver el aporte, el impuesto y la neta.</font></html>");
        }
    }

    private void refrescarTodo() {
        refrescarListado();
        refrescarInforme();
        actualizarVistaPrevia();
    }

    private void refrescarListado() {
        modeloListado.setRowCount(0);
        for (Caballero c : gestor.listar()) {
            modeloListado.addRow(new Object[]{
                c.getId(), c.getNombre(), c.getRango(), c.getConstelacion(),
                c.getNivelPoder(), c.getMision(), c.getNivelDificultad(), Formato.moneda(c.getRecompensa())
            });
        }
        lblResumenListado.setText(gestor.cantidad() + " caballero(s) registrado(s)  |  Estructura: " + gestor.getNombreEstructura());
    }

    private void refrescarInforme() {
        Informe inf = gestor.generarInforme();
        modeloInforme.setRowCount(0);
        for (Informe.Fila f : inf.filas()) {
            Caballero c = f.caballero();
            modeloInforme.addRow(new Object[]{
                c.getNombre(), c.getRango(), c.getConstelacion(), c.getNivelPoder(), c.getMision(),
                Formato.moneda(c.getRecompensa()), Formato.moneda(f.aporte()),
                Formato.moneda(f.impuesto()), Formato.moneda(f.neta())
            });
        }
        lblTotalesInforme.setText(
            "TOTALES   Recompensas: " + Formato.moneda(inf.totalRecompensa()) +
            "  |  Aportes: " + Formato.moneda(inf.totalAporte()) +
            "  |  Impuestos: " + Formato.moneda(inf.totalImpuesto()) +
            "  |  Neto: " + Formato.moneda(inf.totalNeta())
        );
    }

    public void mostrarEstado(String mensaje, boolean error) {
        lblEstado.setForeground(error ? new Color(180, 20, 20) : new Color(18, 122, 42));
        lblEstado.setText(mensaje);
    }

    // Métodos auxiliares para pruebas automatizadas de interfaz
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JComboBox<Rango> getCmbRango() { return cmbRango; }
    public JTextField getTxtConstelacion() { return txtConstelacion; }
    public JSpinner getSpnPoder() { return spnPoder; }
    public JTextField getTxtMision() { return txtMision; }
    public JSpinner getSpnDificultad() { return spnDificultad; }
    public JTextField getTxtRecompensa() { return txtRecompensa; }
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnBuscarId() { return btnBuscarId; }
    public JLabel getLblEstado() { return lblEstado; }
    public JTable getTblCaballeros() { return tblCaballeros; }
    public JTable getTblInforme() { return tblInforme; }
    public JComboBox<CalculadoraFinanciera.MetodoImpuesto> getCmbMetodoImpuesto() { return cmbMetodoImpuesto; }
}
