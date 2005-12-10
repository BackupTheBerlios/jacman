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

package andyr.jacman.utils;

import java.net.URLClassLoader;

import javax.swing.ImageIcon;
import javax.swing.JTable;

/**
 * Jacman utilities class containing useful methods required by many (GUI related) classes.
 * 
 * @author Andrew Roberts
 */

public final class JacmanUtils {

    /**
     * Utility method for returning an ImageIcon based on the <tt>iconName</tt> URL. Primarily
     * utilised for adding icons to JMenuItems.
     * @param path image resource
     * @return an ImageIcon object
     */
    public static ImageIcon loadIcon(String iconName) {
                
        return new ImageIcon(URLClassLoader.getSystemResource(iconName));
    }
    
    /**
     * Utility method for setting the widths of columns within a table to their 'optimal', 
     * i.e., wide enough to fit their contents. 
     * 
     * @param the JTable instance that needs its column widths changing.
     */
    public static void setOptimalTableWidth(JTable table) {
        
        for (int col = 0; col < table.getColumnCount(); col++) {
            JTableHelper.setOptimalColumnWidth(table, col);
        }
    }
}
