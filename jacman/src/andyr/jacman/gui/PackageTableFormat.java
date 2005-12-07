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

import java.util.Comparator;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;

public class PackageTableFormat implements AdvancedTableFormat {
    BeanTableFormat wrapped;

    public PackageTableFormat(BeanTableFormat wrapped) {
      this.wrapped = wrapped;
    }

    public Class getColumnClass(int column) {
        return wrapped.getColumnClass(column);
    }

    public Comparator getColumnComparator(int column) {
        return wrapped.getColumnComparator(column);
    }

    public int getColumnCount() {
      return wrapped.getColumnCount();
    }

    public String getColumnName(int column) {
        return wrapped.getColumnName(column);
    }

    public Object getColumnValue(Object baseObject, int column) {
        return wrapped.getColumnValue(baseObject, column);
      
    }

    public boolean isEditable(Object baseObject, int column) {
      
        return wrapped.isEditable(baseObject, column - 1);
    }
    

  }