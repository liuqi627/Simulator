import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Font;


public class MainFrame extends JFrame {

	public static JPanel contentPane;
	public static JTextField textField;
	public static JTextField textField_PC;
	public static JTextField textField_IR;
	public static JTextField textField_MAR;
	public static JTextField textField_MDR;
	public static JTextField textField_R0;
	public static JTextField textField_R1;
	public static JTextField textField_R2;
	public static JTextField textField_R3;
	public static JTextField textField_X1;
	public static JTextField textField_X2;
	public static JTextField textField_X3;
	public static JTextField textField_CurrentAddress;
	public static JTextField textField_CurrentValue;
	public static JTextField textField_SetAddress;
	public static JTextField textField_SetValue;
	public static JTextArea txtrConsole;
	private JTextField txtrProgram;

	/**
	 * Launch the application.
	 */
	public void runGUI() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

	/**
	 * Create the frame.
	 */
	public MainFrame() {//窗口的构造函数
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 931, 575);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setText("LDR 3 0 31");
		textField.setBounds(8, 105, 184, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSingleStep = new JButton("Single Step");//Single Step 的响应函数在这！这是最重要的函数！！！
		btnSingleStep.setFont(new Font("HanziPen TC", Font.PLAIN, 13));
		btnSingleStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String a = textField.getText();
				if(Helper.constructInstruction(a))//如果为true，则decode成功，那么instruction里应该存着二进制的指令！
					{
						IR.value = Instruction.command;
						Memory.memoryList.get(Integer.parseInt(PC.value, 2)).setValue(Instruction.command);
						//开始分解步骤了：
						MAR.value = PC.value;
						txtrConsole.append("\nMAR <- PC");
						
						MDR.value = Memory.memoryList.get(Integer.parseInt(MAR.value, 2)).getValue();
						txtrConsole.append("\nMDR <- M(MAR)");
						
						IR.value = MDR.value;
						txtrConsole.append("\nIR <- MDR");
						
						txtrConsole.append("\nOPCODE <- IR[0-5]\nRFI <- IR[6-7]\nIX <- IR[8-9]\nIND <- IR[10]\nADDRESS <- IR[11-15]");
						
						txtrConsole.append("\nEA <- Address");
						
						
						ALU.execute();
						txtrConsole.append("\nSuccessfully Ran!");
						txtrConsole.append("\n--------------------");
						
						refresh();
					}
			}
		});
		btnSingleStep.setBounds(188, 105, 105, 29);
		contentPane.add(btnSingleStep);
		
		JButton btnIpl = new JButton("IPL");
		JButton btnHalt = new JButton("Halt");
		
		btnIpl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MainFrame.refresh();
				MainFrame.txtrConsole.append("\nRegisters initiated.");
				MainFrame.txtrConsole.append("\nMemory initiated.");
				MainFrame.txtrConsole.append("\nISA initiated.");
				//btnIpl.setEnabled(false);
				btnIpl.setSelected(true);
				btnHalt.setSelected(false);
			}
		});
		btnIpl.setFont(new Font("HanziPen SC", Font.PLAIN, 13));
		btnIpl.setBounds(9, 27, 136, 42);
		contentPane.add(btnIpl);
		
		
		btnHalt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//init registers
				PC.value = "000000000110";//pc12位; 内存中0-5这六个地址被reserved了，所以要从第六位开始
				R0.value = "0000000000000000";
				R1.value = "0000000000000000";
				R2.value = "0000000000000000";
				R3.value = "0000000000000000";
				X1.value = "0000000000000000";
				X2.value = "0000000000000000";
				X3.value = "0000000000000000";
				MAR.value = "0000000000000000";
				MDR.value = "0000000000000000";
				
				//init memory
				Memory.memoryList.clear();
				for(Integer i=0; i<2048; i++)//0-2047的地址空间，大小位2048
				{
					MemoryItem mi = new MemoryItem();
					mi.setAddress(ALU.addZero(Integer.toBinaryString(i), 16));//memoryitem的地址存的是16位二进制数
					mi.setValue("0000000000000000");
					Memory.memoryList.add(i, mi);
				}
				refresh();
				MainFrame.txtrConsole.append("\nPlease press the IPL button to start.");
				btnIpl.setSelected(false);
				btnHalt.setSelected(true);
			}
		});
		btnHalt.setFont(new Font("HanziPen SC", Font.PLAIN, 13));
		btnHalt.setBounds(151, 27, 135, 42);
		contentPane.add(btnHalt);
		
		//set console panel
			//1.set console panel
		JPanel panelConsole = new JPanel();
		TitledBorder tbConsole = new TitledBorder("console");
		panelConsole.setBorder(tbConsole);
		panelConsole.setBounds(637, 28, 283, 519);
		contentPane.add(panelConsole);
		panelConsole.setLayout(null);
			//2.set scrollPane
		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(12, 21, 259, 492);
		panelConsole.add(scrollPaneConsole);
			//3.set textarea in the scrollPane
		txtrConsole = new JTextArea();//!!!!!!!!!!!!!!!!!!!这里注意，能够让外面调用这个txtrConsole，这行不能写成 JTextArea txtrConsole = new JTextArea();!!!!!!!!
		txtrConsole.setEditable(false);
		txtrConsole.setText("Please press the IPL button to begin.");
		scrollPaneConsole.setViewportView(txtrConsole);
//		scrollPaneConsole.setVerticalScrollBarPolicy(22);
		
		JPanel panelRegister = new JPanel();
		TitledBorder tbRegister = new TitledBorder("Registers");
		panelRegister.setBorder(tbRegister);
		panelRegister.setBounds(294, 28, 331, 310);
		contentPane.add(panelRegister);
		panelRegister.setLayout(null);
		
		JLabel lblPc = new JLabel("PC");
		lblPc.setBounds(27, 20, 36, 16);
		panelRegister.add(lblPc);
		
		JLabel lblIr = new JLabel("IR");
		lblIr.setBounds(27, 40, 36, 16);
		panelRegister.add(lblIr);
		
		JLabel lblMar = new JLabel("MAR");
		lblMar.setBounds(27, 60, 36, 16);
		panelRegister.add(lblMar);
		
		JLabel lblMdr = new JLabel("MDR");
		lblMdr.setBounds(27, 80, 36, 16);
		panelRegister.add(lblMdr);
		
		JLabel lblR = new JLabel("R0");
		lblR.setBounds(27, 100, 36, 16);
		panelRegister.add(lblR);
		
		JLabel lblR_1 = new JLabel("R1");
		lblR_1.setBounds(27, 120, 36, 16);
		panelRegister.add(lblR_1);
		
		JLabel lblR_2 = new JLabel("R2");
		lblR_2.setBounds(27, 140, 36, 16);
		panelRegister.add(lblR_2);
		
		JLabel lblR_3 = new JLabel("R3");
		lblR_3.setBounds(27, 160, 36, 16);
		panelRegister.add(lblR_3);
		
		JLabel lblX = new JLabel("X1");
		lblX.setBounds(27, 180, 36, 16);
		panelRegister.add(lblX);
		
		JLabel lblX_1 = new JLabel("X2");
		lblX_1.setBounds(27, 200, 36, 16);
		panelRegister.add(lblX_1);
		
		JLabel lblX_2 = new JLabel("X3");
		lblX_2.setBounds(27, 220, 36, 16);
		panelRegister.add(lblX_2);
		
		
		
		textField_PC = new JTextField();
		textField_PC.setBounds(66, 20, 186, 19);
		panelRegister.add(textField_PC);
		textField_PC.setColumns(10);
		
		textField_IR = new JTextField();
		textField_IR.setEditable(false);
		textField_IR.setEnabled(false);
		textField_IR.setColumns(10);
		textField_IR.setBounds(66, 40, 186, 19);
		panelRegister.add(textField_IR);
		
		textField_MAR = new JTextField();
		textField_MAR.setEnabled(false);
		textField_MAR.setColumns(10);
		textField_MAR.setBounds(66, 60, 186, 19);
		panelRegister.add(textField_MAR);
		
		textField_MDR = new JTextField();
		textField_MDR.setEnabled(false);
		textField_MDR.setColumns(10);
		textField_MDR.setBounds(66, 80, 186, 19);
		panelRegister.add(textField_MDR);
		
		textField_R0 = new JTextField();
		textField_R0.setColumns(10);
		textField_R0.setBounds(66, 100, 186, 19);
		panelRegister.add(textField_R0);
		
		textField_R1 = new JTextField();
		textField_R1.setColumns(10);
		textField_R1.setBounds(66, 120, 186, 19);
		panelRegister.add(textField_R1);
		
		textField_R2 = new JTextField();
		textField_R2.setColumns(10);
		textField_R2.setBounds(66, 140, 186, 19);
		panelRegister.add(textField_R2);
		
		textField_R3 = new JTextField();
		textField_R3.setColumns(10);
		textField_R3.setBounds(66, 160, 186, 19);
		panelRegister.add(textField_R3);
		
		textField_X1 = new JTextField();
		textField_X1.setColumns(10);
		textField_X1.setBounds(66, 180, 186, 19);
		panelRegister.add(textField_X1);
		
		textField_X2 = new JTextField();
		textField_X2.setColumns(10);
		textField_X2.setBounds(66, 200, 186, 19);
		panelRegister.add(textField_X2);
		
		textField_X3 = new JTextField();
		textField_X3.setColumns(10);
		textField_X3.setBounds(66, 220, 186, 19);
		panelRegister.add(textField_X3);
		
		JButton btnLoadRegister = new JButton("Load ");
		btnLoadRegister.setFont(new Font("HanziPen TC", Font.PLAIN, 13));
		btnLoadRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				R0.value = textField_R0.getText();
				R1.value = textField_R1.getText();
				R2.value = textField_R2.getText();
				R3.value = textField_R3.getText();
				X1.value = textField_X1.getText();
				X2.value = textField_X2.getText();
				X3.value = textField_X3.getText();
				textField_R0.setText(R0.value);
				textField_R1.setText(R1.value);
				textField_R2.setText(R2.value);
				textField_R3.setText(R3.value);
				textField_X1.setText(X1.value);
				textField_X2.setText(X2.value);
				textField_X3.setText(X3.value);
				txtrConsole.append("\nLoad Successfully");
				
			}
		});
		btnLoadRegister.setBounds(97, 256, 126, 39);
		panelRegister.add(btnLoadRegister);
		
		JPanel panelMemory = new JPanel();
		TitledBorder tbMemory = new TitledBorder("Memory");
		panelMemory.setBorder(tbMemory);
		panelMemory.setBounds(294, 349, 331, 198);
		contentPane.add(panelMemory);
		panelMemory.setLayout(null);
		
		JLabel lblCurrentAddress = new JLabel("Cur Address");
		lblCurrentAddress.setBounds(22, 20, 85, 16);
		panelMemory.add(lblCurrentAddress);
		
		JLabel lblCurrentValue = new JLabel("Current Value");
		lblCurrentValue.setBounds(22, 50, 96, 16);
		panelMemory.add(lblCurrentValue);
		
		JLabel lblSetAddress = new JLabel("Set Address");
		lblSetAddress.setBounds(22, 80, 91, 16);
		panelMemory.add(lblSetAddress);
		
		JLabel lblSetValue = new JLabel("Set Value");
		lblSetValue.setBounds(22, 110, 68, 16);
		panelMemory.add(lblSetValue);
		

		
		textField_CurrentAddress = new JTextField();
		textField_CurrentAddress.setBounds(125, 20, 180, 23);
		textField_CurrentAddress.setEditable(false);
		panelMemory.add(textField_CurrentAddress);
		textField_CurrentAddress.setColumns(10);
		
		textField_CurrentValue = new JTextField();
		textField_CurrentValue.setColumns(10);
		textField_CurrentValue.setEditable(false);
		textField_CurrentValue.setBounds(125, 50, 180, 23);
		panelMemory.add(textField_CurrentValue);
		
		textField_SetAddress = new JTextField();
		textField_SetAddress.setColumns(10);
		textField_SetAddress.setBounds(125, 80, 180, 23);
		panelMemory.add(textField_SetAddress);
		
		textField_SetValue = new JTextField();
		textField_SetValue.setColumns(10);
		textField_SetValue.setBounds(125, 110, 180, 23);
		panelMemory.add(textField_SetValue);
		
		JButton btnSetMemory = new JButton("Load");
		btnSetMemory.setFont(new Font("HanziPen TC", Font.PLAIN, 13));
		btnSetMemory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Memory.memoryList.get(Integer.parseInt(textField_SetAddress.getText(),2)).setValue(textField_SetValue.getText());
				Memory.memoryList.get(Integer.parseInt(textField_SetAddress.getText(),2)).setAddress(textField_SetAddress.getText());
				refresh();
				txtrConsole.append("\nLoad Memory Successfully");
				txtrConsole.append("\nMemory Address: " + Memory.memoryList.get(Integer.parseInt(textField_SetAddress.getText(),2)).getAddress());
				txtrConsole.append("\nMemory Content: " + Memory.memoryList.get(Integer.parseInt(textField_SetAddress.getText(),2)).getValue());
				
			}
		});
		btnSetMemory.setBounds(149, 141, 118, 39);
		panelMemory.add(btnSetMemory);
		
		JPanel panelProgram = new JPanel();
		TitledBorder tbpanelProgram = new TitledBorder("Program");
		panelProgram.setBorder(tbpanelProgram);
		panelProgram.setBounds(9, 172, 278, 303);
		contentPane.add(panelProgram);
		panelProgram.setLayout(null);
		
		JScrollPane scrollPaneProgram = new JScrollPane();
		scrollPaneProgram.setBounds(7, 19, 264, 275);
		panelProgram.add(scrollPaneProgram);
		
		txtrProgram = new JTextField();
		scrollPaneProgram.setViewportView(txtrProgram);
		txtrProgram.setText();
		
		
		JButton btnRunProgram = new JButton("Run Program");
		btnRunProgram.setFont(new Font("HanziPen SC", Font.PLAIN, 13));
		btnRunProgram.setBounds(82, 494, 124, 36);
		contentPane.add(btnRunProgram);
		


	}
	
	public static void refresh(){
		textField_PC.setText(PC.value);
		textField_R0.setText(R0.value);
		textField_R1.setText(R1.value);
		textField_R2.setText(R2.value);
		textField_R3.setText(R3.value);
		textField_X1.setText(X1.value);
		textField_X2.setText(X2.value);
		textField_X3.setText(X3.value);
		textField_IR.setText(Instruction.command);
		textField_MAR.setText(MAR.value);
		textField_MDR.setText(MDR.value);
		textField_IR.setText(IR.value);
		textField_CurrentAddress.setText(PC.value);
		textField_CurrentValue.setText(Memory.memoryList.get(Integer.parseInt(PC.value, 2)).getValue());
		System.out.println("text is: "+Memory.memoryList.get(Integer.parseInt(PC.value, 2)).getValue());
		
		
	}
}
