package io.github.tasratech.easycopier.customtable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import io.github.tasratech.easycopier.CopyElement;

public class CopyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<CopyElement> elements;

	public CopyTableModel() {
		elements = new ArrayList<>();
	}

	public void addRow(CopyElement element) {
		elements.add(element);
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	public void removeRow(int index) {
		elements.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public void editRow(int index, CopyElement newElement) {
		elements.set(index, newElement);
		fireTableRowsUpdated(index, index);
	}

	public void exchangeRow(int index1, int index2) {
		CopyElement element1 = elements.get(index1);
		CopyElement element2 = elements.get(index2);
		elements.set(index1, element2);
		elements.set(index2, element1);
		fireTableDataChanged();
	}

	public CopyElement getElementAt(int index) {
		return elements.get(index);
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "No.";
		case 1:
			return "Type";
		case 2:
			return "Status";
		case 3:
			return "Src-File";
		case 4:
			return "Dest-Directory";
		default:
			return super.getColumnName(column);
		}
	}

	@Override
	public int getRowCount() {
		return elements.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return elements.get(rowIndex).getType().getName();
		case 2:
			return elements.get(rowIndex).getStatus();
		case 3:
			return elements.get(rowIndex).getSrc().getAbsolutePath();
		case 4:
			return elements.get(rowIndex).getDest().getParentFile();
		default:
			return null;
		}
	}
}
