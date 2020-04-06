package io.github.tasratech.easycopier;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.io.FilenameUtils;

import io.github.tasratech.easycopier.customtable.CopyTableCellRenderer;
import io.github.tasratech.easycopier.customtable.CopyTableModel;

public class EasyCopier implements Runnable, ActionListener, ListSelectionListener {

	// Main Frame
	private JFrame frame;
	// Task tray
	private TrayIcon icon;
	private MenuItem exitTask;
	// Control Panel
	private JButton copyButton;
	private JButton copyAllButton;
	private JButton upButton;
	private JButton downButton;
	private JButton addButton;
	private JButton reprintButton;
	private JButton editButton;
	private JButton removeButton;
	// Table Panel
	private JTable table;
	private CopyTableModel model;
	// Details Panel
	private JLabel previewLabel;
	private JLabel detailsLabel;

	public static void main(String[] args) {
		EasyCopier run = new EasyCopier();
		Thread thread = new Thread(run);
		thread.start();
	}

	@Override
	public void run() {
		try {
			PopupMenu menu = new PopupMenu();
			exitTask = new MenuItem("Exit");
			exitTask.addActionListener(this);
			menu.add(exitTask);

			Image image = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
			icon = new TrayIcon(image);
			icon.addActionListener(this);
			icon.setPopupMenu(menu);
			SystemTray.getSystemTray().add(icon);
		} catch (IOException | AWTException e) {

		}

		model = new CopyTableModel();
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		table.setModel(model);
		JScrollPane scTable = new JScrollPane();
		scTable.setViewportView(table);
		scTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setMaxWidth(40);
		columnModel.getColumn(1).setMaxWidth(80);
		columnModel.getColumn(2).setMaxWidth(80);
		CopyTableCellRenderer cellRenderer = new CopyTableCellRenderer();
		Enumeration<TableColumn> tableCell = columnModel.getColumns();
		while (tableCell.hasMoreElements()) {
			TableColumn column = tableCell.nextElement();
			column.setCellRenderer(cellRenderer);
		}

		final Dimension buttonSize = new Dimension(120, 30);

		copyButton = new JButton();
		copyButton.setText("Copy");
		copyButton.addActionListener(this);
		copyButton.setMaximumSize(buttonSize);
		copyButton.setEnabled(false);

		copyAllButton = new JButton();
		copyAllButton.setText("Copy All");
		copyAllButton.addActionListener(this);
		copyAllButton.setMaximumSize(buttonSize);

		upButton = new JButton();
		upButton.setText("UP");
		upButton.addActionListener(this);
		upButton.setMaximumSize(buttonSize);
		upButton.setEnabled(false);

		downButton = new JButton();
		downButton.setText("DOWN");
		downButton.addActionListener(this);
		downButton.setMaximumSize(buttonSize);
		downButton.setEnabled(false);

		addButton = new JButton();
		addButton.setText("Add");
		addButton.addActionListener(this);
		addButton.setMaximumSize(buttonSize);

		reprintButton = new JButton();
		reprintButton.setText("Reprint");
		reprintButton.addActionListener(this);
		reprintButton.setMaximumSize(buttonSize);
		reprintButton.setEnabled(false);

		editButton = new JButton();
		editButton.setText("Edit");
		editButton.addActionListener(this);
		editButton.setMaximumSize(buttonSize);
		editButton.setEnabled(false);

		removeButton = new JButton();
		removeButton.setText("Remove");
		removeButton.addActionListener(this);
		removeButton.setMaximumSize(buttonSize);
		removeButton.setEnabled(false);

		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(copyButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(copyAllButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		controlPanel.add(upButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(downButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		controlPanel.add(addButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(reprintButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(editButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(removeButton);

		final Dimension detailsSize = new Dimension(148, 148);

		previewLabel = new JLabel();
		previewLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		previewLabel.setBackground(Color.WHITE);
		previewLabel.setOpaque(true);
		previewLabel.setPreferredSize(detailsSize);

		detailsLabel = new JLabel();
		detailsLabel.setPreferredSize(detailsSize);
		detailsLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel detailsPanel = new JPanel();
		detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		detailsPanel.setLayout(new BorderLayout());
		detailsPanel.add(previewLabel, BorderLayout.SOUTH);
		detailsPanel.add(detailsLabel, BorderLayout.CENTER);

		JScrollPane scDetails = new JScrollPane();
		scDetails.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scDetails.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scDetails.setViewportView(detailsPanel);

		frame = new JFrame();
		frame.getContentPane().add(scTable, BorderLayout.CENTER);
		frame.getContentPane().add(controlPanel, BorderLayout.EAST);
		frame.getContentPane().add(scDetails, BorderLayout.WEST);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Easy Copier");
		frame.setIconImage(icon.getImage());
		frame.setSize(1280, 720);
		frame.setVisible(true);

		for (int i = 0; i < 50; i++) {
			model.addRow(new CopyElement(new File("C:\\Users\\Keita\\Pictures\\" + i + ".png"),
					new File("C:\\Users\\Keita\\Pictures\\TEST\\" + i + ".png")));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == icon) {
			icon.displayMessage("Easy Copier", String.format("Copied %d items.", model.getRowCount()),
					MessageType.INFO);
		} else if (e.getSource() == exitTask) {
			System.exit(0);
		} else if (e.getSource() == copyButton) {
			JOptionPane.showMessageDialog(frame, "Copied.");
		} else if (e.getSource() == copyAllButton) {
			JOptionPane.showMessageDialog(frame, "" + table.getSelectedRow());
		} else if (e.getSource() == upButton) {
			if (table.getSelectedRow() > 0) {
				final int index = table.getSelectedRow();
				model.exchangeRow(index, index - 1);
				table.changeSelection(index - 1, index - 1, false, false);
			}
		} else if (e.getSource() == downButton) {
			if (table.getSelectedRow() + 1 < model.getRowCount()) {
				final int index = table.getSelectedRow();
				model.exchangeRow(index, index + 1);
				table.changeSelection(index + 1, -1, false, false);
			}
		} else if (e.getSource() == addButton) {
			CopyDialog.showAddDialog(frame, model);
		} else if (e.getSource() == reprintButton) {
			model.addRow(model.getElementAt(table.getSelectedRow()));
		} else if (e.getSource() == editButton) {
			CopyDialog.showEditDialog(frame, model, table.getSelectedRow());
		} else if (e.getSource() == removeButton) {
			int index = table.getSelectedRow();
			model.removeRow(index);
			if (index < model.getRowCount()) {
				table.changeSelection(index, -1, false, false);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		boolean isEmpty = table.getSelectionModel().isSelectionEmpty();
		copyButton.setEnabled(!isEmpty);
		reprintButton.setEnabled(!isEmpty);
		editButton.setEnabled(!isEmpty);
		removeButton.setEnabled(!isEmpty);
		if (isEmpty) {
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			previewLabel.setIcon(null);
			return;
		}

		final int index = table.getSelectedRow();
		CopyElement element = model.getElementAt(index);
		previewLabel.setIcon(element.getType().getIcon());
		upButton.setEnabled(index > 0);
		downButton.setEnabled(index + 1 < model.getRowCount());

		long size = element.getSrc().length();

		final String BR = "<br>";
		StringBuilder sb = new StringBuilder();
		sb.append("<html><p style=\"width:108px\">");
		sb.append("Name: " + FilenameUtils.getName(element.getSrc().getAbsolutePath()));
		sb.append(BR);
		sb.append("Path: " + element.getSrc().getAbsolutePath());
		sb.append(BR);
		sb.append("</p></html>");
		detailsLabel.setText(sb.toString());
	}
}
