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

import java.awt.Insets;
import java.net.URLClassLoader;

import javax.swing.ImageIcon;
import javax.swing.JTable;

/**
 * Jacman utilities class containing useful methods required by many (GUI related) classes.
 * 
 * @author Andrew Roberts
 */

public final class JacmanUtils {
	private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);

    
    public static ImageIcon loadIcon(String iconName) {
                
        return new ImageIcon(URLClassLoader.getSystemResource(iconName));
    }
    

    public static void setOptimalTableWidth(JTable table) {
        
        for (int col = 0; col < table.getColumnCount(); col++) {
            JTableHelper.setOptimalColumnWidth(table, col);
        }
    }
}
