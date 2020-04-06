package io.github.tasratech.easycopier.customtable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CopyTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel comp = new JLabel();
		comp.setText(String.valueOf(value));
		comp.setOpaque(true);
		if (isSelected) {
			comp.setBackground(Color.LIGHT_GRAY);
		}
		return comp;
	}
}
