package project2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class NewNotepad {
	private JFrame frame;
	private JTextArea textarea;
	private JMenuBar menuBar;
	private JMenu fileMenu, editMenu, formatMenu, commandPromptMenu;
	private JMenuItem itemNew, itemNewWindow, itemOpen, itemSaveAs, itemSave, itemExit;
	private JMenuItem itemWordWrap;
	private JMenu fontMenu, fontSizeMenu;
	private JMenuItem itemArial, itemTimesNewRoman, itemConsolas;
	private JMenuItem size10, size14, size18, size22;
	private JMenuItem itemOpenCMD;
	private JMenuItem itemAddCppBoilerplate, itemAddPythonBoilerplate, itemAddJavaBoilerplate;

	private String openPath = null;
	private String openFileName = null;
	private boolean isWordWrapEnabled = false; // Word wrap state

	public NewNotepad() {
		createFrame();
		createTextArea();
		createScrollBars();
		createMenuBar();
		createFileMenuItems();
		createEditMenuItems();
		createFormatMenuItems();
		createFontMenuItems();
		createCommandPromptMenu();
	}

	private void createFrame() {
		frame = new JFrame(" NEW Notepad");
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void createTextArea() {
		textarea = new JTextArea();
		textarea.setFont(new Font("Arial", Font.PLAIN, 14)); // Default font
		frame.add(textarea);
	}

	private void createScrollBars() {
		JScrollPane scroll = new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(scroll);
	}

	private void createMenuBar() {
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		formatMenu = new JMenu("Format");
		commandPromptMenu = new JMenu("Command Prompt");

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(formatMenu);
		menuBar.add(commandPromptMenu);
	}

	private void createFileMenuItems() {
		itemNew = new JMenuItem("New");
		itemNew.addActionListener(e -> {
			textarea.setText("");
			openPath = null;
			openFileName = null;
			frame.setTitle("Untitled");
		});

		itemNewWindow = new JMenuItem("New Window");
		itemNewWindow.addActionListener(e -> new NewNotepad());

		itemOpen = new JMenuItem("Open");
		itemOpen.addActionListener(e -> openFile());

		itemSaveAs = new JMenuItem("Save As");
		itemSaveAs.addActionListener(e -> saveFileAs());

		itemSave = new JMenuItem("Save");
		itemSave.addActionListener(e -> saveFile());

		itemExit = new JMenuItem("Exit");
		itemExit.addActionListener(e -> frame.dispose());

		fileMenu.add(itemNew);
		fileMenu.add(itemNewWindow);
		fileMenu.add(itemOpen);
		fileMenu.add(itemSave);
		fileMenu.add(itemSaveAs);
		fileMenu.addSeparator();
		fileMenu.add(itemExit);
	}

	private void createEditMenuItems() {
		itemAddCppBoilerplate = new JMenuItem("Add C++ Boilerplate");
		itemAddCppBoilerplate.addActionListener(e -> addCppBoilerplate());

		itemAddPythonBoilerplate = new JMenuItem("Add Python Boilerplate");
		itemAddPythonBoilerplate.addActionListener(e -> addPythonBoilerplate());

		itemAddJavaBoilerplate = new JMenuItem("Add Java Boilerplate");
		itemAddJavaBoilerplate.addActionListener(e -> addJavaBoilerplate());

		editMenu.add(itemAddCppBoilerplate);
		editMenu.add(itemAddPythonBoilerplate);
		editMenu.add(itemAddJavaBoilerplate);
	}

	private void createFormatMenuItems() {
		itemWordWrap = new JMenuItem("Word Wrap");
		itemWordWrap.addActionListener(e -> toggleWordWrap());

		fontMenu = new JMenu("Font");
		fontSizeMenu = new JMenu("Font Size");

		formatMenu.add(itemWordWrap);
		formatMenu.add(fontMenu);
		formatMenu.add(fontSizeMenu);

		size10 = new JMenuItem("10");
		size14 = new JMenuItem("14");
		size18 = new JMenuItem("18");
		size22 = new JMenuItem("22");

		size10.addActionListener(e -> setFontSize(10));
		size14.addActionListener(e -> setFontSize(14));
		size18.addActionListener(e -> setFontSize(18));
		size22.addActionListener(e -> setFontSize(22));

		fontSizeMenu.add(size10);
		fontSizeMenu.add(size14);
		fontSizeMenu.add(size18);
		fontSizeMenu.add(size22);
	}

	private void createFontMenuItems() {
		itemArial = new JMenuItem("Arial");
		itemTimesNewRoman = new JMenuItem("Times New Roman");
		itemConsolas = new JMenuItem("Consolas");

		itemArial.addActionListener(e -> setFont("Arial"));
		itemTimesNewRoman.addActionListener(e -> setFont("Times New Roman"));
		itemConsolas.addActionListener(e -> setFont("Consolas"));

		fontMenu.add(itemArial);
		fontMenu.add(itemTimesNewRoman);
		fontMenu.add(itemConsolas);
	}

	@SuppressWarnings("deprecation")
	private void createCommandPromptMenu() {
		itemOpenCMD = new JMenuItem("Open CMD");
		itemOpenCMD.addActionListener(e -> {
			try {
				Runtime.getRuntime().exec("cmd.exe /c start");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		commandPromptMenu.add(itemOpenCMD);
	}

	private void openFile() {
		FileDialog fileDialog = new FileDialog(frame, "Open", FileDialog.LOAD);
		fileDialog.setVisible(true);

		String path = fileDialog.getDirectory();
		String filename = fileDialog.getFile();

		if (path != null && filename != null) {
			openPath = path;
			openFileName = filename;
			frame.setTitle(filename);

			try (BufferedReader br = new BufferedReader(new FileReader(path + filename))) {
				textarea.setText("");
				String line;
				while ((line = br.readLine()) != null) {
					textarea.append(line + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void saveFileAs() {
		FileDialog fileDialog = new FileDialog(frame, "Save As", FileDialog.SAVE);
		fileDialog.setVisible(true);

		String path = fileDialog.getDirectory();
		String filename = fileDialog.getFile();

		if (path != null && filename != null) {
			writeFile(path + filename);
			frame.setTitle(filename);
		}
	}

	private void saveFile() {
		if (openFileName != null && openPath != null) {
			writeFile(openPath + openFileName);
		} else {
			saveFileAs();
		}
	}

	private void writeFile(String fullPath) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath))) {
			bw.write(textarea.getText());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void toggleWordWrap() {
		// Toggle word wrap logic
		isWordWrapEnabled = !isWordWrapEnabled;
		textarea.setLineWrap(isWordWrapEnabled);
		textarea.setWrapStyleWord(isWordWrapEnabled);

		// Update menu item text to indicate current state
		itemWordWrap.setText(isWordWrapEnabled ? "Word Wrap Off" : "Word Wrap on");
	}

	private void setFont(String fontName) {
		textarea.setFont(new Font(fontName, Font.PLAIN, textarea.getFont().getSize()));
	}

	private void setFontSize(int size) {
		textarea.setFont(new Font(textarea.getFont().getName(), Font.PLAIN, size));
	}

	private void addCppBoilerplate() {
		String cppBoilerplate = "#include <iostream>\nusing namespace std;\n\nint main() {\n\tcout << \"Hello, World!\" << endl;\n\treturn 0;\n}";
		textarea.setText(cppBoilerplate);
	}

	private void addPythonBoilerplate() {
		String pythonBoilerplate = "# Python Boilerplate\ndef main():\n\tprint(\"Hello, World!\")\n\nif __name__ == \"__main__\":\n\tmain()";
		textarea.setText(pythonBoilerplate);
	}

	private void addJavaBoilerplate() {
		String javaBoilerplate = "public class Main {\n\tpublic static void main(String[] args) {\n\t\tSystem.out.println(\"Hello, World!\");\n\t}\n}";
		textarea.setText(javaBoilerplate);
	}

	public static void main(String[] args) {
		new NewNotepad();
	}
}
