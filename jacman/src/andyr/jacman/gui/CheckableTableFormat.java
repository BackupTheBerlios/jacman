/*
 * Copyright 2005 The Jacman Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package andyr.jacman.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;

public class CheckableTableFormat implements AdvancedTableFormat,
		WritableTableFormat {
	IdentityHashMap selection;
	// WeakHashMap selection;
	BeanTableFormat wrapped;

	public CheckableTableFormat(BeanTableFormat wrapped) {
		this.wrapped = wrapped;
		this.selection = new IdentityHashMap();
		// this.selection = new WeakHashMap();
	}

	public Class getColumnClass(int column) {
		if (column == 0)
			return Boolean.class;
		return wrapped.getColumnClass(column - 1);
	}

	public Comparator getColumnComparator(int column) {
		if (column == 0)
			return GlazedLists.booleanComparator();
		return wrapped.getColumnComparator(column - 1);
	}

	public int getColumnCount() {
		return wrapped.getColumnCount() + 1;
	}

	public String getColumnName(int column) {
		if (column == 0)
			return "Sel.";
		return wrapped.getColumnName(column - 1);
	}

	public Object getColumnValue(Object baseObject, int column) {
		if (column > 0)
			return wrapped.getColumnValue(baseObject, column - 1);
		return Boolean.valueOf(isSelected(baseObject));
	}

	/**
	 * @param bean
	 * @return
	 */
	boolean isSelected(Object bean) {
		return selection.containsKey(bean);
	}

	public List getSelection() {
		return new ArrayList(selection.values());
	}

	public boolean isEditable(Object baseObject, int column) {
		if (column == 0)
			return true;
		else
			return wrapped.isEditable(baseObject, column - 1);
	}

	public Object setColumnValue(Object baseObject, Object editedValue,
			int column) {
		if (column == 0) {
			if (editedValue.equals(Boolean.TRUE)) {
				selection.put(baseObject, baseObject);
			} else {
				selection.remove(baseObject);
			}
			return null;
		} else {
			return wrapped.setColumnValue(baseObject, editedValue, column);
		}
	}

}