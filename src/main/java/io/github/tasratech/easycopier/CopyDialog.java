package io.github.tasratech.easycopier;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import io.github.tasratech.easycopier.customtable.CopyTableModel;

public class CopyDialog implements ActionListener {

	private CopyTableModel model;

	private JDialog dialog;
	private JTextField srcInput;
	private JButton srcCfButton;
	private JTextField destInput;
	private JButton destCfButton;

	private JButton applyButton;
	private JButton cancelButton;

	public static void showAddDialog(Component comp, CopyTableModel model) {
		CopyDialog dialog = new CopyDialog(comp, "Add Copy-File", null, null);
		dialog.setActionListener(e -> {
			model.addRow(dialog.getElement());
			model.fireTableRowsInserted(model.getRowCount(), model.getRowCount());
			dialog.disponse();
		});
		dialog.setModel(model);
		dialog.show();
	}

	public static void showEditDialog(Component comp, CopyTableModel model, int index) {
		CopyElement element = model.getElementAt(index);
		CopyDialog dialog = new CopyDialog(comp, "Edit Copy-File", element.getSrc().getAbsolutePath(),
				element.getDest().getAbsolutePath());
		dialog.setActionListener(e -> {
			model.editRow(index, dialog.getElement());
			model.fireTableRowsUpdated(index, index);
			dialog.disponse();
		});
		dialog.setModel(model);
		dialog.show();
	}

	private CopyDialog(Component comp, String title, String defSrc, String defDest) {
		final int columns = 40;
		srcInput = new JTextField();
		srcInput.setColumns(columns);
		srcInput.setText(defSrc);
		srcCfButton = new JButton();
		srcCfButton.setText("Cf.");
		srcCfButton.addActionListener(this);
		destInput = new JTextField();
		destInput.setColumns(columns);
		destInput.setText(defDest);
		destCfButton = new JButton();
		destCfButton.setText("Cf.");
		destCfButton.addActionListener(this);

		final Dimension labelSize = new Dimension(30, 20);
		JPanel srcPanel = new JPanel();
		srcPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel srcLabel = new JLabel();
		srcLabel.setText("Src");
		srcLabel.setPreferredSize(labelSize);
		srcLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		srcPanel.add(srcLabel);
		srcPanel.add(srcInput);
		srcPanel.add(srcCfButton);

		JLabel destLabel = new JLabel();
		destLabel.setText("Dest");
		destLabel.setPreferredSize(labelSize);
		destLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JPanel destPanel = new JPanel();
		destPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		destPanel.add(destLabel);
		destPanel.add(destInput);
		destPanel.add(destCfButton);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(2, 1));
		inputPanel.add(srcPanel);
		inputPanel.add(destPanel);

		applyButton = new JButton();
		applyButton.setText("Apply");

		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(this);

		JPanel opPanel = new JPanel();
		opPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		opPanel.add(applyButton);
		opPanel.add(cancelButton);

		dialog = new JDialog();
		dialog.setLayout(new BorderLayout());
		dialog.add(inputPanel, BorderLayout.CENTER);
		dialog.add(opPanel, BorderLayout.SOUTH);
		dialog.setTitle(title);
		dialog.pack();
		dialog.setLocationRelativeTo(comp);
		dialog.setResizable(false);
		dialog.setModal(true);
	}

	public void show() {
		if (!dialog.isVisible()) {
			dialog.setVisible(true);
			applyButton
			.setEnabled(!StringUtils.isBlank(srcInput.getText()) || StringUtils.isBlank(destInput.getText()));
		}
	}

	public void disponse() {
		if (dialog.isVisible()) {
			dialog.setVisible(false);
		}
	}

	public void setActionListener(ActionListener listener) {
		applyButton.addActionListener(listener);
	}

	public CopyElement getElement() {
		String srcPath = srcInput.getText();
		String destPath = destInput.getText();
		File destFile = new File(destPath);
		return new CopyElement(new File(srcPath), destFile);
	}

	public CopyTableModel getModel() {
		return model;
	}

	public void setModel(CopyTableModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			dialog.setVisible(false);
			return;
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(srcInput.getText()));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = chooser.showOpenDialog(dialog);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (e.getSource() == srcCfButton) {
				srcInput.setText(file.getAbsolutePath());
			} else if (e.getSource() == destCfButton) {
				destInput.setText(file.getAbsolutePath());
			}
		}
	}
}
